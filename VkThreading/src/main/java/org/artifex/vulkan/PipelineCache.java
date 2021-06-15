package org.artifex.vulkan;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkPipelineCacheCreateInfo;

import java.nio.LongBuffer;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class PipelineCache
{


    public PipelineCache(Device device){
        LOGGER.debug("Creating pipeline cache");
        this.device=device;
        try(MemoryStack stack = MemoryStack.stackPush()){
            VkPipelineCacheCreateInfo createInfo = VkPipelineCacheCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_PIPELINE_CACHE_CREATE_INFO);
            LongBuffer pCache = stack.mallocLong(1);

            vkCheck(vkCreatePipelineCache(device.getDevice(),createInfo,null,pCache),
                    "Could not create pipeline cache");
            pipelineCache = pCache.get(0);
        }
    }

    public void cleanup(){
        LOGGER.debug("Cleaning pipeline cache");
        vkDestroyPipelineCache(device.getDevice(),pipelineCache,null);
    }

    public Device getDevice() {
        return device;
    }

    public long getPipelineCache() {
        return pipelineCache;
    }

    private final Device device;
    private final long pipelineCache;
    private static final Logger LOGGER = LogManager.getLogger(PipelineCache.class);
}
