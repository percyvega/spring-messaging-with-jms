package com.percyvega.service;

import com.percyvega.common.BookOrder;
import com.percyvega.common.BookOrderStatus;
import com.percyvega.common.ProcessedBookOrder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class WarehouseReceiverService {

    private static final String BOOK_ORDER_QUEUE = "book.order.queue";
    private static final String BOOK_ORDER_PROCESSED_QUEUE = "book.order.processed.queue";

    public final WarehouseProcessingService warehouseProcessingService;

    @Autowired
    public WarehouseReceiverService(WarehouseProcessingService warehouseProcessingService) {
        this.warehouseProcessingService = warehouseProcessingService;
    }

    @JmsListener(destination = BOOK_ORDER_QUEUE)
    @SendTo(BOOK_ORDER_PROCESSED_QUEUE)
    public Message<ProcessedBookOrder> receive(@Payload BookOrder bookOrder,
                                               @Header("storeId") String storeId,
                                               @Header("orderStatus") String orderStatus,
                                               MessageHeaders messageHeaders) {
        log.info("WarehouseReceiverService.receive: storeId={}, orderStatus={}, messageHeaders={}",
                storeId, orderStatus, messageHeaders);
        log.info("WarehouseReceiverService.receive: bookOrder={}", bookOrder);

        if ((Long.parseLong(storeId) + Long.parseLong(bookOrder.getBookOrderId())) % 7 == 0) {
            throw new IllegalArgumentException(String.format("Error while processing this BookOrder: %s", bookOrder));
        }

        Message<ProcessedBookOrder> processedBookOrderMessage = warehouseProcessingService.processBookOrder(bookOrder, orderStatus);
        log.info("WarehouseReceiverService received.");
        return processedBookOrderMessage;
    }

}
