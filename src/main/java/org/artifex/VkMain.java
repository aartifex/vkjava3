package org.artifex;

import org.artifex.threading.VkThreadPool;
import org.artifex.util.VkExtensions;
import org.artifex.vulkan.Instance;
import org.lwjgl.vulkan.EXTDebugUtils;
import org.lwjgl.vulkan.KHRSwapchain;

public class VkMain
{


    public static void main(String[] args) {
        new VkMain();

        VkThreadPool pool = new VkThreadPool("hello");

    }


    public VkMain(){
        VkExtensions.addInstanceExtension(EXTDebugUtils.VK_EXT_DEBUG_UTILS_EXTENSION_NAME);
//        VkExtensions.addDeviceExtension(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME);
        new Program(null);
    }
}
