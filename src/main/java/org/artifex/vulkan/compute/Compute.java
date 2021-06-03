package org.artifex.vulkan.compute;

import org.artifex.vulkan.ShaderModule;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import static org.lwjgl.vulkan.VK10.*;

public class Compute
{


    public Compute(ShaderModule shaderModule){
        try(MemoryStack stack =MemoryStack.stackPush()){

            VkDescriptorSetLayoutBinding.Buffer binding = VkDescriptorSetLayoutBinding.callocStack(1,stack)
                    .binding(0)
                    .descriptorCount(1)
                    .descriptorType(VK_DESCRIPTOR_TYPE_STORAGE_BUFFER)
                    .stageFlags(VK_SHADER_STAGE_COMPUTE_BIT);

            VkDescriptorSetLayoutCreateInfo createInfo = VkDescriptorSetLayoutCreateInfo.callocStack(stack)
                    .pBindings(binding)
                    .sType(VK_STRUCTURE_TYPE_DESCRIPTOR_SET_LAYOUT_CREATE_INFO)
                    .flags(0);

            VkPipelineShaderStageCreateInfo stageCreateInfo = VkPipelineShaderStageCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO);
//            VkSpecializationInfo specializationInfo = VkSpecializationInfo.callocStack(stack);

            stageCreateInfo.module(shaderModule.getHandle())
                           .stage(shaderModule.getShaderStage())
                           .pName(stack.UTF8("main"));

//            VkSpecializationMapEntry.Buffer mapEntries = VkSpecializationMapEntry.callocStack(1,stack)
//                    .size(Integer.BYTES*4)
//                    .offset(0);
        }
    }
}
