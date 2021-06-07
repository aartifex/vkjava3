package org.artifex.vulkan.compute;

import org.lwjgl.vulkan.VkDescriptorSetLayoutBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DescriptorBindings
{

    public DescriptorBindings(int capacity,int stage){
        this.bindings = VkDescriptorSetLayoutBinding.calloc(capacity);
        this.capacity=capacity;
        this.stage=stage;
        this.byteSizes= new ArrayList<>(capacity);
    }



    public DescriptorBindings add(int type, int binding, int count,long bufferSize){
        bindings.get(size++)
                .binding(binding)
                .descriptorType(type)
                .descriptorCount(count)
                .stageFlags(stage);
        byteSizes.add(bufferSize);
        return this;
    }

    public VkDescriptorSetLayoutBinding.Buffer getBindings() {
        return bindings;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return size;
    }

    public int getStage() {
        return stage;
    }

    public List<Long> getByteSizes() {
        return byteSizes;
    }

    private final VkDescriptorSetLayoutBinding.Buffer bindings;
    private final List<Long> byteSizes;
    private final int capacity;
    private int size;
    private final int stage;
}
