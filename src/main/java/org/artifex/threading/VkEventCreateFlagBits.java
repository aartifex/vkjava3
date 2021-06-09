package org.artifex.threading;

public class VkEventCreateFlagBits
{

    //VK KHR synchronization 2
    public static VkEventCreateFlagBits DEVICE_ONLY_BIT_KHR = new VkEventCreateFlagBits(0x00000001);
    public static VkEventCreateFlagBits NULL = new VkEventCreateFlagBits(0x0);



    private VkEventCreateFlagBits(int bit){
        this.bit=bit;
    }
    public int get() {
        return bit;
    }

    private final int bit;
}
