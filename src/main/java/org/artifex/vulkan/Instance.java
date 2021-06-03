package org.artifex.vulkan;

import org.artifex.props.AppInfoProps;
import org.artifex.util.DebugUtil;
import org.artifex.util.VkExtensions;
import org.artifex.util.VkValidation;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.nio.LongBuffer;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.vulkan.VK10.*;

public class Instance
{

    public Instance(boolean validate){
        AppInfoProps appInfoProps = AppInfoProps.getInstance();
        try(MemoryStack stack = MemoryStack.stackPush()){
            VkApplicationInfo applicationInfo = VkApplicationInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO);
            applicationInfo.pApplicationName(stack.UTF8(appInfoProps.getAppName()))
                    .pEngineName(stack.UTF8(appInfoProps.getEngineName()))
                    .applicationVersion(appInfoProps.getAppVersion())
                    .engineVersion(appInfoProps.getEngineVersion())
                    .apiVersion(appInfoProps.getEngineVersion());

            VkInstanceCreateInfo instanceCreateInfo = VkInstanceCreateInfo.callocStack(stack)
                                                                          .sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                    .pApplicationInfo(applicationInfo);
            VkDebugUtilsMessengerCreateInfoEXT messengerCreateInfoEXT =  DebugUtil.getDebugUtilsInfo(DebugUtil.defaultMessenger);

            if(validate){
                instanceCreateInfo.ppEnabledLayerNames(VkValidation.ppValidationLayers(stack));
                instanceCreateInfo.pNext(messengerCreateInfoEXT.address());
            }
            if(VkExtensions.hasInstanceExt()){
                instanceCreateInfo.ppEnabledExtensionNames(VkExtensions.getInstanceExt());
            }

            PointerBuffer pInstance = stack.mallocPointer(1);
            vkCheck(vkCreateInstance(instanceCreateInfo,null,pInstance),"Could not create instance!");
            instance = new VkInstance(pInstance.get(0),instanceCreateInfo);

            if(validate){
                LongBuffer pDebug = stack.mallocLong(1);
                EXTDebugUtils.vkCreateDebugUtilsMessengerEXT(instance,messengerCreateInfoEXT,null,pDebug);
                debugMessenger = pDebug.get(0);
            }else
                debugMessenger=0L;
        }
    }

    public void cleanup(){
        if(debugMessenger!=0L)
            EXTDebugUtils.vkDestroyDebugUtilsMessengerEXT(instance,debugMessenger,null);
        vkDestroyInstance(instance,null);
    }


    public VkInstance getInstance() {
        return instance;
    }

    public long getDebugMessenger() {
        return debugMessenger;
    }

    private final VkInstance instance;
    private final long debugMessenger;
}
