package com.percyvega.listener;

import com.percyvega.common.BookOrderStatus;
import com.percyvega.common.ProcessedBookOrder;
import lombok.extern.log4j.Log4j2;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class BookOrderProcessingMessageListener {

    private static final String BOOK_ORDER_PROCESSED_QUEUE = "book.order.processed.queue";
    private static final String BOOK_ORDER_PROCESSED_CREATED_QUEUE = "book.order.processed.created.queue";
    private static final String BOOK_ORDER_PROCESSED_UPDATED_QUEUE = "book.order.processed.updated.queue";
    private static final String BOOK_ORDER_PROCESSED_DELETED_QUEUE = "book.order.processed.deleted.queue";

    @JmsListener(destination = BOOK_ORDER_PROCESSED_QUEUE)
    public JmsResponse<Message<ProcessedBookOrder>> receive(@Payload ProcessedBookOrder processedBookOrder,
                                                            @Header("orderStatus") String orderStatus) {
        log.info("BookOrderProcessingMessageListener.receive: orderStatus={}, processedBookOrder={}", processedBookOrder, orderStatus);

        Message<ProcessedBookOrder> processedBookOrderMessage = MessageBuilder
                .withPayload(processedBookOrder)
                .setHeader("orderStatus", orderStatus)
                .build();

        try {
            switch (BookOrderStatus.valueOf(orderStatus)) {
                case CREATED:
                    return JmsResponse.forQueue(processedBookOrderMessage, BOOK_ORDER_PROCESSED_CREATED_QUEUE);
                case UPDATED:
                    return JmsResponse.forQueue(processedBookOrderMessage, BOOK_ORDER_PROCESSED_UPDATED_QUEUE);
                case DELETED:
                    return JmsResponse.forQueue(processedBookOrderMessage, BOOK_ORDER_PROCESSED_DELETED_QUEUE);
                default:
                    throw new IllegalArgumentException(String.format("Unknown orderStatus: %s", orderStatus));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            log.info("BookOrderProcessingMessageListener received.");
        }
    }

}
