package com.erm.legacy.web;

import com.erm.legacy.service.RiskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ApiExceptionHandler {

    private final RiskService riskService;

    public ApiExceptionHandler(RiskService riskService) {
        this.riskService = riskService;
    }

    @ExceptionHandler(InvalidDomainException.class)
    public Object handleInvalidDomain(InvalidDomainException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ErrorResponse error = new ErrorResponse(
                    "INVALID_DOMAIN",
                    "Invalid domain value '" + ex.getRejectedValue() + "'. Allowed values: " + ex.getAllowedValues(),
                    ex.getField(),
                    request.getRequestURI());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        ModelAndView mav = new ModelAndView("risks");
        mav.addObject("pageTitle", "Enterprise Risk Register");
        mav.addObject("domains", riskService.domains());
        mav.addObject("risks", riskService.findAll());
        mav.addObject("domainError", "Invalid domain '" + ex.getRejectedValue() + "'. Allowed values: " + ex.getAllowedValues());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ErrorResponse error = new ErrorResponse(
                    "FORBIDDEN",
                    "You do not have permission to access this resource.",
                    null,
                    request.getRequestURI());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
        return new ModelAndView("redirect:/login");
    }

    @ExceptionHandler(Exception.class)
    public Object handleInternal(Exception ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            ErrorResponse error = new ErrorResponse(
                    "INTERNAL_ERROR",
                    "An unexpected error occurred.",
                    null,
                    request.getRequestURI());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        return new ModelAndView("redirect:/dashboard");
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String accept = request.getHeader("Accept");
        return uri != null && uri.startsWith("/api")
                || (accept != null && accept.contains("application/json"));
    }
}
