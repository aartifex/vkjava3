package org.artifex.vulkan;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;

import java.nio.LongBuffer;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class CommandPool
{


    public CommandPool(Device device, int queueFamilyIndex){
        LOGGER.debug("Creating Vulkan CommandPool");
        this.device=device;
        try(MemoryStack stack = MemoryStack.stackPush()){
            VkCommandPoolCreateInfo createInfo = VkCommandPoolCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_COMMAND_POOL_CREATE_INFO)
                    .flags(VK_COMMAND_POOL_CREATE_RESET_COMMAND_BUFFER_BIT)
                    .queueFamilyIndex(queueFamilyIndex);
            LongBuffer pCommandPool = stack.longs(VK_NULL_HANDLE);
            vkCheck(vkCreateCommandPool(device.getDevice(),createInfo,null,pCommandPool),
                    "Could not create CommandPool!");
            this.commandPool=pCommandPool.get(0);
        }
    }

    public void cleanup(){
        vkDestroyCommandPool(device.getDevice(),commandPool,null);
    }

    public Device getDevice() {
        return device;
    }


    public long getCommandPool() {
        return commandPool;
    }

    private final Device device;
    private final long commandPool;
    private static final Logger LOGGER = LogManager.getLogger(CommandPool.class);
}
