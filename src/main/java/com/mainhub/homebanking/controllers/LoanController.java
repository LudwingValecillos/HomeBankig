package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.LoanAplicationDTO;
import com.mainhub.homebanking.services.LoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanServices loanServices;

    @GetMapping("/")
    public ResponseEntity<?> getLoans() {
        return ResponseEntity.ok(loanServices.getAllLoansDTO());
    }

    @GetMapping("/loansAvailable")
    public ResponseEntity<?> loansAvailable(Authentication authentication) {
        return loanServices.loansAvailable(authentication);
    }

    @GetMapping("/loansApplied")
    public ResponseEntity<?> loansApplied(Authentication authentication) {
        return loanServices.loansApplied(authentication);
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyForLoan(Authentication authentication, @RequestBody LoanAplicationDTO loanDTO) {
        return loanServices.applyForLoan(authentication, loanDTO);
    }
}
