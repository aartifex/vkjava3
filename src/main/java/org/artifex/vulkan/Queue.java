package org.artifex.vulkan;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.artifex.threading.synch.Fence;
import org.artifex.vulkan.queues.QueueFamily;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkQueue;
import org.lwjgl.vulkan.VkQueueFamilyProperties;
import org.lwjgl.vulkan.VkSubmitInfo;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.List;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class Queue
{

    public Queue(Device device, int queueFamilyIndex, int queueIndex){
        LOGGER.debug("Creating queue  " + queueFamilyIndex + "," + queueIndex);
        this.queueFamilyIndex =queueFamilyIndex;
        try(MemoryStack stack = MemoryStack.stackPush()){
            PointerBuffer pQueue = stack.mallocPointer(1);
            vkGetDeviceQueue(device.getDevice(),queueFamilyIndex,queueIndex,pQueue);
            long queue = pQueue.get(0);
            this.queue = new VkQueue(queue,device.getDevice());
        }

    }

    public void submit(PointerBuffer commandBuffers, LongBuffer waitSemaphores, IntBuffer dstStageMasks,
                       LongBuffer signalSemaphores,Fence fence){

        try(MemoryStack stack =MemoryStack.stackPush()){
            VkSubmitInfo submitInfo =VkSubmitInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_SUBMIT_INFO)
                    .pCommandBuffers(commandBuffers)
                    .pSignalSemaphores(signalSemaphores);
            if(waitSemaphores!=null){
                submitInfo.waitSemaphoreCount(waitSemaphores.capacity())
                        .pWaitSemaphores(waitSemaphores)
                        .pWaitDstStageMask(dstStageMasks);
            }else submitInfo.waitSemaphoreCount(0);
            long fenceHandle =  ( fence != null) ? fence.getFence() : VK_NULL_HANDLE;

            vkCheck(vkQueueSubmit(queue,submitInfo,fenceHandle),
                    "Could not submit to queue!");
        }

    }

    public VkQueue getQueue() {
        return queue;
    }

    public void waitIdle(){
        vkQueueWaitIdle(queue);
    }


    public int getQueueFamilyIndex() {
        return queueFamilyIndex;
    }

    private final VkQueue queue;
    private final int queueFamilyIndex;
    private static final Logger LOGGER = LogManager.getLogger(Queue.class);



    public static class GraphicsQueue extends Queue
    {
        public GraphicsQueue(Device device, int queueIndex) {
            super(device, getGraphicsQueueFamilyIndex(device), queueIndex);
        }

        private static int getGraphicsQueueFamilyIndex(Device device){
            int index = -1;
            PhysicalDevice physicalDevice = device.getPhysicalDevice();
            List<QueueFamily> queueFamilies = device.getPhysicalDevice().getQueueFamilies();
            int numQueuefamilies = queueFamilies.size();
            for (int i = 0; i <numQueuefamilies ; i++) {
                QueueFamily props = queueFamilies.get(i);
                if(props.supportsGraphics()){
                    index=i;break;}
            }
            if(index<0) throw  new RuntimeException("Failed to get graphics queue");

            return index;
        }
    }

//    public static class PresentQueue extends Queue{
//        public PresentQueue(Device device,Surface surface,int queueIndex){
//            super(device,getPresentQueueFamilyIndex(device,surface),queueIndex);
//        }
//
//        private static int getPresentQueueFamilyIndex(Device device, Surface surface){
//            int index = -1;
//            try(MemoryStack stack= MemoryStack.stackPush()){
//                PhysicalDevice physicalDevice= device.getPhysicalDevice();
//                VkQueueFamilyProperties.Buffer queuePropsBuff = physicalDevice.getpQueueFamilyProps();
//                int numQueueFamilies = queuePropsBuff.capacity();
//                IntBuffer n = stack.mallocInt(1);
//                for (int i = 0; i < numQueueFamilies; i++) {
//                    KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(physicalDevice.getDevice(),i,surface.getSurface(),n);
//                    boolean supportsPresentation = n.get(0) == VK_TRUE;
//                    if(supportsPresentation){
//                        index=i;
//                        break;
//                    }
//                }
//                return index;
//            }
//        }
//    }

    public static class ComputeQueue extends Queue
    {
        public ComputeQueue(Device device, int queueIndex) {
            super(device, getComputeQueueFamilyIndex(device), queueIndex);
        }

        private static int getComputeQueueFamilyIndex(Device device){
            int index = -1;
            List<QueueFamily> queueFamilies = device.getPhysicalDevice().getQueueFamilies();
            int numQueuefamilies = queueFamilies.size();
            for (int i = 0; i <numQueuefamilies ; i++) {
                QueueFamily props = queueFamilies.get(i);
                if(props.supportsCompute() && props.remaining()>0){
                    index=i;break;}
            }
            if(index<0) throw  new RuntimeException("Failed to get graphics queue");

            return index;
        }
    }
}
