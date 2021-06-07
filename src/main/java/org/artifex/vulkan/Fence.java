package org.artifex.vulkan;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkFenceCreateInfo;

import java.nio.LongBuffer;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class Fence
{

    public Fence(Device device, boolean signaled){

        this.device=device;
        try(MemoryStack stack = MemoryStack.stackPush()){
            VkFenceCreateInfo createInfo = VkFenceCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_FENCE_CREATE_INFO)
                    .flags(signaled ? VK_FENCE_CREATE_SIGNALED_BIT : 0);

            LongBuffer pFence = stack.mallocLong(1);
            vkCheck(vkCreateFence(device.getDevice(),createInfo,null,pFence),
                    "Could not create fence!");
            fence=pFence.get(0);
        }
    }

    public void cleanup(){
        vkDestroyFence(device.getDevice(),fence,null);
    }

    public void fenceWait(){
        vkWaitForFences(device.getDevice(),fence,true, Long.MAX_VALUE);
    }

    public void reset(){
        vkResetFences(device.getDevice(),fence);
    }

    public long getFence() {
        return fence;
    }

    public Device getDevice() {
        return device;
    }

    private final Device device;
    private final long fence;
}
