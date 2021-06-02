package org.artifex.vulkan;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkApplicationInfo;

import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO;

public class Instance
{

    public Instance(boolean validate){
        if(validate){

        }

        try(MemoryStack stack = MemoryStack.stackPush()){
            VkApplicationInfo applicationInfo = VkApplicationInfo.callocStack(stack)
                    .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO);


        }
    }
}
