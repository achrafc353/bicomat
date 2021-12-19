package com.miage.bicomat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Banque.
 */
@Entity
@Table(name = "banque")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Banque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "adresse")
    private String adresse;

    @OneToMany(mappedBy = "banque")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "banque" }, allowSetters = true)
    private Set<Compte> comptes = new HashSet<>();

    @OneToMany(mappedBy = "banque")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tiers", "cartes", "comptes", "operations", "conseiller", "banque" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Banque id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Banque nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Banque adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Set<Compte> getComptes() {
        return this.comptes;
    }

    public Banque comptes(Set<Compte> comptes) {
        this.setComptes(comptes);
        return this;
    }

    public Banque addComptes(Compte compte) {
        this.comptes.add(compte);
        compte.setBanque(this);
        return this;
    }

    public Banque removeComptes(Compte compte) {
        this.comptes.remove(compte);
        compte.setBanque(null);
        return this;
    }

    public void setComptes(Set<Compte> comptes) {
        if (this.comptes != null) {
            this.comptes.forEach(i -> i.setBanque(null));
        }
        if (comptes != null) {
            comptes.forEach(i -> i.setBanque(this));
        }
        this.comptes = comptes;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public Banque clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Banque addClients(Client client) {
        this.clients.add(client);
        client.setBanque(this);
        return this;
    }

    public Banque removeClients(Client client) {
        this.clients.remove(client);
        client.setBanque(null);
        return this;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.setBanque(null));
        }
        if (clients != null) {
            clients.forEach(i -> i.setBanque(this));
        }
        this.clients = clients;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Banque)) {
            return false;
        }
        return id != null && id.equals(((Banque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Banque{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", adresse='" + getAdresse() + "'" +
            "}";
    }
}
