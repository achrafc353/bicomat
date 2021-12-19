package com.miage.bicomat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.miage.bicomat.IntegrationTest;
import com.miage.bicomat.domain.Tiers;
import com.miage.bicomat.repository.TiersRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TiersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TiersResourceIT {

    private static final String DEFAULT_ETAT_TIERS = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_TIERS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TiersRepository tiersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTiersMockMvc;

    private Tiers tiers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tiers createEntity(EntityManager em) {
        Tiers tiers = new Tiers().etatTiers(DEFAULT_ETAT_TIERS);
        return tiers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tiers createUpdatedEntity(EntityManager em) {
        Tiers tiers = new Tiers().etatTiers(UPDATED_ETAT_TIERS);
        return tiers;
    }

    @BeforeEach
    public void initTest() {
        tiers = createEntity(em);
    }

    @Test
    @Transactional
    void createTiers() throws Exception {
        int databaseSizeBeforeCreate = tiersRepository.findAll().size();
        // Create the Tiers
        restTiersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiers)))
            .andExpect(status().isCreated());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeCreate + 1);
        Tiers testTiers = tiersList.get(tiersList.size() - 1);
        assertThat(testTiers.getEtatTiers()).isEqualTo(DEFAULT_ETAT_TIERS);
    }

    @Test
    @Transactional
    void createTiersWithExistingId() throws Exception {
        // Create the Tiers with an existing ID
        tiers.setId(1L);

        int databaseSizeBeforeCreate = tiersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTiersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiers)))
            .andExpect(status().isBadRequest());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTiers() throws Exception {
        // Initialize the database
        tiersRepository.saveAndFlush(tiers);

        // Get all the tiersList
        restTiersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tiers.getId().intValue())))
            .andExpect(jsonPath("$.[*].etatTiers").value(hasItem(DEFAULT_ETAT_TIERS)));
    }

    @Test
    @Transactional
    void getTiers() throws Exception {
        // Initialize the database
        tiersRepository.saveAndFlush(tiers);

        // Get the tiers
        restTiersMockMvc
            .perform(get(ENTITY_API_URL_ID, tiers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tiers.getId().intValue()))
            .andExpect(jsonPath("$.etatTiers").value(DEFAULT_ETAT_TIERS));
    }

    @Test
    @Transactional
    void getNonExistingTiers() throws Exception {
        // Get the tiers
        restTiersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTiers() throws Exception {
        // Initialize the database
        tiersRepository.saveAndFlush(tiers);

        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();

        // Update the tiers
        Tiers updatedTiers = tiersRepository.findById(tiers.getId()).get();
        // Disconnect from session so that the updates on updatedTiers are not directly saved in db
        em.detach(updatedTiers);
        updatedTiers.etatTiers(UPDATED_ETAT_TIERS);

        restTiersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTiers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTiers))
            )
            .andExpect(status().isOk());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
        Tiers testTiers = tiersList.get(tiersList.size() - 1);
        assertThat(testTiers.getEtatTiers()).isEqualTo(UPDATED_ETAT_TIERS);
    }

    @Test
    @Transactional
    void putNonExistingTiers() throws Exception {
        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();
        tiers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTiersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tiers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tiers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTiers() throws Exception {
        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();
        tiers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tiers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTiers() throws Exception {
        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();
        tiers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tiers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTiersWithPatch() throws Exception {
        // Initialize the database
        tiersRepository.saveAndFlush(tiers);

        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();

        // Update the tiers using partial update
        Tiers partialUpdatedTiers = new Tiers();
        partialUpdatedTiers.setId(tiers.getId());

        restTiersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTiers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTiers))
            )
            .andExpect(status().isOk());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
        Tiers testTiers = tiersList.get(tiersList.size() - 1);
        assertThat(testTiers.getEtatTiers()).isEqualTo(DEFAULT_ETAT_TIERS);
    }

    @Test
    @Transactional
    void fullUpdateTiersWithPatch() throws Exception {
        // Initialize the database
        tiersRepository.saveAndFlush(tiers);

        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();

        // Update the tiers using partial update
        Tiers partialUpdatedTiers = new Tiers();
        partialUpdatedTiers.setId(tiers.getId());

        partialUpdatedTiers.etatTiers(UPDATED_ETAT_TIERS);

        restTiersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTiers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTiers))
            )
            .andExpect(status().isOk());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
        Tiers testTiers = tiersList.get(tiersList.size() - 1);
        assertThat(testTiers.getEtatTiers()).isEqualTo(UPDATED_ETAT_TIERS);
    }

    @Test
    @Transactional
    void patchNonExistingTiers() throws Exception {
        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();
        tiers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTiersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tiers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tiers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTiers() throws Exception {
        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();
        tiers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tiers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTiers() throws Exception {
        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();
        tiers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tiers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTiers() throws Exception {
        // Initialize the database
        tiersRepository.saveAndFlush(tiers);

        int databaseSizeBeforeDelete = tiersRepository.findAll().size();

        // Delete the tiers
        restTiersMockMvc
            .perform(delete(ENTITY_API_URL_ID, tiers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
