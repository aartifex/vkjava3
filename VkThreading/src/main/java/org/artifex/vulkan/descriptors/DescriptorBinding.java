package org.artifex.vulkan.descriptors;

import org.artifex.vulkan.VulkanBuffer;
import org.lwjgl.vulkan.VkDescriptorSetLayoutBinding;

import java.util.List;

/**
 * Basis for a descriptor binding in the Vulkan API.
 */
public class DescriptorBinding
{

    public DescriptorBinding(GLSLayout layout, int descriptorType, int stageFlag){
        this.glsLayout =layout;

        binding = VkDescriptorSetLayoutBinding.calloc()
                .stageFlags(stageFlag)
                .binding(layout.getBinding())
                .descriptorCount(layout.getDescriptorCount())
                .descriptorType(descriptorType);

        this.descriptorType = descriptorType;
        this.shaderStage=stageFlag;
    }

    public GLSLayout getGlsLayout() {
        return glsLayout;
    }

    public VkDescriptorSetLayoutBinding getBinding() {
        return binding;
    }


    public int getDescriptorType() {
        return descriptorType;
    }

    public int getShaderStage() {
        return shaderStage;
    }

    /**
     * Developer is responsible for freeing this memory. Will implement a stack version later.
     * @param bindings
     * @return
     */
    public static VkDescriptorSetLayoutBinding.Buffer bindingsToBuffer(List<DescriptorBinding> bindings){
        VkDescriptorSetLayoutBinding.Buffer buffer = VkDescriptorSetLayoutBinding.calloc(bindings.size());
        for (int i = 0; i < bindings.size(); i++) {
            buffer.get(i).set(bindings.get(i).binding);
        }
        return buffer;
    }

    public static VkDescriptorSetLayoutBinding.Buffer bindingsToBuffer(DescriptorBinding... bindings){
        return bindingsToBuffer(List.of(bindings));
    }


//    public void setBuffers(List<VulkanBuffer> buffers) {
//        this.buffers = buffers;
//    }


    public void setBuffer(VulkanBuffer buffer) {
        this.buffer = buffer;
    }

    public VulkanBuffer getBuffer() {
        return buffer;
    }

    public void cleanup(){
        if(buffer!=null)
            buffer.cleanup();
        binding.free();
    }

    private VulkanBuffer buffer;
    private final VkDescriptorSetLayoutBinding binding;
    private  final GLSLayout glsLayout;
    private final int descriptorType;
    private final int shaderStage;
}
