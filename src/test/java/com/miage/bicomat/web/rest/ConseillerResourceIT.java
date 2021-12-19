package com.miage.bicomat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.miage.bicomat.IntegrationTest;
import com.miage.bicomat.domain.Conseiller;
import com.miage.bicomat.repository.ConseillerRepository;
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
 * Integration tests for the {@link ConseillerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConseillerResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/conseillers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConseillerRepository conseillerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConseillerMockMvc;

    private Conseiller conseiller;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conseiller createEntity(EntityManager em) {
        Conseiller conseiller = new Conseiller().nom(DEFAULT_NOM).prenom(DEFAULT_PRENOM);
        return conseiller;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conseiller createUpdatedEntity(EntityManager em) {
        Conseiller conseiller = new Conseiller().nom(UPDATED_NOM).prenom(UPDATED_PRENOM);
        return conseiller;
    }

    @BeforeEach
    public void initTest() {
        conseiller = createEntity(em);
    }

    @Test
    @Transactional
    void createConseiller() throws Exception {
        int databaseSizeBeforeCreate = conseillerRepository.findAll().size();
        // Create the Conseiller
        restConseillerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conseiller)))
            .andExpect(status().isCreated());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeCreate + 1);
        Conseiller testConseiller = conseillerList.get(conseillerList.size() - 1);
        assertThat(testConseiller.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testConseiller.getPrenom()).isEqualTo(DEFAULT_PRENOM);
    }

    @Test
    @Transactional
    void createConseillerWithExistingId() throws Exception {
        // Create the Conseiller with an existing ID
        conseiller.setId(1L);

        int databaseSizeBeforeCreate = conseillerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConseillerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conseiller)))
            .andExpect(status().isBadRequest());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConseillers() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);

        // Get all the conseillerList
        restConseillerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conseiller.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)));
    }

    @Test
    @Transactional
    void getConseiller() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);

        // Get the conseiller
        restConseillerMockMvc
            .perform(get(ENTITY_API_URL_ID, conseiller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conseiller.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM));
    }

    @Test
    @Transactional
    void getNonExistingConseiller() throws Exception {
        // Get the conseiller
        restConseillerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConseiller() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);

        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();

        // Update the conseiller
        Conseiller updatedConseiller = conseillerRepository.findById(conseiller.getId()).get();
        // Disconnect from session so that the updates on updatedConseiller are not directly saved in db
        em.detach(updatedConseiller);
        updatedConseiller.nom(UPDATED_NOM).prenom(UPDATED_PRENOM);

        restConseillerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConseiller.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConseiller))
            )
            .andExpect(status().isOk());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate);
        Conseiller testConseiller = conseillerList.get(conseillerList.size() - 1);
        assertThat(testConseiller.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testConseiller.getPrenom()).isEqualTo(UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void putNonExistingConseiller() throws Exception {
        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();
        conseiller.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConseillerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conseiller.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conseiller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConseiller() throws Exception {
        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();
        conseiller.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConseillerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conseiller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConseiller() throws Exception {
        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();
        conseiller.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConseillerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conseiller)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConseillerWithPatch() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);

        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();

        // Update the conseiller using partial update
        Conseiller partialUpdatedConseiller = new Conseiller();
        partialUpdatedConseiller.setId(conseiller.getId());

        partialUpdatedConseiller.prenom(UPDATED_PRENOM);

        restConseillerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConseiller.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConseiller))
            )
            .andExpect(status().isOk());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate);
        Conseiller testConseiller = conseillerList.get(conseillerList.size() - 1);
        assertThat(testConseiller.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testConseiller.getPrenom()).isEqualTo(UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void fullUpdateConseillerWithPatch() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);

        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();

        // Update the conseiller using partial update
        Conseiller partialUpdatedConseiller = new Conseiller();
        partialUpdatedConseiller.setId(conseiller.getId());

        partialUpdatedConseiller.nom(UPDATED_NOM).prenom(UPDATED_PRENOM);

        restConseillerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConseiller.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConseiller))
            )
            .andExpect(status().isOk());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate);
        Conseiller testConseiller = conseillerList.get(conseillerList.size() - 1);
        assertThat(testConseiller.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testConseiller.getPrenom()).isEqualTo(UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void patchNonExistingConseiller() throws Exception {
        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();
        conseiller.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConseillerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conseiller.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conseiller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConseiller() throws Exception {
        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();
        conseiller.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConseillerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conseiller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConseiller() throws Exception {
        int databaseSizeBeforeUpdate = conseillerRepository.findAll().size();
        conseiller.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConseillerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(conseiller))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conseiller in the database
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConseiller() throws Exception {
        // Initialize the database
        conseillerRepository.saveAndFlush(conseiller);

        int databaseSizeBeforeDelete = conseillerRepository.findAll().size();

        // Delete the conseiller
        restConseillerMockMvc
            .perform(delete(ENTITY_API_URL_ID, conseiller.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conseiller> conseillerList = conseillerRepository.findAll();
        assertThat(conseillerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
