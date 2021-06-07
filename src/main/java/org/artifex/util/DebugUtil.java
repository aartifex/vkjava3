package org.artifex.util;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.lwjgl.vulkan.VkDebugUtilsMessengerCallbackDataEXT;
import org.lwjgl.vulkan.VkDebugUtilsMessengerCallbackEXT;
import org.lwjgl.vulkan.VkDebugUtilsMessengerCreateInfoEXT;

import static org.lwjgl.vulkan.EXTDebugUtils.*;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK11.VK_FALSE;

public class DebugUtil
{

    public static final int SEVERITY_ERROR =VK_DEBUG_UTILS_MESSAGE_SEVERITY_ERROR_BIT_EXT;
    public static final int SEVERITY_VERBOSE = VK_DEBUG_UTILS_MESSAGE_SEVERITY_VERBOSE_BIT_EXT;
    public static final int SEVERITY_WARNING = VK_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT;
    public static final int SEVERITY_INFO = VK_DEBUG_UTILS_MESSAGE_SEVERITY_INFO_BIT_EXT;

    public static final int TYPE_GENERAL = VK_DEBUG_UTILS_MESSAGE_TYPE_GENERAL_BIT_EXT;
    public static final int TYPE_VALIDATION = VK_DEBUG_UTILS_MESSAGE_TYPE_VALIDATION_BIT_EXT;
    public static final int TYPE_PERFORMANCE = VK_DEBUG_UTILS_MESSAGE_TYPE_PERFORMANCE_BIT_EXT;

    private static Logger LOGGER = LogManager.getLogger(DebugUtil.class);

    public static VkDebugMessenger defaultMessenger = new VkDebugMessenger() {
        @Override
        public int invoke(int severity, int m_type, long data, long userdat) {
                VkDebugUtilsMessengerCallbackDataEXT callbackData = VkDebugUtilsMessengerCallbackDataEXT.create(data);
                Level logLevel = Level.DEBUG;
                if ((severity & VK_DEBUG_UTILS_MESSAGE_SEVERITY_INFO_BIT_EXT) != 0) {
                    logLevel = Level.INFO;
                } else if ((severity & VK_DEBUG_UTILS_MESSAGE_SEVERITY_WARNING_BIT_EXT) != 0) {
                    logLevel = Level.WARN;
                } else if ((severity & VK_DEBUG_UTILS_MESSAGE_SEVERITY_ERROR_BIT_EXT) != 0) {
                    logLevel = Level.ERROR;
                }

                if(severity> VK_DEBUG_UTILS_MESSAGE_SEVERITY_INFO_BIT_EXT)LOGGER.log(logLevel, "VkDebugUtilsCallback, " +  callbackData.pMessageString());
                return VK_FALSE;
            }

    };


    //Simply to give variables a more meaningful name and add utility
    public static abstract class VkDebugMessenger extends VkDebugUtilsMessengerCallbackEXT {

        public abstract int invoke(int severity, int m_type, long data, long userdat);
        protected VkDebugUtilsMessengerCallbackDataEXT getData(long data_addr){
            return VkDebugUtilsMessengerCallbackDataEXT.create(data_addr);
        }
    }

    public static VkDebugUtilsMessengerCreateInfoEXT getDebugUtilsInfo(VkDebugUtilsMessengerCallbackEXT messenger){
        VkDebugUtilsMessengerCreateInfoEXT debugInfo = VkDebugUtilsMessengerCreateInfoEXT.calloc()
                                                                                         .sType(VK_STRUCTURE_TYPE_DEBUG_UTILS_MESSENGER_CREATE_INFO_EXT)
                                                                                         .messageSeverity(SEVERITY_ERROR | SEVERITY_INFO
                                                                                                 | SEVERITY_VERBOSE | SEVERITY_WARNING)
                                                                                         .messageType(TYPE_GENERAL | TYPE_VALIDATION | TYPE_PERFORMANCE)
                                                                                         .pfnUserCallback(messenger);
        return debugInfo;
    }


    public static void vkCheck(int code, String errMsg){
        if(code!=VK_SUCCESS){
            throw new RuntimeException(errMsg + ", code: " + code);
        }
    }
}
