package org.artifex.math;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Vector3fBuffer
{

    private Vector3fBuffer(int capacity){
        this.buffer = MemoryUtil.memAllocFloat(capacity*3);
    }

    private Vector3fBuffer(long address, int capacity){
        this.buffer = MemoryUtil.memFloatBuffer(address,capacity*3);
    }

    public static Vector3fBuffer memVec3fBuffer(long address, int capacity){
        Vector3fBuffer buffer = new Vector3fBuffer(address,capacity);
        return buffer;
    }

    public static Vector3fBuffer memAllocVec3f(int capacity){
        Vector3fBuffer buffer = new Vector3fBuffer(capacity);

        return buffer;
    }


    public float x(int index){
        return buffer.get(index*4);

    }
    public float y(int index){
        return buffer.get(index*4+1);

    }
    public float z(int index){
        return buffer.get(index*4+2);
    }

    public void setX(int index,float val){
        buffer.put(index*4,val);
    }
    public void setY(int index,float val){
        buffer.put(index*4+1,val);
    }
    public void setZ(int index,float val){
        buffer.put(index*4+2,val);
    }

//    public void set(float[] floats){
//        buffer.put(floats);
//    }
    public void setVec(int index, float[] xyz){
        setX(index,xyz[0]);
        setY(index,xyz[1]);
        setZ(index,xyz[2]);
    }

    public long sizeof(){
        return buffer.capacity()* Float.BYTES;
    }
    public FloatBuffer getBuffer(){
        return buffer;
    }
    public Vector3f[] getVectors(){
        Vector3f[] vector3fs = new Vector3f[capacity()];
        for (int i = 0; i < vector3fs.length; i++) {
//            vector3fs[i].x=x(i);
            vector3fs[i] = new Vector3f(x(i),y(i),z(i));
//            System.out.println(vector3fs[i] + " -------------");
        }
        return vector3fs;
    }

    public int capacity(){
        return buffer.capacity()/4;
    }
//    public void free(){
//
//    }
//    private Vector3f[] vectors;
    private FloatBuffer buffer;
    public static final int SIZEOF = 4*Float.BYTES;
}
