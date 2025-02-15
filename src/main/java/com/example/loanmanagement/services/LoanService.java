package com.example.loanmanagement.services;

import com.example.loanmanagement.Helpers.LoanStatusTransition;
import com.example.loanmanagement.dtos.AddLoanDto;
import com.example.loanmanagement.dtos.LoanStatusResponse;
import com.example.loanmanagement.dtos.PaymentResponseDto;
import com.example.loanmanagement.dtos.RepaymentScheduleStatus;
import com.example.loanmanagement.entities.Loan;
import com.example.loanmanagement.entities.User;
import com.example.loanmanagement.repositories.LoanRepository;
import com.example.loanmanagement.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    @Transactional
    public Loan addLoan(AddLoanDto input){
        Loan loan=new Loan();
        loan.setPrincipalAmount(input.getPrincipalAmount());
        loan.setInterestRate(input.getInterestRate());
        loan.setLoanPeriod(input.getLoanPeriod());
        loan.setRepaymentFrequency(input.getRepaymentFrequency());
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

    public LoanStatusResponse getLoanStatus(Integer id){
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));

        // Map Loan entity to LoanStatus DTO
        LoanStatusResponse loanStatus = new LoanStatusResponse();
        loanStatus.setId(loan.getId());
        loanStatus.setPrincipalAmount(loan.getPrincipalAmount());
        loanStatus.setInterestRate(loan.getInterestRate());
        loanStatus.setLoanPeriod(loan.getLoanPeriod());
        loanStatus.setRepaymentFrequency(loan.getRepaymentFrequency());
        loanStatus.setStatus(loan.getStatus().toString());

        return loanStatus;
    }

    public RepaymentScheduleStatus getLoanRepaymentScheduleStatus(Integer id){
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));

        // Map Loan entity to LoanStatus DTO
        RepaymentScheduleStatus loanStatus = new RepaymentScheduleStatus();
        loanStatus.setId(loan.getId());
        loanStatus.setName(loan.getUser().getFullName());
        loanStatus.setPrincipalAmount(loan.getPrincipalAmount());
        loanStatus.setInterestRate(loan.getInterestRate());
        loanStatus.setLoanPeriod(loan.getLoanPeriod());
        loanStatus.setRepaymentFrequency(loan.getRepaymentFrequency());
        loanStatus.setMonthlyRepayment(loan.getMonthlyRepayment());
        loanStatus.setStatus(loan.getStatus().toString());

        return loanStatus;
    }

    @Transactional
    public LoanStatusResponse updateLoanStatus(Integer id, String status){
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));

        Loan.LoanStatus newStatus;
        try {
            newStatus = Loan.LoanStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        // ✅ Check if transition is allowed
        if (!LoanStatusTransition.isValidTransition(loan.getStatus(), newStatus)) {
            throw new IllegalStateException("Invalid status transition from " + loan.getStatus() + " to " + newStatus);
        }
        if (newStatus == Loan.LoanStatus.APPROVED){
            loan.setApprovedDate(LocalDate.now());
        }

        // ✅ Handle logic when moving to DISBURSED
        if (newStatus == Loan.LoanStatus.DISBURSED) {
            loan.setDisbursedDate(LocalDate.now());  // Set disbursed date

            // 🔢 Calculate EMI using months
            double emi = calculateEMI(loan.getPrincipalAmount(), loan.getInterestRate(), loan.getLoanPeriod());
            loan.setMonthlyRepayment(emi);

            // Total due amount is EMI multiplied by the total number of months.
            double totalDue = emi * loan.getLoanPeriod();
            loan.setBalance(totalDue);

            // 📅 Calculate repayment due date
            LocalDate repaymentDate = loan.getDisbursedDate().plusMonths((long) loan.getRepaymentFrequency());

            // 🏦 Store repayment due date
            loan.setDueDate(repaymentDate);

            System.out.println("Loan Disbursed. EMI: " + emi + ", Next Payment Due: " + repaymentDate);
        }

        loan.setStatus(newStatus);
        loanRepository.save(loan);

        return mapToResponse(loan);
    }

    // 📌 Corrected EMI Calculation Formula (Using Months)
    private double calculateEMI(double principal, double interestRate, double loanPeriodInMonths) {
        double monthlyRate = (interestRate / 100) / 12;  // Convert annual rate to monthly
        double months = loanPeriodInMonths;

        if (monthlyRate == 0) {
            return principal / months;
        }

        return (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) /
                (Math.pow(1 + monthlyRate, months) - 1);
    }

    private LoanStatusResponse mapToResponse(Loan loan) {
        LoanStatusResponse response = new LoanStatusResponse();
        response.setId(loan.getId());
        response.setPrincipalAmount(loan.getPrincipalAmount());
        response.setInterestRate(loan.getInterestRate());
        response.setLoanPeriod(loan.getLoanPeriod());
        response.setRepaymentFrequency(loan.getRepaymentFrequency());
        response.setStatus(loan.getStatus().name());
        response.setBalanceDue(loan.getBalance());
        return response;
    }


    //Make payment endpoint
    @Transactional
    public PaymentResponseDto makeRepayment(Integer id, double paidAmount) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id: " + id));

        // Only allow repayment if the loan has been disbursed (i.e. has a balance).
        if (loan.getStatus() != Loan.LoanStatus.DISBURSED && loan.getStatus() != Loan.LoanStatus.APPROVED) {
            throw new IllegalStateException("Loan is not in a state where repayment is allowed.");
        }

        // Subtract the paid amount from the current balance.
        double newBalance = loan.getBalance() - paidAmount;
        loan.setBalance(newBalance);

        // If the balance is 0 or less, mark the loan as PAID.
        if (newBalance <= 0) {
            loan.setBalance(0);
            loan.setStatus(Loan.LoanStatus.PAID);
        }

        loanRepository.save(loan);
        //construct response dto
        PaymentResponseDto response=new PaymentResponseDto();
        response.setId(loan.getId());
        response.setPrincipalAmount(loan.getPrincipalAmount());
        response.setInterestRate(loan.getInterestRate());
        response.setLoanPeriod(loan.getLoanPeriod());
        response.setBalance(loan.getBalance());
        response.setStatus(loan.getStatus().name());
        response.setPaidAmount(paidAmount);
        //next payment
        response.setNextPaymentDate(LocalDate.now().plusMonths(1));

        return(response);
    }
}
