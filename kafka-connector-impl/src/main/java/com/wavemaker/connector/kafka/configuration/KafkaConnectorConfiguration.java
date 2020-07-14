package com.wavemaker.connector.kafka.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.wavemaker.runtime.connector.configuration.ConnectorConfigurationBase;

@Configuration
@ComponentScan(basePackages = "com.wavemaker.connector.kafka")
public class KafkaConnectorConfiguration extends ConnectorConfigurationBase {


    /**
     * Connector default properties files are load in super class.If you
     * have additional properties file, specify in this api and they will be loaded in super class.
     * @return
     */
    @Override
    public List<Resource> getClasspathPropertyResources() {
        return new ArrayList<>();
    }
}