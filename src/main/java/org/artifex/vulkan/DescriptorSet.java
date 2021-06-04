package org.artifex.vulkan;

import org.artifex.util.DebugUtil;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class DescriptorSet
{

    public DescriptorSet(Device device){

        try(MemoryStack stack = MemoryStack.stackPush()){

            VkDescriptorSetLayoutBinding.Buffer layoutBinding = VkDescriptorSetLayoutBinding.callocStack(1,stack);
            layoutBinding.get(0)
                    .stageFlags(VK_SHADER_STAGE_COMPUTE_BIT)
                    .binding(0)
                    .descriptorCount(1)
                    .descriptorType(VK_DESCRIPTOR_TYPE_STORAGE_BUFFER);
            VkDescriptorSetLayoutCreateInfo layoutCreateInfo = VkDescriptorSetLayoutCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO)
                    .pBindings(layoutBinding)
                    .flags(0);

            LongBuffer pLayout = stack.mallocLong(1);

            DebugUtil.vkCheck(
                    vkCreateDescriptorSetLayout(device.getDevice(),layoutCreateInfo,null,pLayout),
                    "Could not create DescriptorSet layout"
            );

            VkDescriptorPoolSize.Buffer poolSize = VkDescriptorPoolSize.callocStack(1,stack);
            poolSize.get(0)
                    .type(VK_DESCRIPTOR_TYPE_STORAGE_BUFFER)
                    .descriptorCount(1);

            VkDescriptorPoolCreateInfo poolInfo = VkDescriptorPoolCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_POOL_CREATE_INFO)
                    .pPoolSizes(poolSize)
                    .maxSets(1)
                    .flags(0);

            LongBuffer pPool = stack.mallocLong(1);

            DebugUtil.vkCheck(
                    vkCreateDescriptorPool(device.getDevice(),poolInfo,null,pPool),
                    "Could not create descriptor pool"
            );


            VulkanBuffer buffer = new VulkanBuffer(device,Integer.BYTES*2,
                    VK_BUFFER_USAGE_STORAGE_BUFFER_BIT,
                    VK_MEMORY_PROPERTY_HOST_VISIBLE_BIT | VK_MEMORY_PROPERTY_HOST_COHERENT_BIT);

            VkDescriptorSetAllocateInfo allocInfo = VkDescriptorSetAllocateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_ALLOCATE_INFO)
                    .descriptorPool(pPool.get(0))
                    .pSetLayouts(pLayout);

            LongBuffer pAlloc = stack.mallocLong(1);
            DebugUtil.vkCheck(
                    vkAllocateDescriptorSets(device.getDevice(),allocInfo,pAlloc),
                    "Could not allocate descriptor set memory!"
            );

            VkDescriptorBufferInfo.Buffer bufferInfo  = VkDescriptorBufferInfo.callocStack(1,stack);
            bufferInfo.get(0)
                    .buffer(buffer.getBuffer())
                    .offset(0)
                    .range(Integer.BYTES*2);


            VkWriteDescriptorSet.Buffer write = VkWriteDescriptorSet.callocStack(1,stack);
            write.get(0).sType(VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET)
                    .descriptorCount(1)
                    .descriptorType(VK_DESCRIPTOR_TYPE_STORAGE_BUFFER)
                    .dstBinding(0)
                    .dstSet(pAlloc.get(0))
                    .pBufferInfo(bufferInfo);
            vkUpdateDescriptorSets(device.getDevice(),write,null);
        }
    }
}
