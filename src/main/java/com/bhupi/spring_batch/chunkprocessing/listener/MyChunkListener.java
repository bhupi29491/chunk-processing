package com.bhupi.spring_batch.chunkprocessing.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class MyChunkListener implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext context) {
        System.out.println("beforeChunk() executed");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        System.out.println("afterChunk() executed");
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        System.out.println("afterChunkError() executed");
    }
}
