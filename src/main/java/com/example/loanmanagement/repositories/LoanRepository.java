package com.example.loanmanagement.repositories;

import com.example.loanmanagement.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Integer> {

    //Optional<Loan> findById(Integer integer);

    //find loans by user id
    List<Loan> findByUserId(Integer userId);

    //find loan by status
    List<Loan> findByStatus(Loan.LoanStatus status);

    // Custom query to find all loans by user ID and status
    List<Loan> findByUserIdAndStatus(Integer userId, String status);


}
