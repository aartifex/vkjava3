package org.artifex.vulkan.descriptors;

import org.artifex.math.Matrix3fBuffer;
import org.artifex.math.Vector4fBuffer;
import org.artifex.util.DebugUtil;
import org.artifex.vulkan.Device;
import org.artifex.vulkan.VulkanBuffer;
import org.joml.Vector4f;
import org.lwjgl.system.CustomBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.vulkan.VK10.*;

public class DescriptorSet
{


    public DescriptorSet(Device device,DescriptorBindings bindings, int layoutFlags){
        this.device=device;
        this.bindings=bindings;
        try(MemoryStack stack = MemoryStack.stackPush()){

            //descriptor layout
            VkDescriptorSetLayoutCreateInfo info = VkDescriptorSetLayoutCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO)
                    .pBindings(DescriptorBinding.bindingsToBuffer(bindings.getBindings()))
                    .flags(layoutFlags);

            LongBuffer pLayout = stack.mallocLong(1);
            DebugUtil.vkCheck(
                    vkCreateDescriptorSetLayout(device.getDevice(), info, null, pLayout),
                    "Could not create descriptor layout"
            );
            this.layout=pLayout.get(0);

            //descriptor pool
            int[] i=new int[]{0};
            VkDescriptorPoolSize.Buffer poolSize = VkDescriptorPoolSize.callocStack(bindings.getSize(),stack);
            poolSize.forEach((ps)->{
                final int i0=i[0];
                ps.type(bindings.getBindings().get(i0).getDescriptorType())
                .descriptorCount(bindings.getBindings().get(i0).getGlsLayout().getDescriptorCount());
                i[0]++;
            });
            VkDescriptorPoolCreateInfo poolCreateInfo = VkDescriptorPoolCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO)
                    .pPoolSizes(poolSize)
                    .maxSets(bindings.getSize());

            LongBuffer pPool = stack.mallocLong(1);

            DebugUtil.vkCheck(
                    vkCreateDescriptorPool(device.getDevice(),poolCreateInfo,null,pPool),
                    "Could not create descriptor pool"
            );
            pool=pPool.get(0);

            //buffers

            bufferInfo = new ArrayList<>();
            for (DescriptorBinding b : bindings.getBindings()) {
                if(b.getDescriptorType()!=VK_DESCRIPTOR_TYPE_STORAGE_BUFFER)
                    throw new IllegalArgumentException("No support for non-storage buffers yet");
                System.out.println(b.getGlsLayout().totalByteSize() + " yo " + b.getGlsLayout().getDescriptorCount());

                VulkanBuffer buffer = new VulkanBuffer(device,b.getGlsLayout().totalByteSize(),
                        VK_BUFFER_USAGE_STORAGE_BUFFER_BIT
                        ,VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT);
                VkDescriptorBufferInfo.Buffer bufferInfos = VkDescriptorBufferInfo.callocStack(b.getGlsLayout().getDescriptorCount(),stack);
                    i[0]=0;
                bufferInfos.forEach((bi)->{
                    final int i0=i[0];
                    GLSLayout.Descriptor mem = b.getGlsLayout().getDescriptor(i0);
                    long offset = (i0==0) ? 0 : b.getGlsLayout().getDescriptor(i0-1).getSizeof();
                    bufferInfos.get(i0)
                            .buffer(buffer.getBuffer())
                            .offset(offset)
                            .range(mem.getSizeof());
                    i[0]++;
                });
                bufferInfo.add(bufferInfos);
                b.setBuffer(buffer);
            }
        }
    }
    public void allocate(){
        if(setHandle!=0L) return;
        try(MemoryStack stack=MemoryStack.stackPush()){
            VkDescriptorSetAllocateInfo.Buffer allocInfos = VkDescriptorSetAllocateInfo.callocStack(1,stack);
            int i[]={0};
            LongBuffer pAlloc = stack.mallocLong(1);
            allocInfos.forEach((a)->{
                int i0=i[0];
                LongBuffer layouts = stack.longs(getLayout());
                a.pSetLayouts(layouts)
                        .descriptorPool(getPool())
                        .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO);
                vkAllocateDescriptorSets(device.getDevice(),a,pAlloc);
                if(pAlloc.get(0)==0L) return;
                setHandle=pAlloc.get(0);
            });
        }
    }

    public Vector4fBuffer mapAsVector4f(int binding, int descriptorNum){
        VulkanBuffer buffer =bindings.getBinding(binding).getBuffer();
        Objects.requireNonNull(buffer,"Buffer was null. Descriptor has not properly been allocated.");

        GLSLayout layout = bindings.getBinding(binding).getGlsLayout();

        System.out.println(layout.getDescriptor(descriptorNum).getLength() + " wju tjp ");
        Vector4fBuffer v4b = Vector4fBuffer.memVector4fBuffer(buffer.map()+layout.getDescriptor(descriptorNum).getOffset(),
                layout.getDescriptor(descriptorNum).getLength());
        return v4b;

    }
    public Matrix3fBuffer mapAsMatrix3f(int binding, int descriptorNum){
        VulkanBuffer buffer =bindings.getBinding(binding).getBuffer();
        Objects.requireNonNull(buffer,"Buffer was null. Descriptor has not properly been allocated.");

        GLSLayout layout = bindings.getBinding(binding).getGlsLayout();

        Matrix3fBuffer m3b = Matrix3fBuffer.memVector4fBuffer(buffer.map()+layout.getDescriptor(descriptorNum).getOffset(),
                layout.getDescriptor(descriptorNum).getLength());
        return m3b;
    }

    public void unmapBuffer(int binding, int descriptorNum){
        VulkanBuffer buffer =bindings.getBinding(binding).getBuffer();
        Objects.requireNonNull(buffer,"Buffer was null. Descriptor has not properly been allocated.");

        buffer.unmap();
    }


    public void cleanup(){
        bufferInfo.forEach(CustomBuffer::free);
        vkDestroyDescriptorPool(device.getDevice(),pool,null);
        vkDestroyDescriptorSetLayout(device.getDevice(),layout,null);
        bindings.cleanup();
    }

    public long getPool() {
        return pool;
    }

    public long getLayout() {
        return layout;
    }

    public Device getDevice() {
        return device;
    }

    public long getSetHandle() {
        return setHandle;
    }

    public List<VkDescriptorBufferInfo.Buffer> getBufferInfo() {
        return bufferInfo;
    }


    public DescriptorBindings getBindings() {
        return bindings;
    }

    private final long layout;
    private final long pool;
    private long setHandle = 0L;
    private final Device device;
    private final List<VkDescriptorBufferInfo.Buffer> bufferInfo;
    private final DescriptorBindings bindings;

}
