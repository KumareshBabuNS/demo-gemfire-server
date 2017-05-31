package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.asyncqueue.AsyncEvent;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventListener;
import com.gemstone.gemfire.pdx.PdxInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by derrickwong on 29/5/2017.
 */
@Slf4j
public class ClientHealthEventListenerFeign implements AsyncEventListener{

    private ClientHealthService clientHealthService;

    public ClientHealthEventListenerFeign(ClientHealthService clientHealthService){
        this.clientHealthService=clientHealthService;
    }

    @Override
    public boolean processEvents(List<AsyncEvent> list) {

        try {
            list.forEach(ae -> clientHealthService.postClientHealthInfo((ClientHealthInfo) ((PdxInstance) ae.getDeserializedValue()).getObject()));
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
        return true;

    }

    @Override
    public void close() {
        log.info("close");
    }
}
