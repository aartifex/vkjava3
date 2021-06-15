package org.artifex.vulkan.descriptors;

import java.util.*;

public class DescriptorBindings
{


    //todo have a check or change to ensure all bindings are within set.
    public DescriptorBindings(int set,List<DescriptorBinding> bindings){
        this.bindings=bindings;
        this.size=bindings.size();
        this.set=set;
        this.bindings.sort(new Comparator<DescriptorBinding>() {
            @Override
            public int compare(DescriptorBinding o1, DescriptorBinding o2) {
                return (o1.getGlsLayout().getBinding()-o2.getGlsLayout().getBinding());
            }
        });
        bindingMap = new HashMap<>();
        for (DescriptorBinding binding : bindings) {
            bindingMap.put(binding.getGlsLayout().getBinding(),binding);
        }

    }
    public DescriptorBindings(int set,DescriptorBinding...bindings){
        this(set,new ArrayList<>(List.of(bindings)));
    }




//    public VkDescriptorSetLayoutBinding.Buffer getBindings() {
//        return bindings;
//    }


    public DescriptorBinding getBinding(int binding){
        return bindingMap.get(binding);
    }
    public List<DescriptorBinding> getBindings() {
        return bindings;
    }


    public int getSize() {
        return size;
    }


public void cleanup(){
        bindings.forEach(DescriptorBinding::cleanup);
}

    public int getSet() {
        return set;
    }

    //    private final VkDescriptorSetLayoutBinding.Buffer bindings;
    private final List<DescriptorBinding> bindings;
    private final Map<Integer,DescriptorBinding> bindingMap;
//    private final List<Long> byteSizes;
    private int size;
    private int set;
}
