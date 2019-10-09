package com.trilogyed.levelupservice.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.levelupservice.LevelUpServiceApplication;
import com.trilogyed.levelupservice.service.ServiceLayer;
import com.trilogyed.queue.LevelUpViewModel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;

import java.io.IOException;
import java.nio.charset.Charset;

@Service
public class MessageListener {

    @Autowired
    ServiceLayer sl;

    @Autowired
    ObjectMapper mapper;

    @RabbitListener(queues = LevelUpServiceApplication.QUEUE_NAME)
    public LevelUpViewModel receiveMessageToSaveDelete(Message message,
                                                       @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key)
            throws IOException {
        String body = new String(message.getBody(), Charset.defaultCharset().name());
        System.out.println("RECEIVED || key: " + key + " | body: " + body);
        if (key.matches("^level-up[.]create[.].*$")) {
            LevelUpViewModel luvm = mapper.readValue(body, LevelUpViewModel.class);
            return sl.save(luvm);
        } else if (key.matches("^level-up[.]update[.].*$")) {
            LevelUpViewModel lupvm = mapper.readValue(body, LevelUpViewModel.class);
            sl.update(lupvm);
            return null;
        } else if (key.matches("^level-up[.]deleteByLevelUpId[.].*$")) {
            try {
                sl.delete(Integer.parseInt(body));
            } catch (Throwable e) {}
            return null;
        } else if (key.matches("^level-up[.]deleteByCustomerId[.].*$")) {
            try {
                sl.deleteByCustomerId(Integer.parseInt(body));
            } catch (Throwable e) {}
            return null;
        }
        else {
            return null;
        }

    }
}
