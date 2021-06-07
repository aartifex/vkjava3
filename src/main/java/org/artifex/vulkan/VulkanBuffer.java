package org.artifex.vulkan;

import org.artifex.util.VulkanUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkBufferCreateInfo;
import org.lwjgl.vulkan.VkMemoryAllocateInfo;
import org.lwjgl.vulkan.VkMemoryRequirements;

import java.nio.LongBuffer;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.vulkan.VK10.*;

/**
 * <s>Buffers for use by the GPU to draw 3D objects (vertices, texture coords, indices, etc)</s>
 * A buffer in vulkan can be used for whatever we desire.
 */
public class VulkanBuffer
{
    public VulkanBuffer(Device device, long size, int usage, int reqMask){
        this.device = device;
        requestedSize = size;
        mappedMemory = NULL;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            VkBufferCreateInfo bufferCreateInfo = VkBufferCreateInfo.callocStack(stack)
                                                                    .sType(VK_STRUCTURE_TYPE_BUFFER_CREATE_INFO)
                                                                    .size(size)
                                                                    .usage(usage)
                                                                    .sharingMode(VK_SHARING_MODE_EXCLUSIVE);
            LongBuffer lp = stack.mallocLong(1);
            vkCheck(vkCreateBuffer(device.getDevice(), bufferCreateInfo, null, lp), "Failed to create buffer");
            buffer = lp.get(0);

            VkMemoryRequirements memReqs = VkMemoryRequirements.mallocStack(stack);
            vkGetBufferMemoryRequirements(device.getDevice(), buffer, memReqs);

            VkMemoryAllocateInfo memAlloc = VkMemoryAllocateInfo.callocStack(stack)
                                                                .sType(VK_STRUCTURE_TYPE_MEMORY_ALLOCATE_INFO)
                                                                .allocationSize(memReqs.size())
                                                                .memoryTypeIndex(VulkanUtils.memoryTypeFromProperties(device.getPhysicalDevice(),
                                                                        memReqs.memoryTypeBits(), reqMask));

            vkCheck(vkAllocateMemory(device.getDevice(), memAlloc, null, lp), "Failed to allocate memory");
            allocationSize = memAlloc.allocationSize();
            memory = lp.get(0);
            pMemory= PointerBuffer.allocateDirect(1);

            vkCheck(vkBindBufferMemory(device.getDevice(), buffer, memory, 0), "Failed to bind buffer memory");
        }

    }

    public long map(){
        if(mappedMemory==MemoryUtil.NULL){
            vkCheck(vkMapMemory(device.getDevice(),memory,0,allocationSize,0,pMemory),"Failed to map buffer!");
            mappedMemory=pMemory.get(0);
        }
        return mappedMemory;
    }

    public  void  unmap(){
        if(mappedMemory!=MemoryUtil.NULL){
            vkUnmapMemory(device.getDevice(),memory);
            mappedMemory = MemoryUtil.NULL;
        }
    }

    public void cleanup(){
        vkDestroyBuffer(device.getDevice(),buffer,null);
        vkFreeMemory(device.getDevice(),memory,null);
    }

    public long getBuffer() {
        return buffer;
    }

    public Device getDevice() {
        return device;
    }

    public long getMappedMemory() {
        return mappedMemory;
    }

    public long getRequestedSize() {
        return requestedSize;
    }

    public long getAllocationSize() {
        return allocationSize;
    }

    public long getMemory() {
        return memory;
    }

    private final long buffer;
    private final long allocationSize;
    private final long requestedSize;
    private final long memory;
    private final PointerBuffer pMemory;
    private long mappedMemory;
    private final Device device;
}
