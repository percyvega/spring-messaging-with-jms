package com.percyvega.service;

import com.percyvega.dto.BookOrder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class WarehouseReceiverService {

    private static final String BOOK_QUEUE = "book.order.queue";

    public final WarehouseProcessingService warehouseProcessingService;

    @Autowired
    public WarehouseReceiverService(WarehouseProcessingService warehouseProcessingService) {
        this.warehouseProcessingService = warehouseProcessingService;
    }

    @JmsListener(destination = BOOK_QUEUE)
    public void receive(BookOrder bookOrder) {
        log.info("WarehouseReceiverService.receive: {}", bookOrder);

        if(Long.parseLong(bookOrder.getBookOrderId()) % 5 == 0) {
            throw new RuntimeException(String.format("Error while processing this BookOrder: %s", bookOrder));
        }

        warehouseProcessingService.processBookOrder(bookOrder);
        log.info("WarehouseReceiverService received: {}", bookOrder);
    }

}
