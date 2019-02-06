package com.percyvega.controller;

import com.percyvega.dto.BookOrder;
import com.percyvega.service.BookOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("bookController")
public class BookOrderController {

    private final BookOrderService bookOrderService;

    @Autowired
    public BookOrderController(BookOrderService bookOrderService) {
        this.bookOrderService = bookOrderService;
    }

    @PostMapping
    public ResponseEntity<BookOrder> createBookOrder(@RequestBody BookOrder bookOrder) {
        log.info("BookOrderController.createBookOrder: {}", bookOrder);
        bookOrderService.send(bookOrder);
        log.info("BookOrderController created: {}", bookOrder);
        return new ResponseEntity<>(bookOrder, HttpStatus.CREATED);
    }
}
