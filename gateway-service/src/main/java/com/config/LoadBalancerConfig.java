package com.config;

import java.util.List;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;

@Configuration
public class LoadBalancerConfig {

    @Bean
    public ServiceInstanceListSupplier
    serviceInstanceListSupplier() {

    	return new ServiceInstanceListSupplier() {

    	    @Override
    	    public String getServiceId() {
    	        return "backend-service";
    	    }

    	    @Override
    	    public Flux<List<ServiceInstance>> get() {

    	        return Flux.just(
    	                List.of(

    	                        new DefaultServiceInstance(
    	                                "backend1",
    	                                "backend-service",
    	                                "localhost",
    	                                9091,
    	                                false
    	                        ),

    	                        new DefaultServiceInstance(
    	                                "backend2",
    	                                "backend-service",
    	                                "localhost",
    	                                9092,
    	                                false
    	                        ),

    	                        new DefaultServiceInstance(
    	                                "backend3",
    	                                "backend-service",
    	                                "localhost",
    	                                9093,
    	                                false
    	                        )
    	                )
    	        );
    	    }
    	};
    }
}