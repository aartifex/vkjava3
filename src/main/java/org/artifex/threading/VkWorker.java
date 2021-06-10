package org.artifex.threading;

import org.artifex.vulkan.CommandPool;
import org.artifex.vulkan.Device;
import org.artifex.vulkan.Queue;

public abstract class VkWorker implements Runnable
{


    public VkWorker(Device device, Queue queue){
        commandPool = new CommandPool(device,queue.getQueueFamilyIndex());
        this.device=device;
        this.queue=queue;
    }


    public abstract void recordCommands();
    public abstract void submitWork();

    public Device getDevice() {
        return device;
    }

    public CommandPool getCommandPool() {
        return commandPool;
    }

    public Queue getQueue() {
        return queue;
    }


    private final Device device;
    private final Queue queue;
    private CommandPool commandPool;
}
