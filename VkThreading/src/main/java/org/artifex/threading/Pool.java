package org.artifex.threading;

public class Pool extends ThreadGroup {
    public Pool(String name) {
        super(name);
    }

    public Pool(ThreadGroup parent, String name) {
        super(parent, name);
    }



}
