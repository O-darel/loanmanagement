package com.example.loanmanagement.services;


import com.example.loanmanagement.dtos.LoanStatsResponse;
import com.example.loanmanagement.entities.Loan;
import com.example.loanmanagement.repositories.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StatsService {

    private final LoanRepository loanRepository;

    public StatsService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Transactional(readOnly = true)
    public LoanStatsResponse getLoanStats() {
        List<Loan> loans = loanRepository.findAll();

//        LoanStatsResponse stats = new LoanStatsResponse();
//        stats.setTotalLoans(loans.size());
//        stats.setPendingLoans(loans.stream().filter(loan -> loan.getStatus() == Loan.LoanStatus.PENDING).count());
//        stats.setApprovedLoans(loans.stream().filter(loan -> loan.getStatus() == Loan.LoanStatus.APPROVED).count());
//        stats.setRejectedLoans(loans.stream().filter(loan -> loan.getStatus() == Loan.LoanStatus.REJECTED).count());
//        stats.setDisbursedLoans(loans.stream().filter(loan -> loan.getStatus() == Loan.LoanStatus.DISBURSED).count());
//        stats.setPaidLoans(loans.stream().filter(loan -> loan.getStatus() == Loan.LoanStatus.PAID).count());
//
//        // Total principal amount across all loans
//        double totalPrincipal = loans.stream().mapToDouble(Loan::getPrincipalAmount).sum();
//        stats.setTotalPrincipalAmount(totalPrincipal);
//
//        // Sum of outstanding balances (balance field)
//        double totalBalance = loans.stream().mapToDouble(Loan::getBalance).sum();
//        stats.setTotalOutstandingBalance(totalBalance);

        LoanStatsResponse stats = new LoanStatsResponse();
        stats.setTotalLoans(loans.size());

        // Pending loans: only those in PENDING state
        long pendingLoans = loans.stream()
                .filter(loan -> loan.getStatus() == Loan.LoanStatus.PENDING)
                .count();
        stats.setPendingLoans(pendingLoans);

        // Approved loans: any loan that has passed the approved stage
        long approvedLoans = loans.stream()
                .filter(loan -> loan.getStatus() == Loan.LoanStatus.APPROVED
                        || loan.getStatus() == Loan.LoanStatus.DISBURSED
                        || loan.getStatus() == Loan.LoanStatus.PAID)
                .count();
        stats.setApprovedLoans(approvedLoans);

        // Rejected loans: those explicitly rejected
        long rejectedLoans = loans.stream()
                .filter(loan -> loan.getStatus() == Loan.LoanStatus.REJECTED)
                .count();
        stats.setRejectedLoans(rejectedLoans);

        // Disbursed loans: loans that are disbursed or have been paid
        long disbursedLoans = loans.stream()
                .filter(loan -> loan.getStatus() == Loan.LoanStatus.DISBURSED
                        || loan.getStatus() == Loan.LoanStatus.PAID)
                .count();
        stats.setDisbursedLoans(disbursedLoans);

        // Paid loans: loans that are completely paid
        long paidLoans = loans.stream()
                .filter(loan -> loan.getStatus() == Loan.LoanStatus.PAID)
                .count();
        stats.setPaidLoans(paidLoans);

        // Total principal amount across all loans
        double totalPrincipal = loans.stream().mapToDouble(Loan::getPrincipalAmount).sum();
        stats.setTotalPrincipalAmount(totalPrincipal);

        // Sum of outstanding balances (balance field)
        double totalBalance = loans.stream().mapToDouble(Loan::getBalance).sum();
        stats.setTotalOutstandingBalance(totalBalance);

        return stats;
    }

}
