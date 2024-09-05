package com.mainhub.homebanking.repositories;

import com.mainhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {


}
