/*
 * Copyright (c) 2026 Adam Chen
 */

package com.adam.app.galaga.engine.handler;

/**
 * Base handler for the spawning Chain of Responsibility.
 */
public abstract class SpawnHandler {
    protected SpawnHandler mNext;

    public void setNext(SpawnHandler next) {
        this.mNext = next;
    }

    /**
     * Handles the spawn request.
     * @return true if a bee should be spawned.
     */
    public abstract boolean handle(SpawnContext context);
}
