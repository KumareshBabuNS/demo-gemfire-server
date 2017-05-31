package com.example.demogemfireclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by derrickwong on 31/5/2017.
 */
@FeignClient(name="ClientHealthService",
        //url = "https://moverestrepo.apps.eas.pcf.manulife.com")
        url = "localhost:8090")
public interface ClientHealthService {

    @GetMapping("/clientHealthInfoes/{accountId}")
    Resource<ClientHealthInfo> getClientHealthInfo(@PathVariable("accountId") String accountId);

    @PostMapping("/clientHealthInfoes")
    void postClientHealthInfo(@RequestBody ClientHealthInfo clientHealthInfo);
}
