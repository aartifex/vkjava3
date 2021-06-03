package org.artifex;

import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.glfwVulkanSupported;

public class Window
{
    static{
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        if(!glfwVulkanSupported()){
            throw new IllegalStateException("Device does not support vulkan");
        }
    }

    public Window(String windowTitle){
        this(windowTitle,null);
    }

    public Window(String title, GLFWKeyCallbackI keyCallback){
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if(vidMode==null) throw new RuntimeException("Could not find monitor");
        width= vidMode.width();
        height=vidMode.height();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CLIENT_API,GLFW_NO_API);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_FALSE);

        window = glfwCreateWindow(width,height,title, MemoryUtil.NULL,MemoryUtil.NULL);
        if(window==MemoryUtil.NULL)
            throw new RuntimeException("Failed to create GLFW window");

        glfwSetFramebufferSizeCallback(window,(window,width,height)-> resize(width,height));

        glfwSetKeyCallback(window,(win,key,scancode,action,mods)->{
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
            if (keyCallback != null) {
                keyCallback.invoke(window, key, scancode, action, mods);
            }
        });
        mouseinput = new MouseInput(window);

    }

    public void resize(int width,int height){
        resized=true;
        this.width=width;
        this.height=height;
    }

    public void cleanup(){
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void pollEvents(){
        glfwPollEvents();
    }
    public boolean alive(){
        return !glfwWindowShouldClose(window);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }

    public void resetResized(){
        resized=false;
    }

    public long window(){
        return window;
    }

    public boolean isKeyPressed(int keyCode){
        return glfwGetKey(window,keyCode) == GLFW_PRESS;
    }
    private String title;
    private long window = 0L;
    private int width,height;
    private MouseInput mouseinput;
    private GLFWKeyCallbackI keyCallback;
    private boolean resized;
}
