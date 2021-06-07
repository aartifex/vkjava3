package org.artifex.vulkan;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.artifex.util.Pointers;
import org.artifex.util.VkExtensions;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class Device
{


    public Device(PhysicalDevice physicalDevice){
        this.physicalDevice=physicalDevice;
        LOGGER.debug("Creating logical device");
        try(MemoryStack stack=MemoryStack.stackPush()){
            //initialization
            PointerBuffer pDevice = stack.mallocPointer(1);
            PointerBuffer ppDeviceExtensions = getDeviceExtensions(stack,
                    List.of(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME));
            VkPhysicalDeviceFeatures features = VkPhysicalDeviceFeatures.callocStack(stack);
            VkQueueFamilyProperties.Buffer queueProps = physicalDevice.getpQueueFamilyProps();
            VkDeviceQueueCreateInfo.Buffer queueCreateInfos = VkDeviceQueueCreateInfo.callocStack(queueProps.capacity(),stack);


            int i=0;
            FloatBuffer prios = stack.floats(1.0f);

            for (VkDeviceQueueCreateInfo queueCreateInfo : queueCreateInfos) {
                queueCreateInfo.sType(VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO)
                               .queueFamilyIndex(i++)
                               .pQueuePriorities(prios);
            }

            VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO)
                    .ppEnabledExtensionNames(VkExtensions.getDeviceExt())
                    .pEnabledFeatures(features)
                    .pQueueCreateInfos(queueCreateInfos);


            vkCheck(vkCreateDevice(physicalDevice.getDevice(),createInfo,null,pDevice),
                    "Could not create logical device!");

            device = new VkDevice(pDevice.get(0),physicalDevice.getDevice(),createInfo);
            LOGGER.debug("Done with logical device");
        }
    }

    private PointerBuffer getDeviceExtensions(MemoryStack stack, List<String> requested){
        List<String> acquired = new ArrayList<>();
        physicalDevice.getpExtensionProperties().forEach(
                (prop)->{
                    if(requested.contains(prop.extensionNameString()))
                    acquired.add(prop.extensionNameString());
                });


        return Pointers.pString(acquired);
    }

    private PointerBuffer getDeviceLayers(MemoryStack stack){


        return null;
    }

    public VkDevice getDevice() {
        return device;
    }
    public void cleanup(){
        LOGGER.debug("Cleaning up logical device");
        vkDestroyDevice(device,null);
    }
    public void waitIdle(){
        vkDeviceWaitIdle(device);
    }
    public PhysicalDevice getPhysicalDevice() {
        return physicalDevice;
    }

    private static final Logger LOGGER = LogManager.getLogger(Device.class);
    private final PhysicalDevice physicalDevice;
    private final VkDevice device;
}
