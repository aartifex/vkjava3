package org.artifex;

import org.artifex.props.AppProperties;
import org.artifex.util.SPIRV;
import org.artifex.vulkan.Device;
import org.artifex.vulkan.Instance;
import org.artifex.vulkan.PhysicalDevice;
import org.artifex.vulkan.ShaderModule;

import static org.lwjgl.vulkan.VK10.VK_SHADER_STAGE_COMPUTE_BIT;
import static org.lwjgl.vulkan.VK10.VK_SHADER_STAGE_VERTEX_BIT;

public class Program
{

    public Program(Window window){
        AppProperties properties = AppProperties.getInstance();

        w = new Window("Title");

        instance = new Instance(properties.isValidate());
        physicalDevice = PhysicalDevice.createPhysicalDevice(instance,null);
        device = new Device(physicalDevice);






        while(w.alive()){
            w.pollEvents();
        }


    }


    private final Instance instance;
    private final PhysicalDevice physicalDevice;
    private final Device device;
    private final Window w;
}
