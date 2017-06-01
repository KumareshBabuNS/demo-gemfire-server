package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.CacheLoader;
import com.gemstone.gemfire.cache.CacheLoaderException;
import com.gemstone.gemfire.cache.LoaderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by derrickwong on 29/5/2017.
 */
@Slf4j
public class ClientHealthCacheLoader
    implements CacheLoader<String, ClientHealthInfo> {

    private ClientHealthInfoRepository clientHealthInfoRepository;

    @Autowired
    public ClientHealthCacheLoader(ClientHealthInfoRepository clientHealthInfoRepository) {
        this.clientHealthInfoRepository = clientHealthInfoRepository;
    }

    @Override
    public ClientHealthInfo load(LoaderHelper<String, ClientHealthInfo> loaderHelper) throws CacheLoaderException {


        String key = loaderHelper.getKey();

        log.debug("load for " + key);

        return clientHealthInfoRepository.findOne(key);

    }

    @Override
    public void close() {
        log.info("close");
    }
}
