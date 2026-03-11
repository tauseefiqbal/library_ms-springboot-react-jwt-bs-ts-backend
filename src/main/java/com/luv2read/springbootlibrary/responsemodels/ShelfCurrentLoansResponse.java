package com.luv2read.springbootlibrary.responsemodels;

import com.luv2read.springbootlibrary.entity.Book;

public record ShelfCurrentLoansResponse(Book book, int daysLeft) {}
