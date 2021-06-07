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


    public Compute(Device device,DescriptorSet set,ShaderModule shaderModule){
        this.computeShader=shaderModule;
        this.device=device;

        try(MemoryStack stack =MemoryStack.stackPush()){


            VkPipelineShaderStageCreateInfo stageCreateInfo = VkPipelineShaderStageCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO);

            stageCreateInfo.module(shaderModule.getHandle())
                           .stage(shaderModule.getShaderStage())
                           .pName(stack.UTF8("main"));

            VkPipelineLayoutCreateInfo layoutCreateInfo = VkPipelineLayoutCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_PIPELINE_LAYOUT_CREATE_INFO)
                    .pSetLayouts(stack.longs(set.getDescriptorLayout()))
                    .flags(0);

            LongBuffer pLayout = stack.mallocLong(1);
            DebugUtil.vkCheck(
                    vkCreatePipelineLayout(device.getDevice(),layoutCreateInfo,null,pLayout),
                    "Could not create pipeline layout"
            );

            this.pipelineLayout=pLayout.get(0);


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

    public long getComputePipeline() {
        return computePipeline;
    }

    public long getPipelineLayout() {
        return pipelineLayout;
    }

    private long computePipeline;
    private long pipelineLayout;
    private ShaderModule computeShader;
    private Device device;
}
