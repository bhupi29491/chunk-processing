package com.bhupi.spring_batch.chunkprocessing.config;

import com.bhupi.spring_batch.chunkprocessing.domain.OSProduct;
import com.bhupi.spring_batch.chunkprocessing.domain.Product;
import com.bhupi.spring_batch.chunkprocessing.domain.ProductFieldSetMapper;
import com.bhupi.spring_batch.chunkprocessing.domain.ProductRowMapper;
import com.bhupi.spring_batch.chunkprocessing.exception.MyException;
import com.bhupi.spring_batch.chunkprocessing.listener.MyChunkListener;
import com.bhupi.spring_batch.chunkprocessing.listener.MyItemProcessListener;
import com.bhupi.spring_batch.chunkprocessing.listener.MyItemReadListener;
import com.bhupi.spring_batch.chunkprocessing.listener.MyItemWriteListener;
import com.bhupi.spring_batch.chunkprocessing.listener.MySkipListener;
import com.bhupi.spring_batch.chunkprocessing.processor.FilterProductItemProcessor;
import com.bhupi.spring_batch.chunkprocessing.processor.TransformProductItemProcessor;
import com.bhupi.spring_batch.chunkprocessing.reader.ProductNameItemReader;
import com.bhupi.spring_batch.chunkprocessing.skippolicy.MySkipPolicy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
public class BatchConfig {

    @Autowired
    public DataSource dataSource;

    @Bean
    public MyChunkListener myChunkListener() {
        return new MyChunkListener();
    }

    @Bean
    public MyItemReadListener myItemReadListener() {
        return new MyItemReadListener();
    }

    @Bean
    public MyItemProcessListener myItemProcessListener() {
        return new MyItemProcessListener();
    }

    @Bean
    public MyItemWriteListener myItemWriteListener() {
        return new MyItemWriteListener();
    }

    @Bean
    public MySkipListener mySkipListener() {
        return new MySkipListener();
    }

    @Bean
    public MySkipPolicy mySkipPolicy() {
        return new MySkipPolicy();
    }

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

    @Bean
    public ItemReader<Product> jdbcPagingItemReader() throws Exception {
        JdbcPagingItemReader<Product> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(dataSource);
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setSelectClause("select product_id, product_name, product_category, product_price");
        factoryBean.setFromClause("from product_details");
        factoryBean.setSortKey("product_id");

        itemReader.setQueryProvider(Objects.requireNonNull(factoryBean.getObject()));
        itemReader.setRowMapper(new ProductRowMapper());
        itemReader.setPageSize(3);

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


//    @Bean
//    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("chunkBasedStep1", jobRepository).<Product, Product>chunk(3, transactionManager)
//                                                                .reader(jdbcCursorItemReader())
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
    public ItemWriter<Product> flatFileItemWriter() {
        FlatFileItemWriter<Product> itemWriter = new FlatFileItemWriter<>();
        itemWriter.setResource(new FileSystemResource("src/main/resources/data/Product_Details_Output.csv"));

        DelimitedLineAggregator<Product> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<Product> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{
                "productId",
                "productName",
                "productCategory",
                "productPrice"});

        lineAggregator.setFieldExtractor(fieldExtractor);

        itemWriter.setLineAggregator(lineAggregator);
        return itemWriter;
    }

//    @Bean
//    public JdbcBatchItemWriter<Product> jdbcBatchItemWriter() {
//        JdbcBatchItemWriter<Product> itemWriter = new JdbcBatchItemWriter<>();
//        itemWriter.setDataSource(dataSource);
//        itemWriter.setSql("insert into product_details_output values (?,?,?,?)");
//        itemWriter.setItemPreparedStatementSetter(new ProductItemPreparedStatementSetter());
//        return itemWriter;
//    }

//    @Bean
//    public JdbcBatchItemWriter<Product> jdbcBatchItemWriterWithNamedParameters() {
//        JdbcBatchItemWriter<Product> itemWriter = new JdbcBatchItemWriter<>();
//        itemWriter.setDataSource(dataSource);
//        itemWriter.setSql(
//                "insert into product_details_output values (:productId,:productName,:productCategory,:productPrice)");
//        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//        return itemWriter;
//    }

    @Bean
    public JdbcBatchItemWriter<OSProduct> jdbcBatchItemWriterWithNamedParameters() {
        JdbcBatchItemWriter<OSProduct> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql(
                "insert into os_product_details values (:productId,:productName,:productCategory,:productPrice, :taxPercent, :sku, :shippingRate)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return itemWriter;
    }

    @Bean
    public ItemProcessor<Product, OSProduct> transformProductItemProcessor() {
        return new TransformProductItemProcessor();
    }

    @Bean
    public ItemProcessor<Product, Product> filterProductItemProcessor() {
        return new FilterProductItemProcessor();
    }

//    @Bean
//    public ValidatingItemProcessor<Product> validateProductItemProcessor() {
//        ValidatingItemProcessor<Product> validatingItemProcessor = new ValidatingItemProcessor<>(new ProductValidator());
//        validatingItemProcessor.setFilter(true);
//        return validatingItemProcessor;
//    }

    @Bean
    public BeanValidatingItemProcessor<Product> validateProductItemProcessor() {
        BeanValidatingItemProcessor<Product> beanValidatingItemProcessor = new BeanValidatingItemProcessor<>();
//        beanValidatingItemProcessor.setFilter(true);
        return beanValidatingItemProcessor;
    }

    @Bean
    public CompositeItemProcessor<Product, OSProduct> itemProcessor() {
        CompositeItemProcessor<Product, OSProduct> itemProcessor = new CompositeItemProcessor<>();
        List itemProcessors = new ArrayList();
        itemProcessors.add(validateProductItemProcessor());
        itemProcessors.add(filterProductItemProcessor());
        itemProcessors.add(transformProductItemProcessor());
        itemProcessor.setDelegates(itemProcessors);
        return itemProcessor;
    }


    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("chunkBasedStep1", jobRepository).<Product, OSProduct>chunk(3, transactionManager)
                                                                .reader(flatFileItemReader())
                                                                .processor(itemProcessor())
                                                                .writer(jdbcBatchItemWriterWithNamedParameters())
                                                                .faultTolerant()
//                                                                .skip(ValidationException.class)
//                                                                .skip(FlatFileParseException.class)
//                                                                .skipLimit(3)
//                                                                .skipPolicy(mySkipPolicy())
                                                                .retry(MyException.class)
                                                                .retryLimit(4)
                                                                .listener(mySkipListener())
                                                                .listener(myChunkListener())
//                                                                .listener(myItemReadListener())
//                                                                .listener(myItemProcessListener())
//                                                                .listener(myItemWriteListener())
                                                                .build();
    }

    @Bean
    public Job firstJob(JobRepository jobRepository, Step step1) throws Exception {
        return new JobBuilder("job1", jobRepository).start(step1)
                                                    .build();
    }

}
