package org.artifex.threading.util;

import org.artifex.vulkan.CommandBuffer;
import org.artifex.vulkan.Pipeline;

import static org.lwjgl.vulkan.VK10.vkCmdBindPipeline;

public class VkCommands
{


    public static void bindPipeline(CommandBuffer cb, Pipeline pipeline){
        vkCmdBindPipeline(cb.getCommandBuffer(),pipeline.getPipelineBindPoint(),pipeline.getPipeline());
    }


}
