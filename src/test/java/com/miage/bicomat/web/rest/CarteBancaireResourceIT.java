package com.miage.bicomat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.miage.bicomat.IntegrationTest;
import com.miage.bicomat.domain.CarteBancaire;
import com.miage.bicomat.domain.enumeration.TypeCarte;
import com.miage.bicomat.repository.CarteBancaireRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link CarteBancaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarteBancaireResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final TypeCarte DEFAULT_TYPE = TypeCarte.VISA;
    private static final TypeCarte UPDATED_TYPE = TypeCarte.MASTERCARD;

    private static final Instant DEFAULT_ECHEANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ECHEANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE_CRYPTO = "AAAAAAAAAA";
    private static final String UPDATED_CODE_CRYPTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/carte-bancaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarteBancaireRepository carteBancaireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarteBancaireMockMvc;

    private CarteBancaire carteBancaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarteBancaire createEntity(EntityManager em) {
        CarteBancaire carteBancaire = new CarteBancaire()
            .numero(DEFAULT_NUMERO)
            .type(DEFAULT_TYPE)
            .echeance(DEFAULT_ECHEANCE)
            .codeCrypto(DEFAULT_CODE_CRYPTO);
        return carteBancaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarteBancaire createUpdatedEntity(EntityManager em) {
        CarteBancaire carteBancaire = new CarteBancaire()
            .numero(UPDATED_NUMERO)
            .type(UPDATED_TYPE)
            .echeance(UPDATED_ECHEANCE)
            .codeCrypto(UPDATED_CODE_CRYPTO);
        return carteBancaire;
    }

    @BeforeEach
    public void initTest() {
        carteBancaire = createEntity(em);
    }

    @Test
    @Transactional
    void createCarteBancaire() throws Exception {
        int databaseSizeBeforeCreate = carteBancaireRepository.findAll().size();
        // Create the CarteBancaire
        restCarteBancaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carteBancaire)))
            .andExpect(status().isCreated());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeCreate + 1);
        CarteBancaire testCarteBancaire = carteBancaireList.get(carteBancaireList.size() - 1);
        assertThat(testCarteBancaire.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testCarteBancaire.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCarteBancaire.getEcheance()).isEqualTo(DEFAULT_ECHEANCE);
        assertThat(testCarteBancaire.getCodeCrypto()).isEqualTo(DEFAULT_CODE_CRYPTO);
    }

    @Test
    @Transactional
    void createCarteBancaireWithExistingId() throws Exception {
        // Create the CarteBancaire with an existing ID
        carteBancaire.setId(1L);

        int databaseSizeBeforeCreate = carteBancaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarteBancaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carteBancaire)))
            .andExpect(status().isBadRequest());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCarteBancaires() throws Exception {
        // Initialize the database
        carteBancaireRepository.saveAndFlush(carteBancaire);

        // Get all the carteBancaireList
        restCarteBancaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carteBancaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].echeance").value(hasItem(DEFAULT_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].codeCrypto").value(hasItem(DEFAULT_CODE_CRYPTO)));
    }

    @Test
    @Transactional
    void getCarteBancaire() throws Exception {
        // Initialize the database
        carteBancaireRepository.saveAndFlush(carteBancaire);

        // Get the carteBancaire
        restCarteBancaireMockMvc
            .perform(get(ENTITY_API_URL_ID, carteBancaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carteBancaire.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.echeance").value(DEFAULT_ECHEANCE.toString()))
            .andExpect(jsonPath("$.codeCrypto").value(DEFAULT_CODE_CRYPTO));
    }

    @Test
    @Transactional
    void getNonExistingCarteBancaire() throws Exception {
        // Get the carteBancaire
        restCarteBancaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCarteBancaire() throws Exception {
        // Initialize the database
        carteBancaireRepository.saveAndFlush(carteBancaire);

        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().size();

        // Update the carteBancaire
        CarteBancaire updatedCarteBancaire = carteBancaireRepository.findById(carteBancaire.getId()).get();
        // Disconnect from session so that the updates on updatedCarteBancaire are not directly saved in db
        em.detach(updatedCarteBancaire);
        updatedCarteBancaire.numero(UPDATED_NUMERO).type(UPDATED_TYPE).echeance(UPDATED_ECHEANCE).codeCrypto(UPDATED_CODE_CRYPTO);

        restCarteBancaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCarteBancaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCarteBancaire))
            )
            .andExpect(status().isOk());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
        CarteBancaire testCarteBancaire = carteBancaireList.get(carteBancaireList.size() - 1);
        assertThat(testCarteBancaire.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCarteBancaire.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCarteBancaire.getEcheance()).isEqualTo(UPDATED_ECHEANCE);
        assertThat(testCarteBancaire.getCodeCrypto()).isEqualTo(UPDATED_CODE_CRYPTO);
    }

    @Test
    @Transactional
    void putNonExistingCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().size();
        carteBancaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarteBancaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carteBancaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carteBancaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().size();
        carteBancaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteBancaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carteBancaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().size();
        carteBancaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteBancaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carteBancaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarteBancaireWithPatch() throws Exception {
        // Initialize the database
        carteBancaireRepository.saveAndFlush(carteBancaire);

        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().size();

        // Update the carteBancaire using partial update
        CarteBancaire partialUpdatedCarteBancaire = new CarteBancaire();
        partialUpdatedCarteBancaire.setId(carteBancaire.getId());

        partialUpdatedCarteBancaire.type(UPDATED_TYPE).echeance(UPDATED_ECHEANCE).codeCrypto(UPDATED_CODE_CRYPTO);

        restCarteBancaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarteBancaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarteBancaire))
            )
            .andExpect(status().isOk());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
        CarteBancaire testCarteBancaire = carteBancaireList.get(carteBancaireList.size() - 1);
        assertThat(testCarteBancaire.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testCarteBancaire.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCarteBancaire.getEcheance()).isEqualTo(UPDATED_ECHEANCE);
        assertThat(testCarteBancaire.getCodeCrypto()).isEqualTo(UPDATED_CODE_CRYPTO);
    }

    @Test
    @Transactional
    void fullUpdateCarteBancaireWithPatch() throws Exception {
        // Initialize the database
        carteBancaireRepository.saveAndFlush(carteBancaire);

        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().size();

        // Update the carteBancaire using partial update
        CarteBancaire partialUpdatedCarteBancaire = new CarteBancaire();
        partialUpdatedCarteBancaire.setId(carteBancaire.getId());

        partialUpdatedCarteBancaire.numero(UPDATED_NUMERO).type(UPDATED_TYPE).echeance(UPDATED_ECHEANCE).codeCrypto(UPDATED_CODE_CRYPTO);

        restCarteBancaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarteBancaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarteBancaire))
            )
            .andExpect(status().isOk());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
        CarteBancaire testCarteBancaire = carteBancaireList.get(carteBancaireList.size() - 1);
        assertThat(testCarteBancaire.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCarteBancaire.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCarteBancaire.getEcheance()).isEqualTo(UPDATED_ECHEANCE);
        assertThat(testCarteBancaire.getCodeCrypto()).isEqualTo(UPDATED_CODE_CRYPTO);
    }

    @Test
    @Transactional
    void patchNonExistingCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().size();
        carteBancaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarteBancaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carteBancaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carteBancaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().size();
        carteBancaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteBancaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carteBancaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarteBancaire() throws Exception {
        int databaseSizeBeforeUpdate = carteBancaireRepository.findAll().size();
        carteBancaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteBancaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(carteBancaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarteBancaire in the database
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarteBancaire() throws Exception {
        // Initialize the database
        carteBancaireRepository.saveAndFlush(carteBancaire);

        int databaseSizeBeforeDelete = carteBancaireRepository.findAll().size();

        // Delete the carteBancaire
        restCarteBancaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, carteBancaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarteBancaire> carteBancaireList = carteBancaireRepository.findAll();
        assertThat(carteBancaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
