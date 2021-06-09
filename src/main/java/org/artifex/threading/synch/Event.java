package org.artifex.threading.synch;

import org.artifex.threading.VkEventCreateFlagBits;
import org.artifex.util.DebugUtil;
import org.artifex.vulkan.Device;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkEventCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_EVENT_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateEvent;
import static org.lwjgl.vulkan.VK10.vkDestroyEvent;

/**
 * Used to synchronize work within a command buffer or sequence of command buffers submitted to one queue.
 */
public class Event
{



    public Event(Device device, VkEventCreateFlagBits flags){
        this.device=device;
        try(MemoryStack stack = MemoryStack.stackPush()){
            VkEventCreateInfo eventCreateInfo = VkEventCreateInfo.callocStack(stack)
                    .flags(flags.get())
                    .sType(VK_STRUCTURE_TYPE_EVENT_CREATE_INFO);


            LongBuffer pEvent = stack.mallocLong(1);
            DebugUtil.vkCheck(vkCreateEvent(device.getDevice(),eventCreateInfo,null,pEvent)
                    ,"Could not create event!"
            );
        }


    }


    public void cleanup(){
        vkDestroyEvent(device.getDevice(),event,null);
    }
    private final Device device;
    private long event;
}
