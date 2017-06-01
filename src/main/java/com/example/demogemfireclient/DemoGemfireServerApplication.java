package com.example.demogemfireclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;

@Slf4j
@EnableGemfireFunctions
@ImportResource("spring-gemfire-cache-server-context.xml")
@SpringBootApplication
public class DemoGemfireServerApplication {

	static final boolean DEFAULT_AUTO_STARTUP = true;

	public static void main(String[] args) {
		SpringApplication.run(DemoGemfireServerApplication.class, args);
	}
//
//	@Bean
//	Properties gemfireProperties(
//			@Value("${gemfire.server.name:DefaultGemfireServer}") String serverName,
//			@Value("${gemfire.log.level:config}") String logLevel,
//			@Value("${gemfire.locator.host-port:localhost[10334]}") String locatorHostPort,
//			@Value("${gemfire.manager.port:1098}") String managerPort) {
//
//		Properties gemfireProperties = new Properties();
//		gemfireProperties.setProperty("name", serverName);
//		gemfireProperties.setProperty("log-level", logLevel);
//		gemfireProperties.setProperty("locators", locatorHostPort);
//		gemfireProperties.setProperty("jmx-manager", "true");
//		gemfireProperties.setProperty("jmx-manager-port", managerPort);
//		gemfireProperties.setProperty("jmx-manager-start", "true");
//
//		return gemfireProperties;
//	}
//
//	@Bean
//	CacheFactoryBean gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties) {
//		CacheFactoryBean gemfireCache = new CacheFactoryBean();
//		gemfireCache.setClose(true);
//		gemfireCache.setProperties(gemfireProperties);
//		gemfireCache.setPdxReadSerialized(true);
//		gemfireCache.setPdxDiskStoreName("pdxDiskStore");
//		gemfireCache.setPdxPersistent(true);
//		return gemfireCache;
//	}
//
//	@Bean
//	CacheServerFactoryBean gemfireCacheServer(
//			Cache gemfireCache,
//		  	@Value("${gemfire.cache.server.bind-address:localhost}") String bindAddress,
//		  	@Value("${gemfire.cache.server.hostname-for-clients:localhost}") String hostNameForClients,
//		  	@Value("${gemfire.cache.server.port:40404}") int port) {
//
//		CacheServerFactoryBean gemfireCacheServer = new CacheServerFactoryBean();
//		gemfireCacheServer.setCache(gemfireCache);
//		gemfireCacheServer.setAutoStartup(DEFAULT_AUTO_STARTUP);
//		gemfireCacheServer.setBindAddress(bindAddress);
//		gemfireCacheServer.setHostNameForClients(hostNameForClients);
//		gemfireCacheServer.setPort(port);
//		return gemfireCacheServer;
//	}
//
//
//
//	@Bean
//	PartitionedRegionFactoryBean<String, ClientHealthInfo> clientHealthRegion(
//			Cache gemfireCache, ClientHealthInfoRepository clientHealthInfoRepository, AsyncEventQueue myAsyncEventQueue) throws Exception{
//		PartitionedRegionFactoryBean<String, ClientHealthInfo> clientHealthRegion = new PartitionedRegionFactoryBean();
//		clientHealthRegion.setCache(gemfireCache);
//		clientHealthRegion.setClose(false);
//		clientHealthRegion.setShortcut(RegionShortcut.PARTITION_REDUNDANT);
//		clientHealthRegion.setName("ClientHealth");
//		clientHealthRegion.setPersistent(false);
//		clientHealthRegion.setAsyncEventQueues(ArrayUtils.asArray(myAsyncEventQueue));
//		clientHealthRegion.setCacheLoader(new ClientHealthCacheLoader(clientHealthInfoRepository));
//
//		return clientHealthRegion;
//	}
//
//
//	@Value("${gemfire.async-event.batch-size:100}") int asyncBatchSize;
//	@Value("${gemfire.async-event.batch-time-interval:10000}") int asyncBatchTimeInterval;
//	@Value("${gemfire.async-event.dispatcher-thread:5}") int asyncDispatcherThread;
//	@Value("${gemfire.async-event.max-queue-memory:100}") int maxQueueMemory;
//
//
//	@Bean
//	AsyncEventQueueFactoryBean myAsyncEventQueue(Cache gemfireCache, ClientHealthInfoRepository clientHealthInfoRepository){
//
//		AsyncEventQueueFactoryBean asyncEventQueueFactoryBean = new AsyncEventQueueFactoryBean(gemfireCache);
//
//		asyncEventQueueFactoryBean.setParallel(true);
//		asyncEventQueueFactoryBean.setBatchSize(asyncBatchSize);
//		asyncEventQueueFactoryBean.setPersistent(false);
//		asyncEventQueueFactoryBean.setDispatcherThreads(asyncDispatcherThread);
//		asyncEventQueueFactoryBean.setMaximumQueueMemory(maxQueueMemory);
//		asyncEventQueueFactoryBean.setBatchTimeInterval(asyncBatchTimeInterval);
//		asyncEventQueueFactoryBean.setAsyncEventListener(new ClientHealthEventListener(clientHealthInfoRepository));
//
//		return asyncEventQueueFactoryBean;
//	}
//
//	@Bean
//	DiskStoreFactoryBean pdxDiskStore(Cache gemfireCache, @Value("${gemfire.pdx.disk-store-dir}") String pdxDir){
//		DiskStoreFactoryBean pdxDiskStore = new DiskStoreFactoryBean();
//		pdxDiskStore.setBeanName("pdxDiskStore");
//		DiskStoreFactoryBean.DiskDir diskDir = new DiskStoreFactoryBean.DiskDir(pdxDir);
//		pdxDiskStore.setDiskDirs(Arrays.asList(diskDir));
//		pdxDiskStore.setCache(gemfireCache);
//
//		return pdxDiskStore;
//	}

}
