package com.luv2read.springbootlibrary.requestmodels;

import java.util.Optional;

public record ReviewRequest(double rating, Long bookId, Optional<String> reviewDescription) {}
