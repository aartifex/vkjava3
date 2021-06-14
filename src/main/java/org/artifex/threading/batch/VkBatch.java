package org.artifex.threading.batch;

import org.artifex.util.SPIRV;
import org.artifex.vulkan.CommandBuffer;
import org.artifex.vulkan.CommandPool;
import org.artifex.vulkan.Device;
import org.artifex.vulkan.ShaderModule;
import org.artifex.vulkan.queues.Queue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class VkBatch implements Runnable
{

    public VkBatch(Device device){
        this.device=device;
        createCommandPool();
    }


    public final void run(){
        execute();
        close();
    }

    protected abstract void execute();
    protected void close(){
        queue.cleanup();
        primaryBuffer.cleanup();
        commandPool.cleanup();
    }
    protected void cleanUpBuffers(CommandBuffer...buffers){
        for (CommandBuffer buffer : buffers) {
            buffer.cleanup();
        }
    }

    private void createCommandPool(){
        commandPool = new CommandPool(device,queue.getFamilyIndex());
    }

    protected CommandBuffer createPrimaryCommandBuffer(boolean oneTimeSubmit){
        if(primaryBuffer!=null)primaryBuffer.cleanup();
        return primaryBuffer = new CommandBuffer(commandPool,true,oneTimeSubmit);
    }

    protected CommandBuffer newCmdBuffer(boolean oneTimeSubmit){
        return new CommandBuffer(commandPool,false,oneTimeSubmit);
    }

    protected ShaderModule createShaderModuleGLSL(File glslFile, SPIRV.ShaderType shaderType){
        String absolute = glslFile.getAbsolutePath();
        SPIRV spirv = SPIRV.compileShaderAbsoluteFile(absolute,shaderType);
        Objects.requireNonNull(spirv,"Could not compile shader file " +glslFile.getName());
        return new ShaderModule(shaderType.type,device,spirv);

    }


    protected  Queue queue;
    protected CommandPool commandPool;
    protected CommandBuffer primaryBuffer;
    protected final Device device;
}
