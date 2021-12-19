package com.miage.bicomat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miage.bicomat.domain.enumeration.TypeCompte;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Compte.
 */
@Entity
@Table(name = "compte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeCompte type;

    @Column(name = "solde")
    private Long solde;

    @Column(name = "decouvert")
    private Long decouvert;

    @Column(name = "taux_renumeration")
    private Long tauxRenumeration;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tiers", "cartes", "comptes", "operations", "conseiller", "banque" }, allowSetters = true)
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties(value = { "comptes", "clients" }, allowSetters = true)
    private Banque banque;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Compte id(Long id) {
        this.id = id;
        return this;
    }

    public String getNumero() {
        return this.numero;
    }

    public Compte numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TypeCompte getType() {
        return this.type;
    }

    public Compte type(TypeCompte type) {
        this.type = type;
        return this;
    }

    public void setType(TypeCompte type) {
        this.type = type;
    }

    public Long getSolde() {
        return this.solde;
    }

    public Compte solde(Long solde) {
        this.solde = solde;
        return this;
    }

    public void setSolde(Long solde) {
        this.solde = solde;
    }

    public Long getDecouvert() {
        return this.decouvert;
    }

    public Compte decouvert(Long decouvert) {
        this.decouvert = decouvert;
        return this;
    }

    public void setDecouvert(Long decouvert) {
        this.decouvert = decouvert;
    }

    public Long getTauxRenumeration() {
        return this.tauxRenumeration;
    }

    public Compte tauxRenumeration(Long tauxRenumeration) {
        this.tauxRenumeration = tauxRenumeration;
        return this;
    }

    public void setTauxRenumeration(Long tauxRenumeration) {
        this.tauxRenumeration = tauxRenumeration;
    }

    public Client getClient() {
        return this.client;
    }

    public Compte client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Banque getBanque() {
        return this.banque;
    }

    public Compte banque(Banque banque) {
        this.setBanque(banque);
        return this;
    }

    public void setBanque(Banque banque) {
        this.banque = banque;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compte)) {
            return false;
        }
        return id != null && id.equals(((Compte) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compte{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", type='" + getType() + "'" +
            ", solde=" + getSolde() +
            ", decouvert=" + getDecouvert() +
            ", tauxRenumeration=" + getTauxRenumeration() +
            "}";
    }
}
