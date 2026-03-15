package com.luv2read.springbootlibrary.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2read.springbootlibrary.dao.BookRepository;
import com.luv2read.springbootlibrary.dao.CheckoutRepository;
import com.luv2read.springbootlibrary.dao.HistoryRepository;
import com.luv2read.springbootlibrary.entity.Book;
import com.luv2read.springbootlibrary.entity.Checkout;
import com.luv2read.springbootlibrary.entity.History;
import com.luv2read.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;


@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    private final CheckoutRepository checkoutRepository;

    private final HistoryRepository historyRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository,
                       HistoryRepository historyRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository = historyRepository;
    }

    public Book checkoutBook (String userEmail, Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        List<Checkout> existingCheckouts = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (!book.isPresent() || !existingCheckouts.isEmpty() || book.get().getCopiesAvailable() <= 0) {
            throw new Exception("Book doesn't exist or already checked out by user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

        Checkout checkout = new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                book.get().getId()
        );

        checkoutRepository.save(checkout);

        return book.get();
    }

    public Boolean checkoutBookByUser(String userEmail, Long bookId) {
        List<Checkout> checkouts = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        return !checkouts.isEmpty();
    }

    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {

        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();

        for (Checkout i: checkoutList) {
            bookIdList.add(i.getBookId());
        }

        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        for (Book book : books) {
            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(x -> x.getBookId().equals(book.getId())).findFirst();

            if (checkout.isPresent()) {

                LocalDate returnDate = LocalDate.parse(checkout.get().getReturnDate());
                LocalDate today = LocalDate.now();

                long daysLeft = ChronoUnit.DAYS.between(today, returnDate);

                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) daysLeft));
            }
        }
        return shelfCurrentLoansResponses;
    }

    public void returnBook (String userEmail, Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        List<Checkout> checkouts = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (!book.isPresent() || checkouts.isEmpty()) {
            throw new Exception("Book does not exist or not checked out by user");
        }

        Checkout validateCheckout = checkouts.get(0);

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);

        bookRepository.save(book.get());
        // Delete all duplicate checkout records for this user/book
        for (Checkout c : checkouts) {
            checkoutRepository.deleteById(c.getId());
        }

        History history = new History(
                userEmail,
                validateCheckout.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg()
        );

        historyRepository.save(history);
    }

    public void renewLoan(String userEmail, Long bookId) throws Exception {

        List<Checkout> checkouts = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (checkouts.isEmpty()) {
            throw new Exception("Book does not exist or not checked out by user");
        }

        Checkout validateCheckout = checkouts.get(0);

        LocalDate returnDate = LocalDate.parse(validateCheckout.getReturnDate());
        LocalDate today = LocalDate.now();

        if (returnDate.isAfter(today) || returnDate.isEqual(today)) {
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validateCheckout);
        }
    }

}















