package org.artifex.vulkan.queues;

public class QueueInfo
{

    public QueueInfo(QueueFamily queueFamily,int index){
        this.index=index;
        this.queueFamily=queueFamily;
    }


    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getIndex() {
        return index;
    }

    public void free(){
        queueFamily.freeQueue(this);
    }

    private boolean available = true;
    private final int index;
    private final QueueFamily queueFamily;
}
