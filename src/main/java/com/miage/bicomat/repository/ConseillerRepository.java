package com.miage.bicomat.repository;

import com.miage.bicomat.domain.Conseiller;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Conseiller entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConseillerRepository extends JpaRepository<Conseiller, Long> {}
