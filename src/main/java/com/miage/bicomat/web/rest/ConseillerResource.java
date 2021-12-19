package com.miage.bicomat.web.rest;

import com.miage.bicomat.domain.Conseiller;
import com.miage.bicomat.repository.ConseillerRepository;
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
 * REST controller for managing {@link com.miage.bicomat.domain.Conseiller}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConseillerResource {

    private final Logger log = LoggerFactory.getLogger(ConseillerResource.class);

    private static final String ENTITY_NAME = "conseiller";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConseillerRepository conseillerRepository;

    public ConseillerResource(ConseillerRepository conseillerRepository) {
        this.conseillerRepository = conseillerRepository;
    }

    /**
     * {@code POST  /conseillers} : Create a new conseiller.
     *
     * @param conseiller the conseiller to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conseiller, or with status {@code 400 (Bad Request)} if the conseiller has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conseillers")
    public ResponseEntity<Conseiller> createConseiller(@RequestBody Conseiller conseiller) throws URISyntaxException {
        log.debug("REST request to save Conseiller : {}", conseiller);
        if (conseiller.getId() != null) {
            throw new BadRequestAlertException("A new conseiller cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conseiller result = conseillerRepository.save(conseiller);
        return ResponseEntity
            .created(new URI("/api/conseillers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conseillers/:id} : Updates an existing conseiller.
     *
     * @param id the id of the conseiller to save.
     * @param conseiller the conseiller to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conseiller,
     * or with status {@code 400 (Bad Request)} if the conseiller is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conseiller couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conseillers/{id}")
    public ResponseEntity<Conseiller> updateConseiller(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Conseiller conseiller
    ) throws URISyntaxException {
        log.debug("REST request to update Conseiller : {}, {}", id, conseiller);
        if (conseiller.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conseiller.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conseillerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Conseiller result = conseillerRepository.save(conseiller);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conseiller.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conseillers/:id} : Partial updates given fields of an existing conseiller, field will ignore if it is null
     *
     * @param id the id of the conseiller to save.
     * @param conseiller the conseiller to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conseiller,
     * or with status {@code 400 (Bad Request)} if the conseiller is not valid,
     * or with status {@code 404 (Not Found)} if the conseiller is not found,
     * or with status {@code 500 (Internal Server Error)} if the conseiller couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conseillers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Conseiller> partialUpdateConseiller(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Conseiller conseiller
    ) throws URISyntaxException {
        log.debug("REST request to partial update Conseiller partially : {}, {}", id, conseiller);
        if (conseiller.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conseiller.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conseillerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Conseiller> result = conseillerRepository
            .findById(conseiller.getId())
            .map(
                existingConseiller -> {
                    if (conseiller.getNom() != null) {
                        existingConseiller.setNom(conseiller.getNom());
                    }
                    if (conseiller.getPrenom() != null) {
                        existingConseiller.setPrenom(conseiller.getPrenom());
                    }

                    return existingConseiller;
                }
            )
            .map(conseillerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conseiller.getId().toString())
        );
    }

    /**
     * {@code GET  /conseillers} : get all the conseillers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conseillers in body.
     */
    @GetMapping("/conseillers")
    public List<Conseiller> getAllConseillers() {
        log.debug("REST request to get all Conseillers");
        return conseillerRepository.findAll();
    }

    /**
     * {@code GET  /conseillers/:id} : get the "id" conseiller.
     *
     * @param id the id of the conseiller to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conseiller, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conseillers/{id}")
    public ResponseEntity<Conseiller> getConseiller(@PathVariable Long id) {
        log.debug("REST request to get Conseiller : {}", id);
        Optional<Conseiller> conseiller = conseillerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(conseiller);
    }

    /**
     * {@code DELETE  /conseillers/:id} : delete the "id" conseiller.
     *
     * @param id the id of the conseiller to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conseillers/{id}")
    public ResponseEntity<Void> deleteConseiller(@PathVariable Long id) {
        log.debug("REST request to delete Conseiller : {}", id);
        conseillerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
