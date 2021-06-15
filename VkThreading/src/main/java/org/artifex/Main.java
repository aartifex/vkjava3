package org.artifex;

import org.artifex.math.Vector4fBuffer;
import org.artifex.threading.VkProcess;
import org.artifex.vkenum.VkBits;
import org.artifex.vkenum.VkShaderStageBits;
import org.artifex.vulkan.Queue;
import org.artifex.vulkan.Swapchain;
import org.artifex.vulkan.compute.ComputeProgram;
import org.artifex.vulkan.descriptors.*;

import java.io.File;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.vulkan.VK10.VK_DESCRIPTOR_TYPE_STORAGE_BUFFER;


public class Main
{
    public static void main(String[] args) {
        VkBits<VkShaderStageBits> shaderStageBits = VkBits.or(VkShaderStageBits.COMPUTE,VkShaderStageBits.FRAGMENT);
        new Main();
    }



    public Main(){
        Vulkan v = new Vulkan(null);



//        GLSLayout inBuff = new GLSLayout(1,0,0);
//        inBuff.setDescriptor(0,Integer.BYTES*10000000,10000000);


//        new Queue.ComputeQueue(v.getDevice(),0,1);
//        new Queue.ComputeQueue(v.getDevice(),0,0);
//        DescriptorBinding binding = new DescriptorBinding(inBuff,VK_DESCRIPTOR_TYPE_STORAGE_BUFFER,VkShaderStageBits.COMPUTE.get());
//        DescriptorBindings bindings = new DescriptorBindings(0,binding);
//
//        DescriptorSet set = new DescriptorSet(v.getDevice(),bindings,0);
//        set.allocate();

//        File f1 = new File(getClass().getResource("/compute.comp").toExternalForm());
        File f2 = new File(getClass().getResource("/compute2.comp").toExternalForm());


        GLSLayout inBuff2 = new GLSLayout(1,0,0);
        inBuff2.setDescriptor(0,Integer.BYTES*20000,20000);
        GLSLayout ibuff2 = new GLSLayout(1,0,1);
        ibuff2.setDescriptor(0,Integer.BYTES*20000,20000);

        DescriptorBinding binding2 = new DescriptorBinding(inBuff2,VK_DESCRIPTOR_TYPE_STORAGE_BUFFER,VkShaderStageBits.COMPUTE.get());
        DescriptorBinding binding3 = new DescriptorBinding(ibuff2,VK_DESCRIPTOR_TYPE_STORAGE_BUFFER,VkShaderStageBits.COMPUTE.get());

        DescriptorBindings bindings2 = new DescriptorBindings(0,binding2,binding3);

        DescriptorSet set2 = new DescriptorSet(v.getDevice(),bindings2,0);
        set2.allocate();

        set2.setCopyWrite(new DescriptorCopyWrite(2,0)
        .addWrite(0,set2)
        .addWrite(1,set2));
        set2.getCopyWrite().updateDescriptorSets(v.getDevice());
//        set.allocate();
//        ComputeProgram pp = new ComputeProgram(v, List.of(set),f1,true);
        ComputeProgram pi = new ComputeProgram(v, List.of(set2),f2,true);

        IntBuffer b1 = set2.mapAsInt(1,0);

        for (int i = 0; i <b1.capacity() ; i++) {
            b1.put(i,i);
        }
        set2.unmapBuffer(1,0);


//        ExecutorService service = Executors.newFixedThreadPool(2);

//        long start = System.nanoTime();
//        Future<?> fut1 = service.submit(pi);
//        Future<?> fut2 = service.submit(pp);
//
//        while(!fut1.isDone() && !fut2.isDone());
//        long end = System.nanoTime();


        long start = System.nanoTime();

//        pp.run();
        pi.run();

        long end = System.nanoTime();


        System.out.println("Dont in " + (end-start)/1E6);

//        set2.getCopyWrite().updateDescriptorSets(v.getDevice());
        IntBuffer ss = pi.getSet(0).mapAsInt(0,0);

        for (int i = 0; i <100 ; i++) {
            System.out.println(ss.get(i));
        }
//        pp.cleanup();
        pi.cleanup();
    }



}
