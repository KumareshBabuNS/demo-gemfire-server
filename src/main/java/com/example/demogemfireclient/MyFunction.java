package com.example.demogemfireclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by derrickwong on 28/5/2017.
 */
@Slf4j
@Component
public class MyFunction{

    @GemfireFunction(HA = true)
    public String function1(){

        String result = UUID.randomUUID().toString();
        log.info("function1 execute " + result);
        return result;
    }
}