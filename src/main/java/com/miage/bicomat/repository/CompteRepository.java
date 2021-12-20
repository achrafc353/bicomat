package com.miage.bicomat.repository;

import ch.qos.logback.core.net.server.Client;
import com.miage.bicomat.domain.Compte;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Compte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    List<Compte> findAllByClientId(Long clientId);
}
