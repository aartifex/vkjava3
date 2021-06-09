package org.artifex.threading;

import static org.lwjgl.vulkan.VK10.*;

public class VkAccessFlagBits
{
    public static final VkAccessFlagBits INDIRECT_COMMAND_READ_BIT = new VkAccessFlagBits(VK_ACCESS_INDIRECT_COMMAND_READ_BIT);
    public static final VkAccessFlagBits INDEX_READ_BIT = new VkAccessFlagBits(VK_ACCESS_INDEX_READ_BIT);
    public static final VkAccessFlagBits VERTEX_ATTRIBUTE_READ_BIT = new VkAccessFlagBits(VK_ACCESS_VERTEX_ATTRIBUTE_READ_BIT);
    public static final VkAccessFlagBits UNIFORM_READ_BIT = new VkAccessFlagBits(VK_ACCESS_UNIFORM_READ_BIT);





    private VkAccessFlagBits(int bit){
        this.bit=bit;
    }


    public int get() {
        return bit;
    }

    private final int bit;
}
