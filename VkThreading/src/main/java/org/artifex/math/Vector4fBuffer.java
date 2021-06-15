package org.artifex.math;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Vector4fBuffer extends MathBuffer
{

    private Vector4fBuffer(long address,int capacity){
        this.buffer = MemoryUtil.memFloatBuffer(address,capacity*4);
    }


    private Vector4fBuffer(int capacity){
        this.buffer=MemoryUtil.memAllocFloat(capacity*4);
    }

    public static Vector4fBuffer memVector4fBuffer(long address, int capacity){
        Vector4fBuffer buff = new Vector4fBuffer(address,capacity);

        return  buff;
    }
    public static Vector4fBuffer memAllocVector4f(int capacity){
        return new Vector4fBuffer(capacity);
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
    public float w(int index){
        return buffer.get(index*4+3);
    }

    public void putX(int index,float x){
        buffer.put(index*4,x);
    }
    public void putY(int index,float y){
        buffer.put(index*4+1,y);
    }
    public void putZ(int index,float z){
        buffer.put(index*4+2,z);
    }
    public void putW(int index,float w){
        buffer.put(index*4+3,w);
    }



    public void put3(int index, float[] xyz){
        putX(index,xyz[0]);
        putY(index,xyz[1]);
        putZ(index,xyz[2]);
    }

    public void put(int index, float[] xyzw){
        putX(index,xyzw[0]);
        putY(index,xyzw[1]);
        putZ(index,xyzw[2]);
        putW(index,xyzw[3]);
    }

    public void put(int index,Vector3f vector3f){
        putX(index,vector3f.x);
        putY(index,vector3f.y);
        putZ(index,vector3f.z);
    }

    public void put(int index,Vector4f vector4f){
        putX(index,vector4f.x);
        putY(index,vector4f.y);
        putZ(index,vector4f.z);
        putW(index,vector4f.w);
    }

    public Vector3f get3f(int i){
        return new Vector3f(x(i),y(i),z(i));
    }

    public Vector4f get(int i){
        return new Vector4f(x(i),y(i),z(i),w(i));
    }

    public Vector3f[] toVector3f(){
        Vector3f[] vector3fs = new Vector3f[capacity()];
        for (int i = 0; i < vector3fs.length; i++) {
            vector3fs[i]=get3f(i);
        }
        return vector3fs;
    }

    public Vector4f[] toVector4f(){
        Vector4f[] vector4fs = new Vector4f[capacity()];
        for (int i = 0; i < vector4fs.length; i++) {
            vector4fs[i]=get(i);
        }
        return vector4fs;
    }

    public void free(){
        MemoryUtil.memFree(buffer);
    }

    public int capacity(){
        return buffer.capacity()/4;
    }

    public FloatBuffer getBuffer() {
        return buffer;
    }

    @Override
    public long sizeof() {
        return Float.BYTES*buffer.capacity();
    }

    private final FloatBuffer buffer;

    public static final long SIZEOF= Float.BYTES*4;

}
