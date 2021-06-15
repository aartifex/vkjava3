package org.artifex;

import org.artifex.math.Matrix3fBuffer;
import org.artifex.props.AppProperties;
import org.artifex.threading.synch.Fence;
import org.artifex.threading.synch.Semaphore;
import org.artifex.util.SPIRV;
import org.artifex.vulkan.*;
import org.artifex.vulkan.compute.ComputePipeline;
import org.artifex.vulkan.descriptors.*;
import org.joml.Matrix3f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.vulkan.VK10.*;

public class Program
{

    public Program(Window window){
        AppProperties properties = AppProperties.getInstance();

//        w = new Window("Title");

        instance = new Instance(properties.isValidate());
        physicalDevice = PhysicalDevice.createPhysicalDevice(instance,null);
        device = new Device(physicalDevice);

        GLSLayout layout = new GLSLayout(1,0,0);
        layout.setDescriptor(0,Matrix3fBuffer.SIZEOF*1000000,1000000);

        GLSLayout layout2 = new GLSLayout(1,0,1);
        layout2.setDescriptor(0,Float.BYTES*128,128);

        DescriptorBinding descriptorBinding = new DescriptorBinding(layout,VK_DESCRIPTOR_TYPE_STORAGE_BUFFER,
                VK_SHADER_STAGE_COMPUTE_BIT);
        DescriptorBinding descriptorBinding1 = new DescriptorBinding(layout2,VK_DESCRIPTOR_TYPE_STORAGE_BUFFER,
                VK_SHADER_STAGE_COMPUTE_BIT);

        DescriptorBindings bindings = new DescriptorBindings(new ArrayList<>(List.of(descriptorBinding,descriptorBinding1)),
                0);
        DescriptorSet set = new DescriptorSet(device,bindings,0);
        set.allocate();

        copyWrite = new DescriptorCopyWrite(2,0);
        copyWrite.addWrite(0,set);
        copyWrite.addWrite(1,set);
        copyWrite.updateDescriptorSets(device);

        computeQueue = new Queue.ComputeQueue(device,0);
        commandPool = new CommandPool(device,computeQueue.getQueueFamilyIndex());
        commandBuffer = new CommandBuffer(commandPool,true,true);

        //submit

        ComputePipeline compute = new ComputePipeline(device,set,
                new ShaderModule(VK_SHADER_STAGE_COMPUTE_BIT,device,SPIRV.compileShaderFile("compute.comp", SPIRV.ShaderType.COMPUTE_SHADER)));
        fence = new Fence(device,false);

         signal = new Semaphore(device);



        Matrix3fBuffer indat = set.mapAsMatrix3f(0,0);
        for (int i = 0; i < indat.capacity(); i++) {
            indat.put(i, new Matrix3f(1,1,1,
                    1,1,1,
                    1,1,1));
        }
        set.unmapBuffer(0,0);

//        copyWrite.updateDescriptorSets(device);


        record(set,bindings,compute);


            IntBuffer shstg = MemoryUtil.memAllocInt(1).put(0,VK_SHADER_STAGE_COMPUTE_BIT);



        computeQueue.submit(MemoryUtil.memAllocPointer(1).put(0,commandBuffer.getCommandBuffer().address()),
                null, shstg,
                null,fence);
        long start = System.nanoTime();

        computeQueue.waitIdle();
        long fin = System.nanoTime();

        device.waitIdle();
        fence.fenceWait();



        System.out.println("Done in " + (fin-start)/1E6 + " millis");
        Matrix3fBuffer ibuff = set.mapAsMatrix3f(0,0);

        Matrix3f[] vecs = ibuff.getMatrices();
        for (int i = 0; i < 3; i++) {
            System.out.println(vecs[i]);
        }
        set.unmapBuffer(0,0);

//        ibuff.free();
//        indat.free();

    }

    public void record(DescriptorSet set, DescriptorBindings bindings, ComputePipeline compute){

        try(MemoryStack stack= MemoryStack.stackPush()){
        commandBuffer.beginRecording();
        vkCmdBindPipeline(commandBuffer.getCommandBuffer(),VK_PIPELINE_BIND_POINT_COMPUTE,
                compute.getPipeline());

        vkCmdBindDescriptorSets(commandBuffer.getCommandBuffer(),VK_PIPELINE_BIND_POINT_COMPUTE,compute.getPipelineLayout(),
                0,stack.longs(set.getSetHandle()),null
                );

        vkCmdDispatch(commandBuffer.getCommandBuffer(),1,2,1);
            commandBuffer.endRecording();

        }

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
