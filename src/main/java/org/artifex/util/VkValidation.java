package org.artifex.util;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkLayerProperties;
import util.Pointers;

import java.nio.IntBuffer;
import java.util.List;
import java.util.UUID;

import static org.lwjgl.vulkan.VK11.vkEnumerateInstanceLayerProperties;

public class VkValidation
{

    public static boolean enableValidationLayers = true;
    public static final String VALIDATION_LAYER;
    public static final UUID uuid = UUID.randomUUID();


    public static PointerBuffer ppValidationLayers(MemoryStack stack){
        return stack.mallocPointer(1)
                    .put(0,stack.UTF8(VALIDATION_LAYER));
    }

    public static List<String> validationLayers(){
        if(VALIDATION_LAYER.equals("")) return List.of();
        return List.of(VALIDATION_LAYER);
    }
    public static boolean layersAvailable(){return !VALIDATION_LAYER.equals("");}
    //resolve proper validation layer
    static{
        try(MemoryStack stack = MemoryStack.stackPush()) {

            IntBuffer count = stack.mallocInt(1);
            vkEnumerateInstanceLayerProperties(count,null);
            VkLayerProperties.Buffer layerProperties = VkLayerProperties.callocStack(count.get(0),stack);
            vkEnumerateInstanceLayerProperties(count,layerProperties);
            String name="";
            for (VkLayerProperties properties : Pointers.structBufferToList(layerProperties)) {
                System.out.println(properties.layerNameString());
                if(properties.layerNameString().equals("VK_LAYER_LUNARG_standard_validation")) {
                    name = "VK_LAYER_LUNARG_standard_validation";
                    break;
                }else if(properties.layerNameString().equals("VK_LAYER_KHRONOS_validation")){
                    name = "VK_LAYER_KHRONOS_validation";
                    break;
                }

            }
            VALIDATION_LAYER = name;
        }
    }
}
