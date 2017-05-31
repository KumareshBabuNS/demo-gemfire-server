package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.RegionShortcut;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventQueue;
import feign.Feign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;
import org.springframework.data.gemfire.server.CacheServerFactoryBean;
import org.springframework.data.gemfire.util.ArrayUtils;
import org.springframework.data.gemfire.wan.AsyncEventQueueFactoryBean;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import java.util.Properties;

@Slf4j
@EnableGemfireFunctions
@EnableFeignClients(clients = ClientHealthService.class)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringBootApplication
public class DemoGemfireServerApplication {

	static final boolean DEFAULT_AUTO_STARTUP = true;

	public static void main(String[] args) {
		SpringApplication.run(DemoGemfireServerApplication.class, args);
	}

	@Bean
	ClientHealthService clientHealthService(){
		return Feign.builder()
				.contract(new SpringMvcContract())
				.target(ClientHealthService.class, "localhost:8090");
	}

	@Bean
	Properties gemfireProperties(
			@Value("${gemfire.server.name:DefaultGemfireServer}") String serverName,
			@Value("${gemfire.log.level:config}") String logLevel,
			@Value("${gemfire.locator.host-port:localhost[10334]}") String locatorHostPort,
			@Value("${gemfire.manager.port:1098}") String managerPort) {

		Properties gemfireProperties = new Properties();
		gemfireProperties.setProperty("name", serverName);
//		gemfireProperties.setProperty("mcast-port", "0");
		gemfireProperties.setProperty("log-level", logLevel);
		gemfireProperties.setProperty("locators", locatorHostPort);
		gemfireProperties.setProperty("jmx-manager", "true");
		gemfireProperties.setProperty("jmx-manager-port", managerPort);
		gemfireProperties.setProperty("jmx-manager-start", "true");

		return gemfireProperties;
	}

	@Bean
	CacheFactoryBean gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties) {
		CacheFactoryBean gemfireCache = new CacheFactoryBean();
		gemfireCache.setClose(true);
		gemfireCache.setProperties(gemfireProperties);
		gemfireCache.setPdxReadSerialized(true);
		return gemfireCache;
	}

	@Bean
	CacheServerFactoryBean gemfireCacheServer(
			Cache gemfireCache,
		  	@Value("${gemfire.cache.server.bind-address:localhost}") String bindAddress,
		  	@Value("${gemfire.cache.server.hostname-for-clients:localhost}") String hostNameForClients,
		  	@Value("${gemfire.cache.server.port:40404}") int port) {

		CacheServerFactoryBean gemfireCacheServer = new CacheServerFactoryBean();
		gemfireCacheServer.setCache(gemfireCache);
		gemfireCacheServer.setAutoStartup(DEFAULT_AUTO_STARTUP);
		gemfireCacheServer.setBindAddress(bindAddress);
		gemfireCacheServer.setHostNameForClients(hostNameForClients);
		gemfireCacheServer.setPort(port);
		return gemfireCacheServer;
	}

	@Bean
	PartitionedRegionFactoryBean<String, ClientHealthInfo> clientHealthRegion(
			Cache gemfireCache
//			, ClientHealthInfoRepository clientHealthInfoRepository
			, ClientHealthService clientHealthService, AsyncEventQueue myAsyncEventQueue

	) throws Exception{
		PartitionedRegionFactoryBean<String, ClientHealthInfo> clientHealthRegion = new PartitionedRegionFactoryBean();
		clientHealthRegion.setCache(gemfireCache);
		clientHealthRegion.setClose(false);
		clientHealthRegion.setShortcut(RegionShortcut.PARTITION_REDUNDANT);
		clientHealthRegion.setName("ClientHealth");
		clientHealthRegion.setPersistent(false);
		clientHealthRegion.setAsyncEventQueues(ArrayUtils.asArray(myAsyncEventQueue));
		clientHealthRegion.setCacheLoader(new ClientHealthCacheLoaderFeign(clientHealthService));

		return clientHealthRegion;
	}


	@Bean
	PartitionedRegionFactoryBean<String, ClientHealthInfo> clientHealthRegion2(
			Cache gemfireCache, ClientHealthInfoRepository clientHealthInfoRepository, AsyncEventQueue myAsyncEventQueue2
	) throws Exception{
		PartitionedRegionFactoryBean<String, ClientHealthInfo> clientHealthRegion = new PartitionedRegionFactoryBean();
		clientHealthRegion.setCache(gemfireCache);
		clientHealthRegion.setClose(false);
		clientHealthRegion.setShortcut(RegionShortcut.PARTITION_REDUNDANT);
		clientHealthRegion.setName("ClientHealth2");
		clientHealthRegion.setPersistent(false);
		clientHealthRegion.setAsyncEventQueues(ArrayUtils.asArray(myAsyncEventQueue2));
		clientHealthRegion.setCacheLoader(new ClientHealthCacheLoaderDb(clientHealthInfoRepository));

		return clientHealthRegion;
	}


	@Value("${gemfire.async-event.batch-size:100}") int asyncBatchSize;
	@Value("${gemfire.async-event.batch-time-interval:10000}") int asyncBatchTimeInterval;

	@Bean
	AsyncEventQueueFactoryBean myAsyncEventQueue(Cache gemfireCache, ClientHealthService clientHealthService){

		AsyncEventQueueFactoryBean asyncEventQueueFactoryBean = new AsyncEventQueueFactoryBean(gemfireCache);

		asyncEventQueueFactoryBean.setParallel(true);
		asyncEventQueueFactoryBean.setBatchSize(asyncBatchSize);
		asyncEventQueueFactoryBean.setPersistent(false);

		asyncEventQueueFactoryBean.setBatchTimeInterval(asyncBatchTimeInterval);
		asyncEventQueueFactoryBean.setAsyncEventListener(new ClientHealthEventListenerFeign(clientHealthService));

		return asyncEventQueueFactoryBean;
	}


	@Bean
	AsyncEventQueueFactoryBean myAsyncEventQueue2(Cache gemfireCache, ClientHealthInfoRepository clientHealthInfoRepository){

		AsyncEventQueueFactoryBean asyncEventQueueFactoryBean2 = new AsyncEventQueueFactoryBean(gemfireCache);

		asyncEventQueueFactoryBean2.setParallel(true);
		asyncEventQueueFactoryBean2.setBatchSize(asyncBatchSize);
		asyncEventQueueFactoryBean2.setPersistent(false);

		asyncEventQueueFactoryBean2.setBatchTimeInterval(asyncBatchTimeInterval);
		asyncEventQueueFactoryBean2.setAsyncEventListener(new ClientHealthEventListenerDb(clientHealthInfoRepository));

		return asyncEventQueueFactoryBean2;
	}

}
