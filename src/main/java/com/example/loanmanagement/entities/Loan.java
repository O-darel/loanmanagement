package com.example.loanmanagement.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Loan")
@Getter
@Setter
public class Loan {

    // ✅ Constructor sets default status
//    public Loan() {
//        this.status = LoanStatus.PENDING;
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    private User user;

    @Column(nullable = false)
    private  double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status=LoanStatus.PENDING; //Default value

    @Column(nullable = false)
    private LocalDate issueDate;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


    //set specific loan status
    public enum LoanStatus{
        PENDING, APPROVED, REJECTED, PAID
    }


}


