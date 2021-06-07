package org.artifex;

import org.artifex.props.AppProperties;
import org.artifex.util.Pointers;
import org.artifex.util.SPIRV;
import org.artifex.vulkan.*;
import org.artifex.vulkan.compute.Compute;
import org.artifex.vulkan.compute.DescriptorBindings;
import org.artifex.vulkan.compute.DescriptorCopyWrite;
import org.artifex.vulkan.compute.DescriptorSet;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.VkBufferMemoryBarrier;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkSubmitInfo;

import java.nio.IntBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class Program
{

    public Program(Window window){
        AppProperties properties = AppProperties.getInstance();

//        w = new Window("Title");

        instance = new Instance(properties.isValidate());
        physicalDevice = PhysicalDevice.createPhysicalDevice(instance,null);
        device = new Device(physicalDevice);

        DescriptorBindings bindings = new DescriptorBindings(2,VK_SHADER_STAGE_COMPUTE_BIT)
                .add(VK_DESCRIPTOR_TYPE_STORAGE_BUFFER,0,1,Integer.BYTES*64)
                .add(VK_DESCRIPTOR_TYPE_STORAGE_BUFFER,1,1,Integer.BYTES*64);

        computeQueue = new Queue.ComputeQueue(device,0);

        this.commandPool = new CommandPool(device,computeQueue.getQueueFamilyIndex());
        this.commandBuffer = new CommandBuffer(commandPool,true,true);
        DescriptorSet set = new DescriptorSet(device,bindings,1);
         copyWrite = new DescriptorCopyWrite(set,2,0)
                .addWrite(0,set.getHandles().get(0),set.getBufferInfos())
                 .addWrite(1,set.getHandles().get(0),set.getBufferInfos());
//                .addCopy(0,0,set.getHandles().get(0),1,0);
        Compute compute = new Compute(device,set,
                new ShaderModule(VK_SHADER_STAGE_COMPUTE_BIT,device,SPIRV.compileShaderFile("compute.comp", SPIRV.ShaderType.COMPUTE_SHADER)));
        fence = new Fence(device,false);

         signal = new Semaphore(device);

        System.out.println("---------------------------------------------------------");


        long map1 = set.getBuffer(0).map();
        IntBuffer indat = MemoryUtil.memIntBuffer(map1,100);
        for (int i = 0; i < indat.capacity(); i++) {
            indat.put(i,i);
        }
        copyWrite.updateDescriptorSets();
        set.getBuffer(0).unmap();


        record(set,bindings,compute);


//        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer shstg = MemoryUtil.memAllocInt(1).put(0,VK_SHADER_STAGE_COMPUTE_BIT);




        computeQueue.submit(MemoryUtil.memAllocPointer(1).put(0,commandBuffer.getCommandBuffer().address()),
                null, shstg,
                null,fence);

        computeQueue.waitIdle();
        device.waitIdle();
        copyWrite.updateDescriptorSets();
        PointerBuffer pLoad = MemoryUtil.memAllocPointer(1);
//        vkMapMemory(device.getDevice(),set.getBuffer(1).getMemory(),0,Integer.BYTES*64,0, pLoad);

        IntBuffer output =MemoryUtil.memIntBuffer(set.getBuffer(0).map(),128);// MemoryUtil.memIntBuffer(set.getBuffer(1).map(),100);

        for (int i = 0; i < output.capacity(); i++) {
            System.out.println(output.get(i));
        }
        set.getBuffer(1).unmap();

//        while(w.alive()){
//            w.pollEvents();
//        }


    }

    public void record(DescriptorSet set, DescriptorBindings bindings,Compute compute){

        commandBuffer.beginRecording();
        vkCmdBindPipeline(commandBuffer.getCommandBuffer(),VK_PIPELINE_BIND_POINT_COMPUTE,
                compute.getComputePipeline());

        vkCmdBindDescriptorSets(commandBuffer.getCommandBuffer(),VK_PIPELINE_BIND_POINT_COMPUTE,compute.getPipelineLayout(),
                0,Pointers.listToBuffer(set.getHandles()),null
                );

        vkCmdDispatch(commandBuffer.getCommandBuffer(),100/256+1,1,1);
            commandBuffer.endRecording();



    }


    private final Instance instance;
    private final PhysicalDevice physicalDevice;
    private final Device device;

    private final CommandBuffer commandBuffer;
    private final CommandPool commandPool;
    private final Queue.ComputeQueue computeQueue;

    private final DescriptorCopyWrite copyWrite;
    private Fence fence;
    private Semaphore signal;

}
