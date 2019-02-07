package com.percyvega.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookOrder {
    private String bookOrderId;
    private Book book;
    private Customer customer;
}
