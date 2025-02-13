package com.example.loanmanagement.controller;

import com.example.loanmanagement.dtos.AddLoanDto;
import com.example.loanmanagement.entities.Loan;
import com.example.loanmanagement.entities.User;
import com.example.loanmanagement.services.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequestMapping("/api/loans")
@RestController
public class LoanController {
    private final LoanService loanService;

    public LoanController( LoanService loanService){
        this.loanService=loanService;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Loan>> getLoans(){
        List<Loan> loans=loanService.getLoans();
        return ResponseEntity.ok(loans.isEmpty() ? Collections.emptyList() : loans);
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Loan> createLoan(
            @RequestBody AddLoanDto addLoanDto
    ){
        Loan loan=loanService.addLoan(addLoanDto);
        return ResponseEntity.ok(loan);
    }



}
