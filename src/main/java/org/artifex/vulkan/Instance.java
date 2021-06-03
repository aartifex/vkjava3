package org.artifex.vulkan;

import org.artifex.props.AppInfoProps;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO;

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
                    .pApplicationInfo(applicationInfo);

        }
    }
}
