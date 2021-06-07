package org.artifex.math;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Vector4fBuffer
{

    private Vector4fBuffer(long address,int capacity){
        this.buffer = MemoryUtil.memFloatBuffer(address,capacity*4);
    }


    public static Vector4fBuffer memVector4fBuffer(long address, int capacity){
        Vector4fBuffer buff = new Vector4fBuffer(address,capacity);

        return  buff;
    }


    public void put3(float[] xyz){

    }

    public void put(float[] xyzw){

    }

    public void put3(int index, float[] xyz){

    }

    public void put(int index, float[] xyzw){

    }

    public void put(Vector3f vector3f){

    }

    public void put(Vector4f vector4f){

    }

    public Vector3f get3f(int i){
        return null;
    }

    public Vector4f get(int index){
        return null;
    }

    public Vector3f[] toVector3f(){
        return null;
    }

    public Vector4f[] toVector4f(){
        return null;
    }

    public void free(){
        buffer.clear();
    }
    private final FloatBuffer buffer;

}
