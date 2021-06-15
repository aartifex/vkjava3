package org.artifex.vulkan;

public class Pipeline
{


    public long getPipeline() {
        return pipeline;
    }

    public long getPipelineLayout() {
        return pipelineLayout;
    }

    public int getPipelineBindPoint() {
        return pipelineBindPoint;
    }

    protected long pipelineLayout;
    protected long pipeline;
    protected int pipelineBindPoint;
}
