package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.RegionShortcut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;
import org.springframework.data.gemfire.server.CacheServerFactoryBean;
import org.springframework.data.gemfire.util.ArrayUtils;
import org.springframework.data.gemfire.wan.AsyncEventQueueFactoryBean;

import java.util.Properties;

@Slf4j
@EnableGemfireFunctions
@SpringBootApplication
public class DemoGemfireServerApplication {

	static final boolean DEFAULT_AUTO_STARTUP = true;

	public static void main(String[] args) {
		SpringApplication.run(DemoGemfireServerApplication.class, args);
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
	PartitionedRegionFactoryBean<String, Client> clientRegion(
			Cache gemfireCache, ClientRepository clientRepository) throws Exception{
		PartitionedRegionFactoryBean<String, Client> clientRegion = new PartitionedRegionFactoryBean();
		clientRegion.setCache(gemfireCache);
		clientRegion.setClose(false);
		clientRegion.setShortcut(RegionShortcut.PARTITION_REDUNDANT);
		clientRegion.setName("Client");
		clientRegion.setPersistent(false);
		clientRegion.setCacheLoader(new ClientCacheLoader(clientRepository));

		return clientRegion;
	}

	@Bean
	PartitionedRegionFactoryBean<String, ClientHealthInfo> clientHealthRegion(
			Cache gemfireCache, ClientHealthInfoRepository clientHealthInfoRepository) throws Exception{
		PartitionedRegionFactoryBean<String, ClientHealthInfo> clientHealthRegion = new PartitionedRegionFactoryBean();
		clientHealthRegion.setCache(gemfireCache);
		clientHealthRegion.setClose(false);
		clientHealthRegion.setShortcut(RegionShortcut.PARTITION_REDUNDANT);
		clientHealthRegion.setName("ClientHealth");
		clientHealthRegion.setPersistent(false);
		clientHealthRegion.setAsyncEventQueues(
				ArrayUtils.asArray(myAsyncEventQueue(gemfireCache, clientHealthInfoRepository).getObject()));
		clientHealthRegion.setCacheLoader(new ClientHealthCacheLoader(clientHealthInfoRepository));

		return clientHealthRegion;
	}


	@Value("${gemfire.async-event.batch-size:100}") int asyncBatchSize;
	@Value("${gemfire.async-event.batch-time-interval:10000}") int asyncBatchTimeInterval;

	@Bean
	AsyncEventQueueFactoryBean myAsyncEventQueue(Cache gemfireCache, ClientHealthInfoRepository clientHealthInfoRepository){

		AsyncEventQueueFactoryBean asyncEventQueueFactoryBean = new AsyncEventQueueFactoryBean(gemfireCache);

		asyncEventQueueFactoryBean.setParallel(true);
		asyncEventQueueFactoryBean.setBatchSize(asyncBatchSize);
		asyncEventQueueFactoryBean.setPersistent(false);

		asyncEventQueueFactoryBean.setBatchTimeInterval(asyncBatchTimeInterval);
		asyncEventQueueFactoryBean.setAsyncEventListener(new ClientHealthEventListener(clientHealthInfoRepository));

		return asyncEventQueueFactoryBean;
	}

}
