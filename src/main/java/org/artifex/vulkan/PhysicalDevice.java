package org.artifex.vulkan;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.artifex.vulkan.queues.QueueFamily;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class PhysicalDevice
{

    public PhysicalDevice(VkPhysicalDevice device){
        this.device=device;
        try(MemoryStack stack = MemoryStack.stackPush()){
             features = VkPhysicalDeviceFeatures.calloc();
            properties = VkPhysicalDeviceProperties.calloc();

            vkGetPhysicalDeviceProperties(device,properties);
            vkGetPhysicalDeviceFeatures(device,features);


            //other properties

            IntBuffer count = stack.callocInt(1);
            IntBuffer count2 = stack.callocInt(1);

            vkGetPhysicalDeviceQueueFamilyProperties(device,count,null);
            VkQueueFamilyProperties.Buffer pQueueFamilyProps = VkQueueFamilyProperties.calloc(count.get(0));

            vkGetPhysicalDeviceQueueFamilyProperties(device,count,pQueueFamilyProps);
            queueFamilies = new ArrayList<>(pQueueFamilyProps.capacity());
            final int[] i={0};
            pQueueFamilyProps.forEach((p)->{
                queueFamilies.add(new QueueFamily(p,i[0]++));
            });


            vkCheck(vkEnumerateDeviceExtensionProperties(device,(String)null,count2,null),
                    "Failed to get device extensions");
            /*VkExtensionProperties.Buffer*/ pExtensionProperties = VkExtensionProperties.calloc(count2.get(0));
            vkCheck(vkEnumerateDeviceExtensionProperties(device,(String)null,count2,pExtensionProperties)
                    ,"Could not find device extensions");



            //memory properties
             memoryProperties = VkPhysicalDeviceMemoryProperties.calloc();
            vkGetPhysicalDeviceMemoryProperties(device,memoryProperties);
            System.out.println(memoryProperties);
            if(memoryProperties.memoryTypeCount()==0) throw new RuntimeException("No memory types!");
            if(memoryProperties.memoryTypes().capacity()<0)throw new RuntimeException("wtf");
        }

    }





    public static PhysicalDevice createPhysicalDevice(Instance instance, String preferredDeviceName){
        LOGGER.debug("Selecting physical devices");
        PhysicalDevice selected = null;
        try(MemoryStack stack = MemoryStack.stackPush()){

            PointerBuffer pDevices = getPhysicalDevices(instance,stack);
            if(numDevices==0) throw new RuntimeException("No physical devices found");
            List<PhysicalDevice> devices = new ArrayList<>(numDevices);
            for (int i = 0; i <numDevices ; i++) {
                VkPhysicalDevice vkPhys = new VkPhysicalDevice(pDevices.get(i),instance.getInstance());
                PhysicalDevice physicalDevice = new PhysicalDevice(vkPhys);
                System.out.println(physicalDevice.getDeviceName());

                String deviceName = physicalDevice.getDeviceName();
                if(physicalDevice.hasGraphicsQueueFamily() && physicalDevice.hasKHRSwapchainExtension()){
                    if(preferredDeviceName!=null && preferredDeviceName.equals(deviceName)){
                        selected=physicalDevice;
                        break;
                    }
                    devices.add(physicalDevice);
                }else{
                    LOGGER.debug("Device " + deviceName + " does not support required extensions");
                    physicalDevice.cleanup();
                }
                selected = (selected==null) && !devices.isEmpty() ? devices.remove(0) : selected;
                for (PhysicalDevice device : devices) {
                    device.cleanup();
                }
                if(selected==null)throw new RuntimeException("Could not find suitable device!");
                LOGGER.debug("Selected device : " +selected.getDeviceName());
            }

        }

        return selected;
    }

    public static PointerBuffer getPhysicalDevices(Instance instance, MemoryStack stack){

        IntBuffer count = stack.callocInt(1);
        vkEnumeratePhysicalDevices(instance.getInstance(),count,null);
        numDevices=count.get(0);

        PointerBuffer pPhysicalDevices = stack.callocPointer(numDevices);
        vkCheck(vkEnumeratePhysicalDevices(instance.getInstance(),count,pPhysicalDevices)
                ,"Could not enumerate physical devices");

        return pPhysicalDevices;
    }

    public String getDeviceName(){
        return properties.deviceNameString();
    }
    public boolean hasGraphicsQueueFamily(){
        for (QueueFamily queueFamily : queueFamilies) {
            if(queueFamily.supportsGraphics()) return true;
        }
        return false;
    }

    public boolean hasComputeQueueFamily(){
        for (QueueFamily queueFamily : queueFamilies) {
            if(queueFamily.supportsCompute()) return true;
        }
        return false;
    }

    public boolean hasKHRSwapchainExtension(){
        for (int i = 0; i <pExtensionProperties.capacity() ; i++) {
            if(pExtensionProperties.get(i).extensionNameString()
                                   .equals(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME))
                return true;
        }
        return false;
    }
    public void cleanup(){
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("Destroying physical device ");
        }
        for (QueueFamily queueFamily : queueFamilies) {
            queueFamily.cleanup();
        }
        pExtensionProperties.free();
        properties.free();
        features.free();
    }

    public VkPhysicalDeviceFeatures getFeatures() {
        return features;
    }
    public VkPhysicalDeviceProperties getProperties() {
        return properties;
    }

    public VkExtensionProperties.Buffer getpExtensionProperties() {
        return pExtensionProperties;
    }

//    public VkQueueFamilyProperties.Buffer getpQueueFamilyProps() {
//        return pQueueFamilyProps;
//    }

    public List<QueueFamily> getQueueFamilies() {
        return queueFamilies;
    }

    public VkPhysicalDevice getDevice() {
        return device;
    }

    public VkPhysicalDeviceMemoryProperties getMemoryProperties() {
        return memoryProperties;
    }
    public static int numDevices = 0;

    private VkPhysicalDevice device;
    private VkPhysicalDeviceFeatures features;
    private VkPhysicalDeviceProperties properties;
    private List<QueueFamily> queueFamilies;
    private VkExtensionProperties.Buffer pExtensionProperties;
    private VkPhysicalDeviceMemoryProperties memoryProperties;


    private static Logger LOGGER = LogManager.getLogger(PhysicalDevice.class);
}
