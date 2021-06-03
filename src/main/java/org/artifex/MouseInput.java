package org.artifex;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput
{
    Vector2f currentPos;
    Vector2f displaceVec;
    private boolean inWindow;
    private boolean leftButtonPressed;
    private boolean rightButtonPressed;
    private Vector2f previousPos;

    public MouseInput(long window){
        previousPos=new Vector2f(-1,-1);
        currentPos = new Vector2f();
        displaceVec = new Vector2f();
        leftButtonPressed=false;
        rightButtonPressed=false;
        inWindow=false;

        glfwSetCursorPosCallback(window,(handle,xpos,ypos)->{
           currentPos.x = (float) xpos;
           currentPos.y = (float) ypos;
        });
        glfwSetCursorEnterCallback(window,(handle,entered)->{
            inWindow= entered;
        });
        glfwSetMouseButtonCallback(window,(handle,button,action,mode)->{
            leftButtonPressed= (button == GLFW_MOUSE_BUTTON_1) && action == GLFW_PRESS;
            rightButtonPressed= (button == GLFW_MOUSE_BUTTON_2) && action == GLFW_PRESS;
        });
    }


    public void input(){
        displaceVec.x=0;
        displaceVec.y=0;
        if(previousPos.x>0&&previousPos.y>0&&inWindow){
            double dx = currentPos.x-previousPos.x;
            double dy = currentPos.y-previousPos.y;
            boolean rotateX = dx !=0;
            boolean rotateY = dy != 0;
            if(rotateX) displaceVec.y=(float)dx;
            if(rotateY) displaceVec.x=(float)dy;
        }
        previousPos.x=currentPos.x;
        previousPos.y=currentPos.y;
    }
    public Vector2f getDisplaceVec() {
        return displaceVec;
    }
    public Vector2f getCurrentPos() {
        return currentPos;
    }
    public Vector2f getPreviousPos() {
        return previousPos;
    }
    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
