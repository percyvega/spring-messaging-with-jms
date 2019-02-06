package com.percyvega.service;

import com.percyvega.dto.BookOrder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class BookOrderService {

    private static final String BOOK_QUEUE = "book.order.queue";

    private final JmsTemplate jmsTemplate;

    @Autowired
    public BookOrderService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    public void send(BookOrder bookOrder) {
        log.info("BookOrderService.send: {}", bookOrder);
        jmsTemplate.convertAndSend(BOOK_QUEUE, bookOrder);
        log.info("BookOrderService sent: {}", bookOrder);
    }

}
