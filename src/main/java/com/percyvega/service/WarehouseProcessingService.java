package com.percyvega.service;

import com.percyvega.common.BookOrder;
import com.percyvega.common.BookOrderStatus;
import com.percyvega.common.ProcessedBookOrder;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.percyvega.common.BookOrderStatus.*;

@Log4j2
@Service
public class WarehouseProcessingService {

    public Message<ProcessedBookOrder> processBookOrder(BookOrder bookOrder, String orderStatus) {
        log.info("WarehouseProcessingService.processBookOrder: orderStatus={}, bookOrder={}", orderStatus, bookOrder);

        ProcessedBookOrder processedBookOrder;
        switch (BookOrderStatus.valueOf(orderStatus)) {
            case CREATE:
                processedBookOrder = new ProcessedBookOrder(bookOrder, new Date(), new Date());
                orderStatus = CREATED.name();
                log.info("**ADDING A CREATE RECORD TO THE DATABASE**");
                break;
            case UPDATE:
                processedBookOrder = new ProcessedBookOrder(bookOrder, new Date(), new Date());
                orderStatus = UPDATED.name();
                log.info("**UPDATING A RECORD IN THE DATABASE**");
                break;
            case DELETE:
                processedBookOrder = new ProcessedBookOrder(bookOrder, new Date(), null);
                orderStatus = DELETED.name();
                log.info("**DELETING A RECORD FROM THE DATABASE**");
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown orderStatus: %s", orderStatus));
        }

        log.info("WarehouseProcessingService processed book order.");

        return MessageBuilder
                .withPayload(processedBookOrder)
                .setHeader("orderStatus", orderStatus)
                .build();
    }

}
