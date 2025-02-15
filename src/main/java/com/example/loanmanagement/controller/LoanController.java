package com.example.loanmanagement.controller;

import com.example.loanmanagement.dtos.*;
import com.example.loanmanagement.entities.Loan;
import com.example.loanmanagement.services.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "Loan Controller", description = "Endpoints for managing loans")
@RequestMapping("/api/loans")
@RestController
public class LoanController {
    private final LoanService loanService;

    public LoanController( LoanService loanService){
        this.loanService=loanService;
    }


    @Operation(summary = "Get loans",
            description = "Returns aggregated statistics for loans",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved loans"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Loan>> getLoans(){
        List<Loan> loans=loanService.getLoans();
        return ResponseEntity.ok(loans.isEmpty() ? Collections.emptyList() : loans);
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddLoanResponse> createLoan(
            @RequestBody AddLoanDto addLoanDto
    ){
        Loan loan=loanService.addLoan(addLoanDto);
        //construct response
        AddLoanResponse loanResponse=new AddLoanResponse();
        loanResponse.setName(loan.getUser().getFullName());
        loanResponse.setEmail(loan.getUser().getEmail());
        loanResponse.setLoanPeriod(loan.getLoanPeriod());
        loanResponse.setInterestRate(loan.getInterestRate());
        loanResponse.setPrincipalAmount(loan.getPrincipalAmount());
        loanResponse.setRepaymentFrequency(loan.getRepaymentFrequency());
        loanResponse.setApplicationDate(loan.getCreatedAt());
        loanResponse.setId(loan.getId());

        return ResponseEntity.ok(loanResponse);
    }


    //get loan status
    @PostMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN','USER','AGENT')")
    public ResponseEntity<LoanStatusResponse> getLoans(
            @RequestBody LoanStatusRequest request
    ){
        LoanStatusResponse loan=loanService.getLoanStatus(request.getId());

        return ResponseEntity.ok(loan);
    }


    //update loan status
    @PutMapping("/update-status")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public ResponseEntity<LoanStatusResponse> updateLoanStatus(@RequestBody LoanStatusUpdateRequest request) {
        LoanStatusResponse updatedLoan = loanService.updateLoanStatus(request.getId(), request.getStatus());
        return ResponseEntity.ok(updatedLoan);
    }

    //get repayment status
    @PostMapping("/repayment-schedule")
    @PreAuthorize("hasAnyRole('ADMIN','USER','AGENT')")
    public ResponseEntity<RepaymentScheduleStatus> getRepaymentStatus(
            @RequestBody LoanStatusRequest request
    ){
        RepaymentScheduleStatus loan=loanService.getLoanRepaymentScheduleStatus(request.getId());

        return ResponseEntity.ok(loan);
    }

    @PutMapping("/repay")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponseDto> repayLoan(
            @RequestBody LoanRepaymentRequest request
    ) {
        PaymentResponseDto updatedLoan = loanService.makeRepayment(request.getId(), request.getPaidAmount());
        return ResponseEntity.ok(updatedLoan);
    }

}
