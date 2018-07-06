package io.nambm.buildhabit.configuration;

import io.nambm.buildhabit.table.BlobService;
import io.nambm.buildhabit.table.impl.BlobServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfiguration {

    @Bean
    public BlobService getBlobService() {
        return new BlobServiceImpl();
    }
}
