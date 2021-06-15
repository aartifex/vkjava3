package org.artifex.vkenum;

public class VkBits<SELF >
{

    public VkBits(int bit){
        this.bit=bit;
    }


    public int get() {
        return bit;
    }

    public static <SELF> VkBits<SELF> or(VkBits<SELF>...bits){
        int bitmask = 0;
        for (VkBits<SELF> bit : bits) {
            bitmask|=bit.get();
        }
        return new VkBits<>(bitmask);
    }

    private final int bit;
}
