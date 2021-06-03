package org.artifex;

import org.artifex.vulkan.Instance;

public class VkMain
{


    public static void main(String[] args) {
        new VkMain();
    }


    public VkMain(){
        new Instance(true);

    }
}
