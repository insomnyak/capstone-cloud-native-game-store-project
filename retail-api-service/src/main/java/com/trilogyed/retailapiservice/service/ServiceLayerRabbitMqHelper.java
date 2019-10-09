package com.trilogyed.retailapiservice.service;

import com.insomnyak.util.MapClasses;
import com.trilogyed.queue.shared.viewmodel.LevelUpViewModel;
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
    protected static final String EXCHANGE = "level-up-exchange";
    protected static final String ROUTING_KEY_ADD = "level-up.create.retail.service";
    protected static final String ROUTING_KEY_UPDATE = "level-up.update.retail.service";
    protected static final String ROUTING_KEY_DELETE_BY_LEVEL_UP_ID = "level-up.deleteByLevelUpId.retail.service";
    protected static final String ROUTING_KEY_DELETE_BY_CUSTOMER_ID = "level-up.deleteByCustomerId.retail.service";
    protected static final Long TIMEOUT = 8L;
    protected static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;

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
            throw new RuntimeException(e.getCause() + " \n " + e.getMessage());
        }
    }

    public void updateLevelUp(LevelUpViewModel luvm) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_UPDATE, luvm);
    }

    public void deleteByLevelUpId(Integer levelUpId) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_DELETE_BY_LEVEL_UP_ID, levelUpId);
    }

    public void deleteByCustomerId(Integer customerId) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY_DELETE_BY_CUSTOMER_ID, customerId);
    }
}
