package org.artifex.util;

import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.KHRSwapchain;

import java.util.List;
import java.util.Vector;

import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;

public class VkExtensions
{
    public static final String DEVICE_SWAPCHAIN_KHR = KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME;
    static final List<String> inst_extensions = new Vector<>();
    static final List<String> device_extensions = new Vector<>();
    static{
        PointerBuffer b = glfwGetRequiredInstanceExtensions();
        if(b!=null)
            inst_extensions.addAll(Pointers.derefString(b));
    }

    public static PointerBuffer getInstanceExt(){

//        PointerBuffer reqext = glfwGetRequiredInstanceExtensions();
//        if(reqext==null) return util.Pointers.pString(inst_extensions);
//        PointerBuffer p = stack.mallocPointer(inst_extensions.size());
//        p.put(reqext).flip();
        System.out.println(inst_extensions);
        return Pointers.pString(inst_extensions);
    }

    public static PointerBuffer getDeviceExt(){

        return  Pointers.pString(device_extensions);
    }

    public static boolean hasInstanceExt(){
        return !inst_extensions.isEmpty();
    }

    public static boolean hasDeviceExt(){
        return !device_extensions.isEmpty();
    }
    public static void addInstanceExtension(String extension){
        if(inst_extensions.contains(extension))return;
        inst_extensions.add(extension);
    }

    public static void removeInstanceExtension(String extension){
        inst_extensions.remove(extension);
    }

    public static void addDeviceExtension(String extension){
        if(device_extensions.contains(extension))return;
        device_extensions.add(extension);
    }

    public static void removeDeviceExtension(String extension){
        device_extensions.remove(extension);
    }

}
