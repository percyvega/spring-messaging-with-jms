package com.percyvega.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Log4j2
@Component
public class BookOrderProcessingMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        log.info("BookOrderProcessingMessageListener.onMessage: {}", message);
        String text = null;
        try {
            text = ((TextMessage) message).getText();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        log.info("BookOrderProcessingMessageListener - text: {}", text);
    }
}
