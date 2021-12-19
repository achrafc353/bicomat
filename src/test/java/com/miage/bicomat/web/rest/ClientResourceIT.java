package com.miage.bicomat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.miage.bicomat.IntegrationTest;
import com.miage.bicomat.domain.Client;
import com.miage.bicomat.domain.enumeration.TypeClient2;
import com.miage.bicomat.repository.ClientRepository;
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
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_ADMEL = "AAAAAAAAAA";
    private static final String UPDATED_ADMEL = "BBBBBBBBBB";

    private static final TypeClient2 DEFAULT_TYPE = TypeClient2.PRIVE;
    private static final TypeClient2 UPDATED_TYPE = TypeClient2.MORAL;

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_MOT_DE_PASSE = "AAAAAAAAAA";
    private static final String UPDATED_MOT_DE_PASSE = "BBBBBBBBBB";

    private static final Instant DEFAULT_ANNEE_ARRIVEE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ANNEE_ARRIVEE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NUM_CONTRAT = "AAAAAAAAAA";
    private static final String UPDATED_NUM_CONTRAT = "BBBBBBBBBB";

    private static final String DEFAULT_AGENCY = "AAAAAAAAAA";
    private static final String UPDATED_AGENCY = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_PORTABLE = "AAAAAAAAAA";
    private static final String UPDATED_NUM_PORTABLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .admel(DEFAULT_ADMEL)
            .type(DEFAULT_TYPE)
            .login(DEFAULT_LOGIN)
            .motDePasse(DEFAULT_MOT_DE_PASSE)
            .anneeArrivee(DEFAULT_ANNEE_ARRIVEE)
            .numContrat(DEFAULT_NUM_CONTRAT)
            .agency(DEFAULT_AGENCY)
            .numPortable(DEFAULT_NUM_PORTABLE);
        return client;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity(EntityManager em) {
        Client client = new Client()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .admel(UPDATED_ADMEL)
            .type(UPDATED_TYPE)
            .login(UPDATED_LOGIN)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .anneeArrivee(UPDATED_ANNEE_ARRIVEE)
            .numContrat(UPDATED_NUM_CONTRAT)
            .agency(UPDATED_AGENCY)
            .numPortable(UPDATED_NUM_PORTABLE);
        return client;
    }

    @BeforeEach
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();
        // Create the Client
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testClient.getAdmel()).isEqualTo(DEFAULT_ADMEL);
        assertThat(testClient.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testClient.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testClient.getMotDePasse()).isEqualTo(DEFAULT_MOT_DE_PASSE);
        assertThat(testClient.getAnneeArrivee()).isEqualTo(DEFAULT_ANNEE_ARRIVEE);
        assertThat(testClient.getNumContrat()).isEqualTo(DEFAULT_NUM_CONTRAT);
        assertThat(testClient.getAgency()).isEqualTo(DEFAULT_AGENCY);
        assertThat(testClient.getNumPortable()).isEqualTo(DEFAULT_NUM_PORTABLE);
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId(1L);

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].admel").value(hasItem(DEFAULT_ADMEL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].motDePasse").value(hasItem(DEFAULT_MOT_DE_PASSE)))
            .andExpect(jsonPath("$.[*].anneeArrivee").value(hasItem(DEFAULT_ANNEE_ARRIVEE.toString())))
            .andExpect(jsonPath("$.[*].numContrat").value(hasItem(DEFAULT_NUM_CONTRAT)))
            .andExpect(jsonPath("$.[*].agency").value(hasItem(DEFAULT_AGENCY)))
            .andExpect(jsonPath("$.[*].numPortable").value(hasItem(DEFAULT_NUM_PORTABLE)));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.admel").value(DEFAULT_ADMEL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.motDePasse").value(DEFAULT_MOT_DE_PASSE))
            .andExpect(jsonPath("$.anneeArrivee").value(DEFAULT_ANNEE_ARRIVEE.toString()))
            .andExpect(jsonPath("$.numContrat").value(DEFAULT_NUM_CONTRAT))
            .andExpect(jsonPath("$.agency").value(DEFAULT_AGENCY))
            .andExpect(jsonPath("$.numPortable").value(DEFAULT_NUM_PORTABLE));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).get();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .admel(UPDATED_ADMEL)
            .type(UPDATED_TYPE)
            .login(UPDATED_LOGIN)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .anneeArrivee(UPDATED_ANNEE_ARRIVEE)
            .numContrat(UPDATED_NUM_CONTRAT)
            .agency(UPDATED_AGENCY)
            .numPortable(UPDATED_NUM_PORTABLE);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testClient.getAdmel()).isEqualTo(UPDATED_ADMEL);
        assertThat(testClient.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testClient.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testClient.getMotDePasse()).isEqualTo(UPDATED_MOT_DE_PASSE);
        assertThat(testClient.getAnneeArrivee()).isEqualTo(UPDATED_ANNEE_ARRIVEE);
        assertThat(testClient.getNumContrat()).isEqualTo(UPDATED_NUM_CONTRAT);
        assertThat(testClient.getAgency()).isEqualTo(UPDATED_AGENCY);
        assertThat(testClient.getNumPortable()).isEqualTo(UPDATED_NUM_PORTABLE);
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, client.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .admel(UPDATED_ADMEL)
            .type(UPDATED_TYPE)
            .login(UPDATED_LOGIN)
            .anneeArrivee(UPDATED_ANNEE_ARRIVEE)
            .agency(UPDATED_AGENCY)
            .numPortable(UPDATED_NUM_PORTABLE);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testClient.getAdmel()).isEqualTo(UPDATED_ADMEL);
        assertThat(testClient.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testClient.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testClient.getMotDePasse()).isEqualTo(DEFAULT_MOT_DE_PASSE);
        assertThat(testClient.getAnneeArrivee()).isEqualTo(UPDATED_ANNEE_ARRIVEE);
        assertThat(testClient.getNumContrat()).isEqualTo(DEFAULT_NUM_CONTRAT);
        assertThat(testClient.getAgency()).isEqualTo(UPDATED_AGENCY);
        assertThat(testClient.getNumPortable()).isEqualTo(UPDATED_NUM_PORTABLE);
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .admel(UPDATED_ADMEL)
            .type(UPDATED_TYPE)
            .login(UPDATED_LOGIN)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .anneeArrivee(UPDATED_ANNEE_ARRIVEE)
            .numContrat(UPDATED_NUM_CONTRAT)
            .agency(UPDATED_AGENCY)
            .numPortable(UPDATED_NUM_PORTABLE);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testClient.getAdmel()).isEqualTo(UPDATED_ADMEL);
        assertThat(testClient.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testClient.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testClient.getMotDePasse()).isEqualTo(UPDATED_MOT_DE_PASSE);
        assertThat(testClient.getAnneeArrivee()).isEqualTo(UPDATED_ANNEE_ARRIVEE);
        assertThat(testClient.getNumContrat()).isEqualTo(UPDATED_NUM_CONTRAT);
        assertThat(testClient.getAgency()).isEqualTo(UPDATED_AGENCY);
        assertThat(testClient.getNumPortable()).isEqualTo(UPDATED_NUM_PORTABLE);
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, client.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
