package org.artifex.threading.synch;

import org.artifex.threading.VkAccessFlagBits;
import org.lwjgl.vulkan.VkBufferMemoryBarrier;
import org.lwjgl.vulkan.VkImageMemoryBarrier;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_MEMORY_BARRIER;

public class ImageBarrier
{
    public ImageBarrier(VkAccessFlagBits srcMask, VkAccessFlagBits dstMask)
    {
        imageBarrier = VkImageMemoryBarrier.calloc()
                .dstAccessMask(dstMask.get())
                .srcAccessMask(srcMask.get())
                .sType(VK_STRUCTURE_TYPE_MEMORY_BARRIER);
    }

    public void cleanup(){
        imageBarrier.free();
    }
    public VkImageMemoryBarrier getImageBarrier() {
        return imageBarrier;
    }

    private final VkImageMemoryBarrier imageBarrier;
}
