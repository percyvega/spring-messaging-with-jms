package com.percyvega.service;

import com.percyvega.common.BookOrder;
import com.percyvega.common.BookOrderStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class BookOrderService {

    private static final String BOOK_ORDER_QUEUE = "book.order.queue";

    private final JmsTemplate jmsTemplate;

    @Autowired
    public BookOrderService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    public void send(BookOrder bookOrder, String storeId, BookOrderStatus orderStatus) {
        log.info("BookOrderService.send: {}", bookOrder);
        jmsTemplate.convertAndSend(BOOK_ORDER_QUEUE, bookOrder, message -> {
            message.setStringProperty("storeId", storeId);
            message.setStringProperty("orderStatus", orderStatus.name());
            return message;
        });
        log.info("BookOrderService sent.");
    }

}
