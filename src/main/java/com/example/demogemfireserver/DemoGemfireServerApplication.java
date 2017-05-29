package com.example.demogemfireserver;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.RegionAttributes;
import com.gemstone.gemfire.cache.RegionShortcut;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEvent;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;
import org.springframework.data.gemfire.server.CacheServerFactoryBean;
import org.springframework.data.gemfire.util.ArrayUtils;
import org.springframework.data.gemfire.wan.AsyncEventQueueFactoryBean;

import java.util.List;
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

		return gemfireCache;
	}

	@Bean
	CacheServerFactoryBean gemfireCacheServer(Cache gemfireCache,
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
	PartitionedRegionFactoryBean<Long, Long> factorialsRegion(
			Cache gemfireCache,
			@Qualifier("factorialsRegionAttributes") RegionAttributes<Long, Long> factorialsRegionAttributes)
			throws Exception {

		PartitionedRegionFactoryBean<Long, Long> factorialsRegion = new PartitionedRegionFactoryBean<>();

		factorialsRegion.setAttributes(factorialsRegionAttributes);
		factorialsRegion.setCache(gemfireCache);
		factorialsRegion.setClose(false);
		factorialsRegion.setShortcut(RegionShortcut.PARTITION_REDUNDANT);

		factorialsRegion.setName("Factorials");
		factorialsRegion.setPersistent(false);

		factorialsRegion.setAsyncEventQueues(
		ArrayUtils.asArray(myAsyncEventQueue(gemfireCache).getObject()));

		return factorialsRegion;
	}

//	@Bean
//	DiskStoreFactoryBean myDiskStore(Cache gemfireCache){
//		DiskStoreFactoryBean diskStoreFactoryBean = new DiskStoreFactoryBean();
//		diskStoreFactoryBean.setCache(gemfireCache);
//
//		diskStoreFactoryBean.setBeanName("myDiskStore");
//		DiskStoreFactoryBean.DiskDir dir = new DiskStoreFactoryBean.DiskDir
//				("/Users/derrickwong/Downloads/demo-gemfire-server/", 20);
//		diskStoreFactoryBean.setDiskDirs(Lists.newArrayList(dir));
//
//		return diskStoreFactoryBean;
//	}

	@Bean
	AsyncEventQueueFactoryBean myAsyncEventQueue(Cache gemfireCache){
		AsyncEventQueueFactoryBean asyncEventQueueFactoryBean = new AsyncEventQueueFactoryBean(gemfireCache);

		asyncEventQueueFactoryBean.setParallel(true);
		asyncEventQueueFactoryBean.setBatchSize(10);
		asyncEventQueueFactoryBean.setPersistent(false);
//		asyncEventQueueFactoryBean.setDiskStoreRef("myDiskStore");


		AsyncEventListener asyncEventListener = new AsyncEventListener() {
			@Override
			public boolean processEvents(List<AsyncEvent> list) {
				log.info("processEvent");
				return true;
			}

			@Override
			public void close() {

			}
		};

		asyncEventQueueFactoryBean.setAsyncEventListener(asyncEventListener);

		return asyncEventQueueFactoryBean;
	}

	@Bean
	@SuppressWarnings("unchecked")
	RegionAttributesFactoryBean factorialsRegionAttributes() {
		RegionAttributesFactoryBean factorialsRegionAttributes = new RegionAttributesFactoryBean();

//		factorialsRegionAttributes.setCacheLoader(factorialsCacheLoader());
		factorialsRegionAttributes.setKeyConstraint(Long.class);
		factorialsRegionAttributes.setValueConstraint(Long.class);

		return factorialsRegionAttributes;
	}


	@Bean
	MyFunction myFunction(){
		return new MyFunction();
	}

}
