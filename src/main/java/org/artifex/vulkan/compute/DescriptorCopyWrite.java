package org.artifex.vulkan.compute;

import org.lwjgl.vulkan.VkCopyDescriptorSet;
import org.lwjgl.vulkan.VkDescriptorBufferInfo;
import org.lwjgl.vulkan.VkWriteDescriptorSet;

import static org.lwjgl.vulkan.VK10.*;

public class DescriptorCopyWrite
{


    public DescriptorCopyWrite(DescriptorSet descriptorSet,int writeSize, int copySize){
        this.descriptorWrite = VkWriteDescriptorSet.calloc(writeSize);
        this.descriptorCopy = VkCopyDescriptorSet.calloc(copySize);

        descriptorWrite.forEach((w)->w.sType(VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET));
        descriptorCopy.forEach((w)->w.sType(VK_STRUCTURE_TYPE_COPY_DESCRIPTOR_SET));

        this.set=descriptorSet;
    }


    /**
     * Implied destinationSet is the one passed in the constructor
     * @param srcBinding
     * @param srcSet
     * @param dstBinding
     * @return
     */
    public DescriptorCopyWrite addCopy(int srcBinding, int srcArrElem,long srcSet, int dstBinding,int dstArrElem)
    {
        descriptorCopy.get(copyCount++)
                .srcSet(srcSet)
                .srcBinding(srcBinding)
                .dstBinding(dstBinding)
                .dstArrayElement(dstArrElem)
                .srcArrayElement(srcArrElem)
                .dstSet(set.getHandles().get(0));
        return this;
    }

    public DescriptorCopyWrite addWrite(int dstBinding, long dstSet, VkDescriptorBufferInfo.Buffer buffers){
        descriptorWrite.get(writeCount++)
                .dstSet(dstSet)
                .dstBinding(dstBinding)
                .pBufferInfo(set.getBufferInfos())
                .descriptorCount(set.getDescriptorCount())
                .descriptorType(set.getDescriptorType())
                .pBufferInfo(buffers);
        return this;
    }


    public void updateDescriptorSets(){
        vkUpdateDescriptorSets(set.getDevice().getDevice(),descriptorWrite,descriptorCopy);
    }

    private final DescriptorSet set;

    private final VkWriteDescriptorSet.Buffer descriptorWrite;
    private int writeCount=0;
    private final VkCopyDescriptorSet.Buffer descriptorCopy;
    private int copyCount=0;

}
