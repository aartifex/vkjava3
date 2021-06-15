package org.artifex.threading;

import org.artifex.vulkan.CommandBuffer;

public interface CommandRecord
{
    void record(CommandBuffer cmdBuffer);
}
