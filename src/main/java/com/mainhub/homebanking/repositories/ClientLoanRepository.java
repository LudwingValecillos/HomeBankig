package com.mainhub.homebanking.repositories;

import com.mainhub.homebanking.models.ClientLoan;
import com.mainhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {

    List<ClientLoan> findAllByClientId(Long clientId);

}
