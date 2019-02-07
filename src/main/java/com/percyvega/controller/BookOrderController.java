package com.percyvega.controller;

import com.percyvega.common.BookOrder;
import com.percyvega.service.BookOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.percyvega.common.BookOrderStatus.*;

@Log4j2
@RestController
@RequestMapping("bookController")
public class BookOrderController {

    private final BookOrderService bookOrderService;

    @Autowired
    public BookOrderController(BookOrderService bookOrderService) {
        this.bookOrderService = bookOrderService;
    }

    @PostMapping("/{storeId}")
    public ResponseEntity<BookOrder> createBookOrder(
            @RequestBody BookOrder bookOrder,
            @PathVariable String storeId) {
        log.info("BookOrderController.createBookOrder: {}", bookOrder);
        bookOrderService.send(bookOrder, storeId, CREATE);
        log.info("BookOrderController created: {}", bookOrder);
        return new ResponseEntity<>(bookOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<BookOrder> updateBookOrder(
            @RequestBody BookOrder bookOrder,
            @PathVariable String storeId) {
        log.info("BookOrderController.updateBookOrder: {}", bookOrder);
        bookOrderService.send(bookOrder, storeId, UPDATE);
        log.info("BookOrderController updated");
        return new ResponseEntity<>(bookOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<BookOrder> deleteBookOrder(
            @RequestBody BookOrder bookOrder,
            @PathVariable String storeId) {
        log.info("BookOrderController.deleteBookOrder: {}", bookOrder);
        bookOrderService.send(bookOrder, storeId, DELETE);
        log.info("BookOrderController deleted");
        return new ResponseEntity<>(bookOrder, HttpStatus.OK);
    }
}
