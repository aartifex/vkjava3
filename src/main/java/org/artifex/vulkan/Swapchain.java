package org.artifex.vulkan;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkSwapchainCreateInfoKHR;

import static org.lwjgl.vulkan.KHRSwapchain.VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR;

public class Swapchain
{

    public Swapchain(Device device){
        try(MemoryStack stack = MemoryStack.stackPush()){

            VkSwapchainCreateInfoKHR swapchainCreateInfoKHR = VkSwapchainCreateInfoKHR.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_SWAPCHAIN_CREATE_INFO_KHR);


        }
    }
}
