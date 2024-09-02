package com.mainhub.homebanking.repositories;

import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByNumber(String number);

    List<Account> findByClient(Client client);

    Account existsByNumber(String number);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(a.number, LENGTH(:prefix) + 1) AS int)), 0) FROM Account a WHERE a.number LIKE CONCAT(:prefix, '%')")
    int findMaxAccountNumberByPrefix(String prefix);
}
