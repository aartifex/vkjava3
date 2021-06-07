package org.artifex.vulkan.compute;

import org.artifex.util.DebugUtil;
import org.artifex.vulkan.Device;
import org.artifex.vulkan.VulkanBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Struct;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.vulkan.VK10.*;

public class DescriptorSet
{

    public DescriptorSet(Device device, DescriptorBindings bindings,int maxSets){
        this(device,bindings,0,maxSets);
    }
    public DescriptorSet(Device device, DescriptorBindings bindings,int layoutFlags,int maxSets){
        this.device=device;
        this.bindings=bindings;
        this.buffers = new ArrayList<>();

        try(MemoryStack stack = MemoryStack.stackPush()){
            //Layout
            VkDescriptorSetLayoutCreateInfo info = VkDescriptorSetLayoutCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO)
                    .pBindings(bindings.getBindings())
                    .flags(layoutFlags);

            LongBuffer pLayout = stack.mallocLong(1);
            DebugUtil.vkCheck(
                    vkCreateDescriptorSetLayout(device.getDevice(),info,null,pLayout),
                    "Could not create descriptor layout"
            );
            this.descriptorLayout = pLayout.get(0);

            int[] i={0};
        // POOL
            VkDescriptorPoolSize.Buffer poolSize = VkDescriptorPoolSize.callocStack(bindings.getSize(),stack);
                    poolSize.forEach((p)->{
                        VkDescriptorSetLayoutBinding binding = bindings.getBindings().get(i[0]++);
                        p.descriptorCount(binding.descriptorCount())
                                .type(binding.descriptorType());
                    });

            VkDescriptorPoolCreateInfo poolCreateInfo = VkDescriptorPoolCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO)
                    .pPoolSizes(poolSize)
                    .maxSets(maxSets);

            LongBuffer pPool = stack.mallocLong(1);

            DebugUtil.vkCheck(
                    vkCreateDescriptorPool(device.getDevice(),poolCreateInfo,null,pPool),
                    "Could not create descriptor pool"
            );

            this.descriptorPool=pPool.get(0);
    //BUFFER / MEMALLOC
            i[0]=0;
             bufferInfos = VkDescriptorBufferInfo
                    .calloc(bindings.getSize());
            bufferInfos.forEach((b)->{
                VulkanBuffer buffer = new VulkanBuffer(device,bindings.getByteSizes().get(i[0]),
                        VK_BUFFER_USAGE_STORAGE_BUFFER_BIT
                        ,VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT);
                long offset = (i[0]==0) ? 0 : buffers.get(i[0]-1).getAllocationSize();

                System.out.println("\t\t"+buffer.getBuffer() + " size " + buffer.getAllocationSize());
                b.buffer(buffer.getBuffer())
                        .offset(0)
                        .range(buffer.getAllocationSize());
                i[0]++;
                buffers.add(buffer);
            });
            VkDescriptorSetAllocateInfo.Buffer allocInfos = VkDescriptorSetAllocateInfo.callocStack(bindings.getSize(),stack);
            i[0]=0;
            LongBuffer pAlloc = stack.mallocLong(1);
            allocInfos.forEach((a)->{
                a.pSetLayouts(pLayout)
                        .descriptorPool(pPool.get(0))
                        .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO);
                vkAllocateDescriptorSets(device.getDevice(),a,pAlloc);
                if(pAlloc.get(0)==0L) return;
                handles.add(pAlloc.get(0));
            });

        }
    }


    public void cleanup(){
//        descriptorWrite.forEach(Struct::free);
//        descriptorCopy.forEach(Struct::free);
        buffers.forEach(VulkanBuffer::cleanup);
        handles.forEach((l)->{
            vkFreeDescriptorSets(device.getDevice(),descriptorPool,l);
        });
        vkDestroyDescriptorPool(device.getDevice(),descriptorPool,null);
        vkDestroyDescriptorSetLayout(device.getDevice(),descriptorLayout,null);
    }


    public List<Long> getHandles() {
        return handles;
    }

    public VkDescriptorBufferInfo.Buffer getBufferInfos() {
        return bufferInfos;
    }

    public VkDescriptorBufferInfo.Buffer getBufferInfo(int i) {
        return VkDescriptorBufferInfo.calloc(1).put(0,
                bufferInfos.get(i));
    }
    public Device getDevice() {
        return device;
    }
    public VulkanBuffer getBuffer(int i){
        return buffers.get(i);
    }

    public long getDescriptorLayout() {
        return descriptorLayout;
    }


    private final Device device;
    private final DescriptorBindings bindings;
    private final long descriptorLayout;
    private final long descriptorPool;

    private final List<VulkanBuffer> buffers;
    private final List<Long> handles = new ArrayList<>();

    private VkDescriptorBufferInfo.Buffer bufferInfos;



    private final int descriptorType = VK_DESCRIPTOR_TYPE_STORAGE_BUFFER;
    private final int descriptorCount = 1;

    public int getDescriptorCount() {
        return descriptorCount;
    }

    public int getDescriptorType() {
        return descriptorType;
    }
}
