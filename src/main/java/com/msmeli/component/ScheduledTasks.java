package com.msmeli.component;

import com.msmeli.feignClient.MeliFeignClient;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Getter
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final MeliFeignClient meliFeignClient;
    private final ModelMapper mapper;


    @Value("${meli.grantType}")
    private String grant_type;

    @Value("${meli.clientId}")
    private String client_id;

    @Value("${meli.clientSecret}")
    private String client_secret;

    private String token;

    public ScheduledTasks(MeliFeignClient meliFeignClient, ModelMapper mapper) {
        this.meliFeignClient = meliFeignClient;
        this.mapper = mapper;
    }


}
