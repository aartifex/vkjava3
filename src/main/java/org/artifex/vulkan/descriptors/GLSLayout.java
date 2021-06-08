package org.artifex.vulkan.descriptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GLSLayout
{

    public GLSLayout(int descriptorCount, int set, int binding){
        this.descriptorCount = descriptorCount;
        this.set=set;
        this.binding=binding;
        descriptors = new HashMap<>(descriptorCount);
    }



    public void setDescriptor(int descriptorNumber, long bytes, int length){
        long totalSize = totalByteSize();
        Descriptor m = new Descriptor(bytes,length,totalSize);
        descriptors.put(descriptorNumber,m);
    }


    public int getSet() {
        return set;
    }

    public int getBinding() {
        return binding;
    }
    public long totalByteSize(){
        long res = 0L;
        for (Descriptor descriptor : descriptors.values()) {
            res+= descriptor.sizeof;
        }
        return res;
    }

    public long totalLength(){
        long res = 0L;
        for (Descriptor descriptor : descriptors.values()) {
            res+= descriptor.length;
        }
        return res;
    }

    public static class Descriptor {
        private long sizeof;
        private int length;
        private long offset;

        private Descriptor(long sizeof, int length,long offset){
            this.length=length;
            this.sizeof=sizeof;
            this.offset=offset;
        }

        public int getLength() {
            return length;
        }

        public long getSizeof() {
            return sizeof;
        }

        public long getOffset() {
            return offset;
        }
    }


    public Descriptor getDescriptor(int num){
        return descriptors.get(num);
    }

    public int getDescriptorCount() {
        return descriptorCount;
    }

    private final int descriptorCount;
    private final int set;
    private final int binding;
    private Map<Integer,Descriptor> descriptors;

}
