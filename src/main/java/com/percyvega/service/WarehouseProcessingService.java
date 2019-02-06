package com.percyvega.service;

import com.percyvega.dto.BookOrder;
import com.percyvega.dto.ProcessedBookOrder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Log4j2
@Service
public class WarehouseProcessingService {

    private static final String BOOK_ORDER_PROCESSED_QUEUE = "book.order.processed.queue";

    private final JmsTemplate jmsTemplate;

    @Autowired
    public WarehouseProcessingService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    public void processBookOrder(BookOrder bookOrder) {
        log.info("WarehouseProcessingService.processBookOrder: {}", bookOrder);
        ProcessedBookOrder processedBookOrder = new ProcessedBookOrder(bookOrder, new Date(), new Date());
        jmsTemplate.convertAndSend(BOOK_ORDER_PROCESSED_QUEUE, processedBookOrder);
        log.info("WarehouseProcessingService processed book order: {}", processedBookOrder);
    }

}
