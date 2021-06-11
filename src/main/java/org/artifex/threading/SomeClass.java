package org.artifex.threading;

import org.artifex.threading.synch.*;
import org.artifex.vulkan.CommandBuffer;
import org.artifex.vulkan.Device;

public class SomeClass
{


    public SomeClass(Device device, CommandBuffer buffer,VkEventCreateFlagBits createFlagBits){
        this.device=device;
        this.event=new Event(device,createFlagBits);
    }

    public void setMemoryBarrier(MemoryBarrier memoryBarrier) {
        this.memoryBarrier = memoryBarrier;
    }

    public void setBufferBarrier(BufferBarrier bufferBarrier) {
        this.bufferBarrier = bufferBarrier;
    }

    public void setImageBarrier(ImageBarrier imageBarrier) {
        this.imageBarrier = imageBarrier;
    }

    public void startEvent(){
//        this.event.setEvent();
    }

    private final Device device;
    private final Event event;
    private MemoryBarrier memoryBarrier = null;
    private BufferBarrier bufferBarrier = null;
    private ImageBarrier imageBarrier = null;
    private Fence fence = null;
}
