package org.artifex.util;

import org.lwjgl.vulkan.VkMemoryType;
import vulkan.PhysicalDevice;

public class VulkanUtils
{



    public static int memoryTypeFromProperties(PhysicalDevice device,int typeBits,int reqsMask){

        int result = -1;
        VkMemoryType.Buffer memoryTypes = device.getMemoryProperties().memoryTypes();//memoryProperties.memoryTypes();
        int count = device.getMemoryProperties().memoryTypeCount();
        for (int i = 0; i < (long)count; i++) {
            System.out.println(i);
            if ((typeBits & (1 << i)) != 1 && (memoryTypes.get(i).propertyFlags() & reqsMask) == reqsMask) {
                result = i;
                break;
            }
        }
        if (result < 0) {
            throw new RuntimeException("Failed to find memoryType");
        }
        return result;
    }
}
