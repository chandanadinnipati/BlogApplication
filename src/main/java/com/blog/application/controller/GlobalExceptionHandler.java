package com.blog.application.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception exception, Model model) {
        model.addAttribute("error", "An Error Occurred");
        model.addAttribute("message", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundException(NoHandlerFoundException exception, Model model) {
        model.addAttribute("error", "Page Not Found");
        model.addAttribute("message", "The page you are looking for does not exist.");
        return "error";
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public String handleAccessDeniedException(Exception exception, Model model) {
        model.addAttribute("error", "Access Denied");
        model.addAttribute("message", "You do not have permission to access this page.");
        return "error";
    }
}
