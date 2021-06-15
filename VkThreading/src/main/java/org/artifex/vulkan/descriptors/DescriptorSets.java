package org.artifex.vulkan.descriptors;

import org.artifex.vulkan.Device;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkDescriptorSetAllocateInfo;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO;
import static org.lwjgl.vulkan.VK10.vkAllocateDescriptorSets;

public class DescriptorSets
{

    public DescriptorSets(List<DescriptorSet> descriptorSets,DescriptorCopyWrite copyWrite){
        Objects.requireNonNull(descriptorSets,"Descriptor sets cannot be null!");
        if(descriptorSets.isEmpty()) throw new IllegalArgumentException("Descriptor sets cannot be empty!");
        this.descriptorSets=descriptorSets;
        this.copyWrite=copyWrite;

        this.sets = new ArrayList<>(descriptorSets.size());
        this.device = descriptorSets.get(0).getDevice();
    }

    public void allocate(){
        if(allocated) return;
        try(MemoryStack stack=MemoryStack.stackPush()){
            VkDescriptorSetAllocateInfo.Buffer allocInfos = VkDescriptorSetAllocateInfo.callocStack(descriptorSets.size(),stack);
            int i[]={0};
            LongBuffer pAlloc = stack.mallocLong(1);
            allocInfos.forEach((a)->{
                int i0=i[0];
                LongBuffer layouts = stack.longs(descriptorSets.get(i0).getLayout());
                a.pSetLayouts(layouts)
                        .descriptorPool(descriptorSets.get(i0).getPool())
                        .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO);
                vkAllocateDescriptorSets(device.getDevice(),a,pAlloc);
                if(pAlloc.get(0)==0L) return;
                sets.add(pAlloc.get(0));
            });
            allocated=true;
        }
    }


    private final DescriptorCopyWrite copyWrite;
    private final List<DescriptorSet> descriptorSets;
    private boolean allocated = false;
    private final List<Long> sets;
    private final Device device;
}
