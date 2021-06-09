package org.artifex.threading.synch;

import org.artifex.vulkan.Device;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;

import java.nio.LongBuffer;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class Semaphore
{


    public Semaphore(Device device){
        this.device=device;
        try(MemoryStack stack=MemoryStack.stackPush()){
            VkSemaphoreCreateInfo createInfo = VkSemaphoreCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_SEMAPHORE_CREATE_INFO);

            LongBuffer pSemaphore = stack.mallocLong(1);
            vkCheck(vkCreateSemaphore(device.getDevice(),createInfo,null,pSemaphore),
                    "Could not create semaphore!");
            this.semaphore=pSemaphore.get(0);
        }
    }

    public void cleanup(){
        vkDestroySemaphore(device.getDevice(),semaphore,null);
    }

    public long getSemaphore() {
        return semaphore;
    }

    public Device getDevice() {
        return device;
    }

    private final Device device;
    private final long semaphore;



}
