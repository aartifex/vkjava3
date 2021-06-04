package org.artifex.vulkan.compute;

import org.artifex.util.DebugUtil;
import org.artifex.vulkan.Device;
import org.artifex.vulkan.ShaderModule;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class Compute
{


    public Compute(Device device,ShaderModule shaderModule){
        this.computeShader=shaderModule;
        this.device=device;

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


            LongBuffer pSetLayout = stack.mallocLong(1);
            DebugUtil.vkCheck(vkCreateDescriptorSetLayout( device.getDevice(),createInfo,null,pSetLayout),
                    "Could not create set layout!");

            VkPipelineShaderStageCreateInfo stageCreateInfo = VkPipelineShaderStageCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO);

            stageCreateInfo.module(shaderModule.getHandle())
                           .stage(shaderModule.getShaderStage())
                           .pName(stack.UTF8("main"));

            VkPipelineLayoutCreateInfo layoutCreateInfo = VkPipelineLayoutCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO)
                    .pSetLayouts(pSetLayout)
                    .flags(0);

            LongBuffer pLayout = stack.mallocLong(1);
            DebugUtil.vkCheck(
                    vkCreatePipelineLayout(device.getDevice(),layoutCreateInfo,null,pLayout),
                    "Could not create pipeline layout"
            );



            VkComputePipelineCreateInfo.Buffer pipelineCreateInfo = VkComputePipelineCreateInfo.callocStack(1,stack);
            pipelineCreateInfo.get(0)
                    .flags(0)
                    .layout(pLayout.get(0))
                    .stage(stageCreateInfo)
                    .sType(VK_STRUCTURE_TYPE_COMPUTE_PIPELINE_CREATE_INFO);


            LongBuffer pCompute = stack.mallocLong(1);
            DebugUtil.vkCheck(
                    vkCreateComputePipelines(device.getDevice(),0L,pipelineCreateInfo,null,pCompute),
                    "Could not create compute shader"
            );

            computePipeline=pCompute.get(0);
        }
    }

    private long computePipeline;
    private ShaderModule computeShader;
    private Device device;
}
