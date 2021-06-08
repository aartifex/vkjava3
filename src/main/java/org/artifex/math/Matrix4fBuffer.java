package org.artifex.math;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Matrix4fBuffer extends MathBuffer
{

    private Matrix4fBuffer(int capacity) {
        this.buffer = MemoryUtil.memAllocFloat(capacity*FLOATS);
    }






    @Override
    public long sizeof() {
        return Float.BYTES*buffer.capacity();
    }

    private final FloatBuffer buffer;
    private static final int FLOATS = 16;
    public static final int SIZEOF = Float.BYTES*FLOATS;
}
