package com.luv2read.springbootlibrary.requestmodels;

public record AddBookRequest(
    String title,
    String author,
    String description,
    int copies,
    String category,
    String img
) {}
