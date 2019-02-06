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
    public void processBookOrder(BookOrder bookOrder, String orderStatus, String storeId) {
        log.info("WarehouseProcessingService.processBookOrder: orderStatus={}, storeId={}", orderStatus, storeId);
        log.info("WarehouseProcessingService.processBookOrder: {}", bookOrder);
        ProcessedBookOrder processedBookOrder = new ProcessedBookOrder(bookOrder, new Date(), new Date());

        switch (orderStatus) {
            case "NEW":
                log.info("**ADDING A NEW RECORD TO THE DATABASE**");
                break;
            case "UPDATE":
                log.info("**UPDATING A RECORD IN THE DATABASE**");
                break;
            case "DELETE":
                log.info("**DELETING A RECORD FROM THE DATABASE**");
                break;
            default:
                throw new RuntimeException(String.format("Unknown orderStatus: %s", orderStatus));
        }

        jmsTemplate.convertAndSend(BOOK_ORDER_PROCESSED_QUEUE, processedBookOrder);
        log.info("WarehouseProcessingService processed book order: {}", processedBookOrder);
    }

}
