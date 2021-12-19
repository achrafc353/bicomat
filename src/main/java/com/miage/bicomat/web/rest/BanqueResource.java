package com.miage.bicomat.web.rest;

import com.miage.bicomat.domain.Banque;
import com.miage.bicomat.repository.BanqueRepository;
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
 * REST controller for managing {@link com.miage.bicomat.domain.Banque}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BanqueResource {

    private final Logger log = LoggerFactory.getLogger(BanqueResource.class);

    private static final String ENTITY_NAME = "banque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BanqueRepository banqueRepository;

    public BanqueResource(BanqueRepository banqueRepository) {
        this.banqueRepository = banqueRepository;
    }

    /**
     * {@code POST  /banques} : Create a new banque.
     *
     * @param banque the banque to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new banque, or with status {@code 400 (Bad Request)} if the banque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/banques")
    public ResponseEntity<Banque> createBanque(@RequestBody Banque banque) throws URISyntaxException {
        log.debug("REST request to save Banque : {}", banque);
        if (banque.getId() != null) {
            throw new BadRequestAlertException("A new banque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Banque result = banqueRepository.save(banque);
        return ResponseEntity
            .created(new URI("/api/banques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /banques/:id} : Updates an existing banque.
     *
     * @param id the id of the banque to save.
     * @param banque the banque to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated banque,
     * or with status {@code 400 (Bad Request)} if the banque is not valid,
     * or with status {@code 500 (Internal Server Error)} if the banque couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/banques/{id}")
    public ResponseEntity<Banque> updateBanque(@PathVariable(value = "id", required = false) final Long id, @RequestBody Banque banque)
        throws URISyntaxException {
        log.debug("REST request to update Banque : {}, {}", id, banque);
        if (banque.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, banque.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!banqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Banque result = banqueRepository.save(banque);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, banque.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /banques/:id} : Partial updates given fields of an existing banque, field will ignore if it is null
     *
     * @param id the id of the banque to save.
     * @param banque the banque to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated banque,
     * or with status {@code 400 (Bad Request)} if the banque is not valid,
     * or with status {@code 404 (Not Found)} if the banque is not found,
     * or with status {@code 500 (Internal Server Error)} if the banque couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/banques/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Banque> partialUpdateBanque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Banque banque
    ) throws URISyntaxException {
        log.debug("REST request to partial update Banque partially : {}, {}", id, banque);
        if (banque.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, banque.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!banqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Banque> result = banqueRepository
            .findById(banque.getId())
            .map(
                existingBanque -> {
                    if (banque.getNom() != null) {
                        existingBanque.setNom(banque.getNom());
                    }
                    if (banque.getAdresse() != null) {
                        existingBanque.setAdresse(banque.getAdresse());
                    }

                    return existingBanque;
                }
            )
            .map(banqueRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, banque.getId().toString())
        );
    }

    /**
     * {@code GET  /banques} : get all the banques.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of banques in body.
     */
    @GetMapping("/banques")
    public List<Banque> getAllBanques() {
        log.debug("REST request to get all Banques");
        return banqueRepository.findAll();
    }

    /**
     * {@code GET  /banques/:id} : get the "id" banque.
     *
     * @param id the id of the banque to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the banque, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/banques/{id}")
    public ResponseEntity<Banque> getBanque(@PathVariable Long id) {
        log.debug("REST request to get Banque : {}", id);
        Optional<Banque> banque = banqueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(banque);
    }

    /**
     * {@code DELETE  /banques/:id} : delete the "id" banque.
     *
     * @param id the id of the banque to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/banques/{id}")
    public ResponseEntity<Void> deleteBanque(@PathVariable Long id) {
        log.debug("REST request to delete Banque : {}", id);
        banqueRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
