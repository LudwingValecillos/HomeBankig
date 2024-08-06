package com.mainhub.homebanking.repositories;

import com.mainhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
