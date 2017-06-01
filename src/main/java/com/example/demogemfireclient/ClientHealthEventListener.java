package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.asyncqueue.AsyncEvent;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventListener;
import com.gemstone.gemfire.pdx.PdxInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by derrickwong on 29/5/2017.
 */
@Slf4j
public class ClientHealthEventListener implements AsyncEventListener {

    private ClientHealthInfoRepository clientHealthInfoRepository;

    @Autowired
    public ClientHealthEventListener(ClientHealthInfoRepository clientHealthInfoRepository){
        this.clientHealthInfoRepository=clientHealthInfoRepository;
    }

    @Override
    public boolean processEvents(List<AsyncEvent> list) {

        List<ClientHealthInfo> healthEvents =
                list.stream()
                        .filter(ae -> ae.getOperation().isCreate() || ae.getOperation().isUpdate())
                        .map(ae -> (ClientHealthInfo)((PdxInstance)ae.getDeserializedValue()).getObject())
                        .collect(Collectors.toList());
        if(healthEvents!=null && healthEvents.size()>0)
            clientHealthInfoRepository.save(healthEvents);

        log.info("Saved " + healthEvents.size() + " of client health info");

        return true;

    }

    @Override
    public void close() {
        log.info("close");
    }
}
