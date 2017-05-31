package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.CacheLoader;
import com.gemstone.gemfire.cache.CacheLoaderException;
import com.gemstone.gemfire.cache.LoaderHelper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Resource;

/**
 * Created by derrickwong on 29/5/2017.
 */
@Slf4j
public class ClientHealthCacheLoaderFeign implements CacheLoader<String, ClientHealthInfo>{

    private ClientHealthService clientHealthService;

    public ClientHealthCacheLoaderFeign(ClientHealthService clientHealthService){
        this.clientHealthService=clientHealthService;
    }

    @Override
    public ClientHealthInfo load(LoaderHelper<String, ClientHealthInfo> loaderHelper) throws CacheLoaderException {


        String key = loaderHelper.getKey();

        log.debug("load for " + key);

//        ClientHealthInfo info = clientHealthInfoRepository.findOne(key);

        try {
            Resource<ClientHealthInfo> clientHealthInfoResource = clientHealthService.getClientHealthInfo(key);
            if(clientHealthInfoResource!=null){
                ClientHealthInfo clientHealthInfo = clientHealthInfoResource.getContent();
                clientHealthInfo.setAccountId(key);
                return clientHealthInfo;
            }
        }catch (FeignException e){
            log.error(e.getMessage());
            return null;
        }

        return null;

    }

    @Override
    public void close() {
        log.info("close");
    }
}
