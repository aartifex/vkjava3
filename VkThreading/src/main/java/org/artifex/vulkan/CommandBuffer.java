package org.artifex.vulkan;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;
import org.lwjgl.vulkan.VkCommandBufferBeginInfo;
import org.lwjgl.vulkan.VkDevice;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class CommandBuffer
{

    public CommandBuffer(CommandPool commandPool, boolean primary, boolean oneTimeSubmit){
        LOGGER.trace("Creating command buffer");
        this.commandPool=commandPool;
        this.oneTimeSubmit=oneTimeSubmit;
        VkDevice device = commandPool.getDevice().getDevice();

        try(MemoryStack stack = MemoryStack.stackPush()){
            VkCommandBufferAllocateInfo allocateInfo = VkCommandBufferAllocateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_ALLOCATE_INFO)
                    .commandPool(commandPool.getCommandPool())
                    .level(primary ? VK_COMMAND_BUFFER_LEVEL_PRIMARY : VK_COMMAND_BUFFER_LEVEL_SECONDARY)
                    .commandBufferCount(1);
            PointerBuffer pCommandBuffer = stack.mallocPointer(1);
            vkCheck(vkAllocateCommandBuffers(device,allocateInfo,pCommandBuffer),
                    "Failed to allocate render command buffer");
            commandBuffer = new VkCommandBuffer(pCommandBuffer.get(0),device);
        }
    }


    public void beginRecording(){
        try(MemoryStack stack = MemoryStack.stackPush()){
            VkCommandBufferBeginInfo beginInfo = VkCommandBufferBeginInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_COMMAND_BUFFER_BEGIN_INFO);
            if(oneTimeSubmit){
                beginInfo.flags(VK_COMMAND_BUFFER_USAGE_ONE_TIME_SUBMIT_BIT);
            }
            vkCheck(vkBeginCommandBuffer(commandBuffer,beginInfo),
                    "Could not begin command buffer");
        }
    }

    public void cleanup(){
        LOGGER.trace("Freeing command buffer");
        vkFreeCommandBuffers(commandPool.getDevice().getDevice(),commandPool.getCommandPool(),commandBuffer);
    }

    public void endRecording(){
        LOGGER.trace("Ending command buffer recording");
        vkEndCommandBuffer(commandBuffer);
    }

    public void reset(){
        vkResetCommandBuffer(commandBuffer,VK_COMMAND_BUFFER_RESET_RELEASE_RESOURCES_BIT);
    }

    public CommandPool getCommandPool() {
        return commandPool;
    }

    public boolean isOneTimeSubmit() {
        return oneTimeSubmit;
    }

    public VkCommandBuffer getCommandBuffer() {
        return commandBuffer;
    }

    private final CommandPool commandPool;
    private final boolean oneTimeSubmit;
    private final VkCommandBuffer commandBuffer;
    private static final Logger LOGGER = LogManager.getLogger(CommandBuffer.class);
}
