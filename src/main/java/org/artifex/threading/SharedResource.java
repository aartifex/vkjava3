package org.artifex.threading;

public class SharedResource<R>
{
    private final R  resource;


    public SharedResource(R resource){
        this.resource=resource;
    }

    public R getResource() {
        return resource;
    }
}
