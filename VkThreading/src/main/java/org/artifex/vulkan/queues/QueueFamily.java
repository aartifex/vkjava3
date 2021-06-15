package org.artifex.vulkan.queues;

import org.lwjgl.vulkan.VkQueueFamilyProperties;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.vulkan.VK10.*;

public class QueueFamily
{

    public static final QueueFamily NULL = new QueueFamily(null,-1);

    public QueueFamily(VkQueueFamilyProperties properties,int index){
        if(properties==null){
            this.properties=null;
            this.familyIndex=-1;
            this.queueInfo = new ArrayList<>();
        }else {
            this.properties = properties;
            this.familyIndex = index;

            this.queueInfo = new ArrayList<>(properties.queueCount());
            for (int i = 0; i < properties.queueCount(); i++) {
                queueInfo.add(new QueueInfo(this, i));
            }

            int flags = properties.queueFlags();

            supportsCompute = (flags & VK_QUEUE_COMPUTE_BIT) == VK_QUEUE_COMPUTE_BIT;
            supportsGraphics = (flags & VK_QUEUE_GRAPHICS_BIT) == VK_QUEUE_GRAPHICS_BIT;
            supportsTransfer = (flags & VK_QUEUE_TRANSFER_BIT) == VK_QUEUE_TRANSFER_BIT;
            supportsSparseBinding = (flags & VK_QUEUE_SPARSE_BINDING_BIT) == VK_QUEUE_SPARSE_BINDING_BIT;
        }
    }


    public int queueCount(){
        return properties.queueCount();
    }

    public int remaining(){
        int r=0;
        for (QueueInfo info : queueInfo) {
            if(info.isAvailable())r++;
        }
        return r;
    }

    public boolean supportsCompute() {
        return supportsCompute;
    }

    public boolean supportsGraphics() {
        return supportsGraphics;
    }

    public boolean supportsSparseBinding() {
        return supportsSparseBinding;
    }

    public boolean supportsTransfer() {
        return supportsTransfer;
    }

    public boolean supports(QueueType type){
        switch (type){
            case SPARSE_BINDING:
                return supportsSparseBinding;
            case GRAPHICS:
                return supportsGraphics;
            case COMPUTE:
                return supportsCompute;
            case TRANSFER:
                return supportsTransfer;
        }
        return false;
    }

    public QueueInfo getFirstAvailableQueue(){
        while(remaining()==0){
        }
        QueueInfo res = null;
        for (QueueInfo info : queueInfo) {
            if(info.isAvailable()){
                info.setAvailable(false);
                res=info; break;
            }
        }
        return res;
    }

    void freeQueue(QueueInfo info){
        info.setAvailable(true);
    }
    public void cleanup(){
        properties.free();
    }

    public int getFamilyIndex() {
        return familyIndex;
    }

    private final List<QueueInfo> queueInfo;
    private final VkQueueFamilyProperties properties;
    private final int familyIndex;
    private boolean supportsGraphics;
    private boolean supportsCompute;
    private boolean supportsTransfer;
    private boolean supportsSparseBinding;
}
