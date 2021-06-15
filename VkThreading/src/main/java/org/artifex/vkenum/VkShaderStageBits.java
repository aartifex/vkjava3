package org.artifex.vkenum;

import static org.lwjgl.vulkan.VK10.*;
public class VkShaderStageBits extends VkBits<VkShaderStageBits>
{
    public static final VkShaderStageBits FRAGMENT = new VkShaderStageBits(VK_SHADER_STAGE_FRAGMENT_BIT);
    public static final VkShaderStageBits VERTEX = new VkShaderStageBits(VK_SHADER_STAGE_VERTEX_BIT);
    public static final VkShaderStageBits COMPUTE = new VkShaderStageBits(VK_SHADER_STAGE_COMPUTE_BIT);
    public static final VkShaderStageBits GEOMETRY = new VkShaderStageBits(VK_SHADER_STAGE_GEOMETRY_BIT);
    public static final VkShaderStageBits TESS_CONTROL = new VkShaderStageBits(VK_SHADER_STAGE_TESSELLATION_CONTROL_BIT);
    public static final VkShaderStageBits TESS_EVAL = new VkShaderStageBits(VK_SHADER_STAGE_TESSELLATION_EVALUATION_BIT);


    public VkShaderStageBits(int bit) {
        super(bit);
    }
}
