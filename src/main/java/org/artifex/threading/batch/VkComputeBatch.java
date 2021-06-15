package org.artifex.threading.batch;

import org.artifex.vulkan.Device;
import org.artifex.vulkan.ShaderModule;
import org.artifex.vulkan.compute.ComputePipeline;
import org.artifex.vulkan.descriptors.DescriptorSet;
import org.artifex.vulkan.queues.Queue;
import org.artifex.vulkan.queues.QueueFamily;
import org.artifex.vulkan.queues.QueueType;

public abstract class VkComputeBatch extends VkBatch
{
    public VkComputeBatch(Device device,boolean runOnce) {
        super(device);
        for (QueueFamily queueFamily : device.getPhysicalDevice().getQueueFamilies()) {
            if(queueFamily.remaining()>0) this.queue = new Queue(device,queueFamily, QueueType.COMPUTE);
        }
        createPrimaryCommandBuffer(runOnce);
    }


    protected final ComputePipeline newComputePipeline(DescriptorSet set, ShaderModule module){
        return new ComputePipeline(device,set,module);
    }





}
