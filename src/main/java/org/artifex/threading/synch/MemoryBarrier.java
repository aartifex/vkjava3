package org.artifex.threading.synch;

import org.artifex.threading.VkAccessFlagBits;
import org.lwjgl.vulkan.VkBufferMemoryBarrier;
import org.lwjgl.vulkan.VkMemoryBarrier;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_MEMORY_BARRIER;

public class MemoryBarrier
{


    public MemoryBarrier(VkAccessFlagBits srcMask,VkAccessFlagBits dstMask)
    {
        memoryBarrier = VkMemoryBarrier.calloc()
                .dstAccessMask(dstMask.get())
                .srcAccessMask(srcMask.get())
                .sType(VK_STRUCTURE_TYPE_MEMORY_BARRIER);

    }

    public void cleanup(){
        memoryBarrier.free();
    }
    public VkMemoryBarrier getMemoryBarrier() {
        return memoryBarrier;
    }

    private final VkMemoryBarrier memoryBarrier;
}
