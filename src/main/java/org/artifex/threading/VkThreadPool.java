package org.artifex.threading;

import java.util.*;

public class VkThreadPool extends ThreadGroup
{


    public VkThreadPool(String name) {
        super(name);
    }

    public Thread addWorker(VkWorker worker){
        Thread t =  new Thread(worker,VkWorker.class.getCanonicalName());
        threadMap.put(t,worker);
        return t;
    }


    public Set<VkWorker> getWorkers() {
        return (Set<VkWorker>)threadMap.values();
    }

    private final List<VkWorker> workers = new ArrayList<>(4);
    private final Map<Thread, VkWorker> threadMap = new HashMap<>(4);
}
