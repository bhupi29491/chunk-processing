package com.bhupi.spring_batch.chunkprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChunkProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChunkProcessingApplication.class, args);
        System.out.println("This is a Spring Batch Chunk Processing application..!!");
    }

}
