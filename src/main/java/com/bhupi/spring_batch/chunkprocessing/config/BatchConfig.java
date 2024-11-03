package com.bhupi.spring_batch.chunkprocessing.config;

import com.bhupi.spring_batch.chunkprocessing.domain.Product;
import com.bhupi.spring_batch.chunkprocessing.domain.ProductFieldSetMapper;
import com.bhupi.spring_batch.chunkprocessing.domain.ProductRowMapper;
import com.bhupi.spring_batch.chunkprocessing.reader.ProductNameItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class BatchConfig {

    @Autowired
    public DataSource dataSource;

    @Bean
    public ItemReader<String> itemReader() {
        List<String> productList = new ArrayList<>();
        productList.add("Product 1");
        productList.add("Product 2");
        productList.add("Product 3");
        productList.add("Product 4");
        productList.add("Product 5");
        productList.add("Product 6");
        productList.add("Product 7");
        productList.add("Product 8");
        return new ProductNameItemReader(productList);
    }

    @Bean
    public ItemReader<Product> flatFileItemReader() {
        FlatFileItemReader<Product> itemReader = new FlatFileItemReader<>();
        itemReader.setLinesToSkip(1);
        itemReader.setResource(new ClassPathResource("/data/Product_Details.csv"));

        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("product_id", "product_name", "product_category", "product_price");

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new ProductFieldSetMapper());

        itemReader.setLineMapper(lineMapper);

        return itemReader;
    }


    @Bean
    public ItemReader<Product> jdbcCursorItemReader() {
        JdbcCursorItemReader<Product> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setSql("Select * from product_details order by product_id");
        itemReader.setRowMapper(new ProductRowMapper());
        return itemReader;
    }

//    @Bean
//    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("chunkBasedStep1", jobRepository).<String, String>chunk(3, transactionManager)
//                                                                .reader(itemReader())
//                                                                .writer(new ItemWriter<String>() {
//                                                                    @Override
//                                                                    public void write(Chunk<? extends String> chunk) throws Exception {
//                                                                        System.out.println("Chunk-processing Started");
//                                                                        chunk.forEach(System.out::println);
//                                                                        System.out.println("Chunk-processing Ended");
//                                                                    }
//                                                                })
//                                                                .build();
//    }

//    @Bean
//    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("chunkBasedStep1", jobRepository).<Product, Product>chunk(3, transactionManager)
//                                                                .reader(flatFileItemReader())
//                                                                .writer(new ItemWriter<Product>() {
//                                                                    @Override
//                                                                    public void write(Chunk<? extends Product> chunk) throws Exception {
//                                                                        System.out.println("Chunk-processing Started");
//                                                                        chunk.forEach(System.out::println);
//                                                                        System.out.println("Chunk-processing Ended");
//                                                                    }
//                                                                })
//                                                                .build();
//    }


    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("chunkBasedStep1", jobRepository).<Product, Product>chunk(3, transactionManager)
                                                                .reader(jdbcCursorItemReader())
                                                                .writer(new ItemWriter<Product>() {
                                                                    @Override
                                                                    public void write(Chunk<? extends Product> chunk) throws Exception {
                                                                        System.out.println("Chunk-processing Started");
                                                                        chunk.forEach(System.out::println);
                                                                        System.out.println("Chunk-processing Ended");
                                                                    }
                                                                })
                                                                .build();
    }


    @Bean
    public Job firstJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("job1", jobRepository).start(step1(jobRepository, transactionManager))
                                                    .build();
    }

}
