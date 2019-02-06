package com.percyvega.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedBookOrder {
    private BookOrder bookOrder;
    private Date processingDateTime;
    private Date expectedShippingDateTime;
}
