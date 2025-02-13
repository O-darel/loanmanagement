package com.example.loanmanagement.services;

import com.example.loanmanagement.dtos.AddLoanDto;
import com.example.loanmanagement.entities.Loan;
import com.example.loanmanagement.entities.User;
import com.example.loanmanagement.repositories.LoanRepository;
import com.example.loanmanagement.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public static Logger logger;

    public LoanService(LoanRepository loanRepository,
                       UserRepository userRepository){
        this.loanRepository=loanRepository;
        this.userRepository=userRepository;

        //logger
        //this.logger=logger;
    }

    public Loan addLoan(AddLoanDto input){
        Loan loan=new Loan();
        loan.setAmount(input.getAmount());
        loan.setIssueDate(input.getIssueDate());
        //get user
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User currentUser=(User) authentication.getPrincipal();
        loan.setUser(currentUser);
        //save loan
        return loanRepository.save(loan);
    }

    public List<Loan> getLoans(){
        List<Loan> loans=new ArrayList<>();
        loanRepository.findAll().forEach(loans::add);
        //logger.log((LogRecord) loans);
        return loans;
    }
}
