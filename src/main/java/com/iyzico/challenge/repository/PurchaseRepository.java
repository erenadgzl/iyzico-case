package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author erenadiguzel
 */

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>{
}
