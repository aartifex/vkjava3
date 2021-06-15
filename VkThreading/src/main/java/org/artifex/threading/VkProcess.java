package org.artifex.threading;

import org.artifex.Vulkan;

public abstract class VkProcess implements Runnable {

    public VkProcess(Vulkan vulkan){
        this.vulkan=vulkan;
        initialize();
    }

    protected abstract void initialize();



    protected  final Vulkan vulkan;

}
