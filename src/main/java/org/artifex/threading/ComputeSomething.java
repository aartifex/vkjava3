package org.artifex.threading;

import org.artifex.threading.batch.VkComputeBatch;
import org.artifex.vulkan.CommandBuffer;
import org.artifex.vulkan.Device;

public class ComputeSomething extends VkComputeBatch {
    public ComputeSomething(Device device) {
        super(device);

    }

    @Override
    protected void execute() {

    }


    protected CommandBuffer secondBuffer;

}
