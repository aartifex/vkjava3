package org.artifex.vulkan.queues;

import org.artifex.vulkan.Device;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkQueue;

import static org.lwjgl.vulkan.VK10.vkGetDeviceQueue;

public class Queue
{

    public Queue(Device device, QueueFamily queueFamily,QueueType queueType){

        boolean cancreate = false;
        switch (queueType) {
            case TRANSFER:
                cancreate = queueFamily.supportsTransfer();
                break;
            case COMPUTE:
                cancreate = queueFamily.supportsCompute();
                break;
            case GRAPHICS:
                cancreate = queueFamily.supportsGraphics();
                break;
            case SPARSE_BINDING:
                cancreate = queueFamily.supportsSparseBinding();
                break;
        }
        if(!cancreate) throw new RuntimeException("Could not create "+queueType + " queue in queue family " + queueFamily.getFamilyIndex());
        queueInfo = queueFamily.getFirstAvailableQueue();
        this.queueFamily=queueFamily;
        try(MemoryStack stack = MemoryStack.stackPush()){
            PointerBuffer pQueue = stack.mallocPointer(1);
            vkGetDeviceQueue(device.getDevice(),queueFamily.getFamilyIndex(),queueInfo.getIndex(),pQueue);
            long queue = pQueue.get(0);
            this.queue = new VkQueue(queue,device.getDevice());
        }

    }


    public int getFamilyIndex() {
        return queueFamily.getFamilyIndex();
    }

    public int getIndex(){
        return queueInfo.getIndex();
    }

    public void cleanup(){
        queueInfo.free();
    }


    private final VkQueue queue;
    private final QueueInfo queueInfo;
    private final QueueFamily queueFamily;
}
