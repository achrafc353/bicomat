package com.miage.bicomat.repository;

import com.miage.bicomat.domain.Tiers;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tiers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TiersRepository extends JpaRepository<Tiers, Long> {}
