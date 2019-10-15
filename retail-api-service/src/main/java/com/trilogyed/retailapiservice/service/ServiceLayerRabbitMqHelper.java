package com.trilogyed.retailapiservice.service;

import com.insomnyak.util.MapClasses;
import com.trilogyed.queue.shared.viewmodel.LevelUpViewModel;
import com.trilogyed.retailapiservice.exception.QueueRequestTimeoutException;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class ServiceLayerRabbitMqHelper {
    private static final String EXCHANGE = "level-up-exchange";
    private static final String ROUTING_KEY_ADD = "level-up.create.retail-api";
    private static final String ROUTING_KEY_UPDATE = "level-up.update.retail-api";
    private static final String ROUTING_KEY_UPDATE_FALLBACK = "level-up.update-fallback.retail-api";
    private static final String ROUTING_KEY_CONSOLIDATE_CUSTOMER_ID = "level-up.consolidate-customerId.retail-api";
    private static final Long TIMEOUT = 5L;
    private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;

    private RabbitTemplate rabbitTemplate;
    private AsyncRabbitTemplate asyncRabbitTemplate;

    @Autowired
    public ServiceLayerRabbitMqHelper(RabbitTemplate rabbitTemplate,
                                      AsyncRabbitTemplate asyncRabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.asyncRabbitTemplate = asyncRabbitTemplate;
    }

    @Transactional
    public LevelUpViewModel saveLevelUp(LevelUpViewModel luvm) {
        try {
            luvm.setMemberDate(null);
            LevelUpViewModel luvm2 = (LevelUpViewModel) asyncRabbitTemplate
                    .convertSendAndReceiveAsType(EXCHANGE, ROUTING_KEY_ADD, luvm,
                            ParameterizedTypeReference.forType(LevelUpViewModel.class))
                    .get(TIMEOUT, TIMEOUT_UNIT);
            (new MapClasses<>(luvm2, luvm)).mapFirstToSecond(false, true);
            return luvm;
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new QueueRequestTimeoutException(e.getCause() + " \n " + e.getMessage());
        }
    }

    LevelUpViewModel consolidateLevelUpsByCustomerId(Integer customerId) {
        try {
            return (LevelUpViewModel) asyncRabbitTemplate.convertSendAndReceiveAsType(
                    EXCHANGE, ROUTING_KEY_CONSOLIDATE_CUSTOMER_ID, customerId,
                    ParameterizedTypeReference.forType(LevelUpViewModel.class))
                    .get(TIMEOUT, TIMEOUT_UNIT);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new QueueRequestTimeoutException(e.getCause() + " \n " + e.getMessage());
        }
    }

    public void updateLevelUp(LevelUpViewModel luvm) {
        new Thread(() -> rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_UPDATE, luvm)).start();
    }

    public void updateLevelUpFallback(LevelUpViewModel luvm) {
        new Thread(() -> rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_UPDATE_FALLBACK, luvm)).start();
    }
}
