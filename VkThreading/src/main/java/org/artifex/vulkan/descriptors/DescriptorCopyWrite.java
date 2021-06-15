package org.artifex.vulkan.descriptors;

import org.artifex.vulkan.Device;
import org.lwjgl.vulkan.VkCopyDescriptorSet;
import org.lwjgl.vulkan.VkWriteDescriptorSet;

import static org.lwjgl.vulkan.VK10.*;

public class DescriptorCopyWrite
{

    public static final DescriptorCopyWrite NULL = new DescriptorCopyWrite(0,0);


    public DescriptorCopyWrite(int writeSize, int copySize){
        this.descriptorWrite = VkWriteDescriptorSet.calloc(writeSize);
        this.descriptorCopy = (copySize == 0 ) ? null : VkCopyDescriptorSet.calloc(copySize);

        descriptorWrite.forEach((w)->w.sType(VK_STRUCTURE_TYPE_WRITE_DESCRIPTOR_SET));
        if(descriptorCopy!=null)
            descriptorCopy.forEach((w)->w.sType(VK_STRUCTURE_TYPE_COPY_DESCRIPTOR_SET));


    }



    public DescriptorCopyWrite addCopy(int srcBinding, int srcArrElem,long srcSet, int dstBinding,int dstArrElem,
                                       DescriptorSet set)
    {
        descriptorCopy.get(copyCount++)
                .srcSet(srcSet)
                .srcBinding(srcBinding)
                .dstBinding(dstBinding)
                .dstArrayElement(dstArrElem)
                .srcArrayElement(srcArrElem)
                .dstSet(set.getSetHandle());
        return this;
    }

    public DescriptorCopyWrite addWrite(int dstBinding, DescriptorSet dstSet){
        DescriptorBindings b = dstSet.getBindings();
        descriptorWrite.get(writeCount++)
                .dstSet(dstSet.getSetHandle())
                .dstBinding(dstBinding)
                .pBufferInfo(dstSet.getBufferInfo().get(dstBinding))
                .descriptorCount(b.getBindings().get(dstBinding).getBinding().descriptorCount())
                .descriptorType(b.getBindings().get(dstBinding).getDescriptorType());
        return this;
    }


    public void updateDescriptorSets(Device device){
        vkUpdateDescriptorSets(device.getDevice(),descriptorWrite,descriptorCopy);
    }


    private final VkWriteDescriptorSet.Buffer descriptorWrite;
    private int writeCount=0;
    private final VkCopyDescriptorSet.Buffer descriptorCopy;
    private int copyCount=0;

}
