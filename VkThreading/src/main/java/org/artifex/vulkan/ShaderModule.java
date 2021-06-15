package org.artifex.vulkan;

import org.artifex.util.SPIRV;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkShaderModuleCreateInfo;

import java.nio.LongBuffer;

import static org.artifex.util.DebugUtil.vkCheck;
import static org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO;
import static org.lwjgl.vulkan.VK10.vkCreateShaderModule;

public class ShaderModule
{

    public ShaderModule(int shaderStage, Device device,SPIRV spirv){
        this.shaderStage=shaderStage;

        try(MemoryStack stack = MemoryStack.stackPush()){

            LongBuffer pShader = stack.mallocLong(1);

            VkShaderModuleCreateInfo shaderModuleCreateInfo = VkShaderModuleCreateInfo.callocStack()
                    .sType(VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO)
                    .pCode(spirv.bytecode());

            vkCheck(vkCreateShaderModule(device.getDevice(),shaderModuleCreateInfo,null,pShader)
                   ,"Could not create shader module!");

            handle = pShader.get(0);
        }
    }

    public int getShaderStage() {
        return shaderStage;
    }


    public long getHandle() {
        return handle;
    }



    private final int shaderStage;
    private final long handle;
}
