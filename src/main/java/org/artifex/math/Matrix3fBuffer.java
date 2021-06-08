package org.artifex.math;

import org.joml.Matrix3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

/**
 * GLSL is in column-major / as is OpenGL (origin of GLSL)
 */
public class Matrix3fBuffer extends MathBuffer
{

    private Matrix3fBuffer(int capacity){
        this.buffer = MemoryUtil.memAllocFloat(capacity*FLOATS);
    }

    private Matrix3fBuffer(long address, int capacity){
        this.buffer = MemoryUtil.memFloatBuffer(address,capacity*FLOATS);
    }


    public static Matrix3fBuffer memVector4fBuffer(long address, int capacity){
        Matrix3fBuffer buff = new Matrix3fBuffer(address,capacity);

        return  buff;
    }
    public static Matrix3fBuffer memVector4fBuffer( int capacity){
        Matrix3fBuffer buff = new Matrix3fBuffer(capacity);

        return  buff;
    }

    public void putm00(int index,float val){ buffer.put(index*FLOATS,val); }
    public void putm10(int index,float val){ buffer.put(index*FLOATS+1,val); }
    public void putm20(int index,float val){ buffer.put(index*FLOATS+2,val); }


    public void putm01(int index,float val){ buffer.put(index*FLOATS+4,val); }
    public void putm11(int index,float val){ buffer.put(index*FLOATS+5,val); }
    public void putm21(int index,float val){ buffer.put(index*FLOATS+6,val); }


    public void putm02(int index,float val){ buffer.put(index*FLOATS+8,val); }
    public void putm12(int index,float val){ buffer.put(index*FLOATS+9,val); }
    public void putm22(int index,float val){ buffer.put(index*FLOATS+10,val); }


    public float m00(int index){ return buffer.get(index*FLOATS); }
    public float m10(int index){return  buffer.get(index*FLOATS+1); }
    public float m20(int index){ return buffer.get(index*FLOATS+2); }

    public float m01(int index){return buffer.get(index*FLOATS+4); }
    public float m11(int index){return buffer.get(index*FLOATS+5); }
    public float m21(int index){return  buffer.get(index*FLOATS+6); }


    public float m02(int index){return  buffer.get(index*FLOATS+8); }
    public float m12(int index){ return buffer.get(index*FLOATS+9); }
    public float m22(int index){ return buffer.get(index*FLOATS+10); }

    public void put(int index,Matrix3f matrix3f){
        putm00(index,matrix3f.m00);
        putm01(index,matrix3f.m01);
        putm02(index,matrix3f.m02);

        putm10(index,matrix3f.m10);
        putm11(index,matrix3f.m11);
        putm12(index,matrix3f.m12);

        putm20(index,matrix3f.m20);
        putm21(index,matrix3f.m21);
        putm22(index,matrix3f.m22);
    }

    public Matrix3f get(int index){
        Matrix3f matrix3f = new Matrix3f();
        matrix3f.m00=m00(index);
        matrix3f.m10=m10(index);
        matrix3f.m20=m20(index);

        matrix3f.m01=m01(index);
        matrix3f.m11=m11(index);
        matrix3f.m21=m21(index);

        matrix3f.m02=m02(index);
        matrix3f.m12=m12(index);
        matrix3f.m22=m22(index);
        return matrix3f;
    }


    public Matrix3f[] getMatrices(){
        Matrix3f[] matrices = new Matrix3f[capacity()];

        for (int i = 0; i < capacity(); i++) {
            matrices[i]=get(i);
        }
        return matrices;
    }

    @Override
    public long sizeof() {
        return Float.BYTES*buffer.capacity();
    }

    public int capacity(){
        return buffer.capacity()/FLOATS;
    }


    public void free(){
        MemoryUtil.memFree(buffer);
    }

    private final FloatBuffer buffer;

    private static final int FLOATS = 12;
    public static  final long SIZEOF = FLOATS*Float.BYTES;


}
