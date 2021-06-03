package org.artifex;

import org.artifex.util.VkExtensions;
import org.artifex.vulkan.Instance;
import org.lwjgl.vulkan.EXTDebugUtils;

public class VkMain
{


    public static void main(String[] args) {
        new VkMain();
    }


    public VkMain(){
        VkExtensions.addInstanceExtension(EXTDebugUtils.VK_EXT_DEBUG_UTILS_EXTENSION_NAME);
        new Program(null);
    }
}
