package org.artifex;

import org.artifex.vulkan.Device;
import org.artifex.vulkan.Instance;
import org.artifex.vulkan.PhysicalDevice;

public class Vulkan
{

    public Vulkan(String preferredDevice){
        if(instance==null)instance = new Instance(false);
        physicalDevice = PhysicalDevice.createPhysicalDevice(instance,null);
        device = new Device(physicalDevice);
    }


    public Instance getInstance() {
        return instance;
    }

    public Device getDevice() {
        return device;
    }

    public PhysicalDevice getPhysicalDevice() {
        return physicalDevice;
    }

    private static Instance instance;
    private Device device;
    private PhysicalDevice physicalDevice;

}
