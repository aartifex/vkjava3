package org.artifex.util;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Struct;
import org.lwjgl.system.StructBuffer;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.system.MemoryUtil.memAllocLong;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public class Pointers
{
    //null ptr is a pointer size 1
    public static final PointerBuffer nullptr = PointerBuffer.allocateDirect(1).
            put(memUTF8("\0")).position(0);

    /*
            STRING POINTERS
     */

    public static PointerBuffer pString(List<String> strings){
        PointerBuffer pStrings = MemoryUtil.memAllocPointer(strings.size());
        if(strings.size()==0) pStrings = nullptr;
        for (String string : strings) {
            pStrings.put(MemoryUtil.memUTF8(string+"\0"));
        }
        return pStrings.flip();
    }
    public static PointerBuffer pString(String...strings){
        return pString(List.of(strings));
    }

    public static List<String> derefString(PointerBuffer pString){
        List<String> strings = new ArrayList<>(pString.capacity());
        for(int i=pString.capacity()-1;i>=0;--i){
            strings.add(MemoryUtil.memUTF8(pString.get(i)));
        }
        return strings;
    }

    /*
            END STRING POINTERS
     */


    public static List<Integer> BufferToList(IntBuffer buff){
        List<Integer> res = new Vector<>();
        for (int i = 0; i <buff.capacity() ; i++) {
            res.add(buff.get(i));
        }
        return res;
    }
    public static List<Long> BufferToList(LongBuffer buff){
        List<Long> res = new Vector<>();
        for (int i = 0; i <buff.capacity() ; i++) {
            res.add(buff.get(i));
        }
        return res;
    }
    public static LongBuffer listToBuffer(List<Long> list){
        LongBuffer buffer = memAllocLong(list.size());
        for (Long l : list) {
            buffer.put(l);
        }
        return buffer.flip();
    }
    /*
        OTHER POINTERS ?
     */

    public static <C extends Struct,B extends StructBuffer<C,B>> List<C> structBufferToList(B buffer){
        List<C> result = new ArrayList<>(buffer.capacity());
        for(int i=0;i<buffer.capacity();i++){
            result.add(buffer.get(i));
        }
        return result;
    }


}
