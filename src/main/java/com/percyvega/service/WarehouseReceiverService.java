package com.percyvega.service;

import com.percyvega.dto.BookOrder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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
    public void receive(@Header("bookOrderId") String bookOrderId,
                        @Header("storeId") String storeId,
                        @Header("orderStatus") String orderStatus,
                        @Payload BookOrder bookOrder,
                        MessageHeaders messageHeaders) {
        log.info("WarehouseReceiverService.receive: bookOrderId={}, storeId={}, orderStatus={}, messageHeaders={}",
                bookOrderId, storeId, orderStatus, messageHeaders);
        log.info("WarehouseReceiverService.receive: bookOrder={}", bookOrder);

        if(Long.parseLong(bookOrder.getBookOrderId()) % 7 == 0) {
            throw new RuntimeException(String.format("Error while processing this BookOrder: %s", bookOrder));
        }

        warehouseProcessingService.processBookOrder(bookOrder, orderStatus, storeId);
        log.info("WarehouseReceiverService received: {}", bookOrder);
    }

}
