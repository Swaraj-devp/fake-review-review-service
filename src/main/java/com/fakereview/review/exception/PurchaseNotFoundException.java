package com.fakereview.review.exception;

public class PurchaseNotFoundException extends RuntimeException {

    public PurchaseNotFoundException(String message){
        super(message);
    }
}