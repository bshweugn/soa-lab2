package com.example.orgdirectoryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfigurer {

    private static final String JNDI = "jdbc/orgdirectory_service";

    @Bean(destroyMethod = "")
    public DataSource dataSource() throws DataSourceLookupFailureException {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        return dataSourceLookup.getDataSource(JNDI);
    }
}
