package com.luv2read.springbootlibrary.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luv2read.springbootlibrary.entity.Book;
import com.luv2read.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import com.luv2read.springbootlibrary.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(Authentication authentication)
        throws Exception
    {
        String userEmail = authentication.getName();
        return bookService.currentLoans(userEmail);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(Authentication authentication) {
        String userEmail = authentication.getName();
        return bookService.currentLoansCount(userEmail);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(Authentication authentication,
                                      @RequestParam Long bookId) {
        String userEmail = authentication.getName();
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook (Authentication authentication,
                              @RequestParam Long bookId) throws Exception {
        String userEmail = authentication.getName();
        return bookService.checkoutBook(userEmail, bookId);
    }

    @PutMapping("/secure/return")
    public void returnBook(Authentication authentication,
                           @RequestParam Long bookId) throws Exception {
        String userEmail = authentication.getName();
        bookService.returnBook(userEmail, bookId);
    }

    @PutMapping("/secure/renew/loan")
    public void renewLoan(Authentication authentication,
                          @RequestParam Long bookId) throws Exception {
        String userEmail = authentication.getName();
        bookService.renewLoan(userEmail, bookId);
    }

}












