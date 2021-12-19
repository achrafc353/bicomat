package com.miage.bicomat.web.rest;

import com.miage.bicomat.domain.Tiers;
import com.miage.bicomat.repository.TiersRepository;
import com.miage.bicomat.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.miage.bicomat.domain.Tiers}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TiersResource {

    private final Logger log = LoggerFactory.getLogger(TiersResource.class);

    private static final String ENTITY_NAME = "tiers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TiersRepository tiersRepository;

    public TiersResource(TiersRepository tiersRepository) {
        this.tiersRepository = tiersRepository;
    }

    /**
     * {@code POST  /tiers} : Create a new tiers.
     *
     * @param tiers the tiers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tiers, or with status {@code 400 (Bad Request)} if the tiers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tiers")
    public ResponseEntity<Tiers> createTiers(@RequestBody Tiers tiers) throws URISyntaxException {
        log.debug("REST request to save Tiers : {}", tiers);
        if (tiers.getId() != null) {
            throw new BadRequestAlertException("A new tiers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tiers result = tiersRepository.save(tiers);
        return ResponseEntity
            .created(new URI("/api/tiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tiers/:id} : Updates an existing tiers.
     *
     * @param id the id of the tiers to save.
     * @param tiers the tiers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tiers,
     * or with status {@code 400 (Bad Request)} if the tiers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tiers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tiers/{id}")
    public ResponseEntity<Tiers> updateTiers(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tiers tiers)
        throws URISyntaxException {
        log.debug("REST request to update Tiers : {}, {}", id, tiers);
        if (tiers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tiers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tiersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tiers result = tiersRepository.save(tiers);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tiers.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tiers/:id} : Partial updates given fields of an existing tiers, field will ignore if it is null
     *
     * @param id the id of the tiers to save.
     * @param tiers the tiers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tiers,
     * or with status {@code 400 (Bad Request)} if the tiers is not valid,
     * or with status {@code 404 (Not Found)} if the tiers is not found,
     * or with status {@code 500 (Internal Server Error)} if the tiers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tiers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Tiers> partialUpdateTiers(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tiers tiers)
        throws URISyntaxException {
        log.debug("REST request to partial update Tiers partially : {}, {}", id, tiers);
        if (tiers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tiers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tiersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tiers> result = tiersRepository
            .findById(tiers.getId())
            .map(
                existingTiers -> {
                    if (tiers.getEtatTiers() != null) {
                        existingTiers.setEtatTiers(tiers.getEtatTiers());
                    }

                    return existingTiers;
                }
            )
            .map(tiersRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tiers.getId().toString())
        );
    }

    /**
     * {@code GET  /tiers} : get all the tiers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tiers in body.
     */
    @GetMapping("/tiers")
    public List<Tiers> getAllTiers() {
        log.debug("REST request to get all Tiers");
        return tiersRepository.findAll();
    }

    /**
     * {@code GET  /tiers/:id} : get the "id" tiers.
     *
     * @param id the id of the tiers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tiers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tiers/{id}")
    public ResponseEntity<Tiers> getTiers(@PathVariable Long id) {
        log.debug("REST request to get Tiers : {}", id);
        Optional<Tiers> tiers = tiersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tiers);
    }

    /**
     * {@code DELETE  /tiers/:id} : delete the "id" tiers.
     *
     * @param id the id of the tiers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tiers/{id}")
    public ResponseEntity<Void> deleteTiers(@PathVariable Long id) {
        log.debug("REST request to delete Tiers : {}", id);
        tiersRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
