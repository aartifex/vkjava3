package org.artifex.vulkan.compute;

import org.artifex.Vulkan;
import org.artifex.synch.Fence;
import org.artifex.util.SPIRV;
import org.artifex.vkenum.VkShaderStageBits;
import org.artifex.vulkan.*;
import org.artifex.vulkan.descriptors.DescriptorSet;
import org.artifex.vulkan.descriptors.DescriptorSets;
import org.artifex.vulkan.queues.QueueFamily;
import org.artifex.vulkan.queues.QueueType;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.nio.LongBuffer;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.vulkan.VK10.*;

public class ComputeProgram implements Runnable
{



    public ComputeProgram(Vulkan vk, List<DescriptorSet> sets, File file,boolean runOnce){
        this.sets=sets;
        this.oneTimeSubmit=runOnce;
        this.vulkan=vk;
        QueueFamily q = vk.getPhysicalDevice().getAvailableQueueFamily(QueueType.COMPUTE);
        this.queue = new Queue.ComputeQueue(vk.getDevice(),q);
        this.cmdPool=new CommandPool(vk.getDevice(),q.getFamilyIndex());
        this.cmdBuffer = new CommandBuffer(cmdPool,true,runOnce);

        sets.forEach(DescriptorSet::allocate); //ensure allocated

        spirv = SPIRV.compileShaderAbsoluteFile(file.toString().replace("\\","/"), SPIRV.ShaderType.COMPUTE_SHADER);
        Objects.requireNonNull(spirv,"SPIRV code is null. Shader did not compile correctly: " + file.getName());
        module = new ShaderModule(VkShaderStageBits.COMPUTE.get(),vk.getDevice(),spirv);
        this.pipeline= new ComputePipeline(vk.getDevice(),sets,module);


        record();

        this.fence = new Fence(vk.getDevice(),false);

        this.id=idgen.getAndIncrement();
//        getSet(0).getCopyWrite().updateDescriptorSets(vk.getDevice());
    }

    private void record(){
        try(MemoryStack stack = MemoryStack.stackPush()) {
            cmdBuffer.beginRecording();

            vkCmdBindPipeline(cmdBuffer.getCommandBuffer(), VK_PIPELINE_BIND_POINT_COMPUTE,
                    pipeline.getPipeline());

            int min = Integer.MAX_VALUE;
            LongBuffer pSets = stack.mallocLong(sets.size());

            for (DescriptorSet set : sets) {
                if (set.getBindings().getSet() < min) min = set.getBindings().getSet();
                pSets.put(set.getSetHandle());
            }


            vkCmdBindDescriptorSets(cmdBuffer.getCommandBuffer(), VK_PIPELINE_BIND_POINT_COMPUTE,
                    pipeline.getPipelineLayout(), min, pSets,null);

            vkCmdDispatch(cmdBuffer.getCommandBuffer(),1,1,1);

            cmdBuffer.endRecording();
        }
    }

    @Override
    public void run() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer pCmd = stack.pointers(cmdBuffer.getCommandBuffer().address());//.put(0, cmdBuffer.getCommandBuffer().address());
            queue.submit(pCmd, null,stack.ints(VK_PIPELINE_STAGE_COMPUTE_SHADER_BIT),null,fence);
//            getSet(0).getCopyWrite().updateDescriptorSets(vulkan.getDevice());

        }
        queue.waitIdle();
        System.out.println("DONE WITH " + id);
        isDone=true;
//        cleanup();
    }


    public void cleanup(){
        spirv.free();
        cmdBuffer.cleanup();
        cmdPool.cleanup();
        fence.cleanup();
    }


    public DescriptorSet getSet(int setNum){
        for (DescriptorSet set : sets) {
            if(set.getBindings().getSet()==setNum)return set;
        }
        return DescriptorSet.NULL;
    }


    private ComputePipeline pipeline;
    private Vulkan vulkan;
    private Queue.ComputeQueue queue;
    private CommandBuffer cmdBuffer;
    private CommandPool cmdPool;
    private boolean oneTimeSubmit;
    private List<DescriptorSet> sets;
    private SPIRV spirv;
    private ShaderModule module;
    private Fence fence;

    private boolean isDone = false;

    private final int id;
    private static final AtomicInteger idgen = new AtomicInteger();

}
