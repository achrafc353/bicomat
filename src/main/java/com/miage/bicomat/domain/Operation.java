package com.miage.bicomat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miage.bicomat.domain.enumeration.TypeOperation;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Operation.
 */
@Entity
@Table(name = "operation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "date")
    private Instant date;

    @Column(name = "montant")
    private Long montant;

    @Column(name = "signe")
    private Long signe;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeOperation type;

    @Column(name = "echeance")
    private Instant echeance;

    @JsonIgnoreProperties(value = { "operationLiee", "compte", "client" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Operation operationLiee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "client", "banque" }, allowSetters = true)
    private Compte compte;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tiers", "cartes", "comptes", "operations", "conseiller", "banque" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Operation id(Long id) {
        this.id = id;
        return this;
    }

    public String getNumero() {
        return this.numero;
    }

    public Operation numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Instant getDate() {
        return this.date;
    }

    public Operation date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Long getMontant() {
        return this.montant;
    }

    public Operation montant(Long montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public Long getSigne() {
        return this.signe;
    }

    public Operation signe(Long signe) {
        this.signe = signe;
        return this;
    }

    public void setSigne(Long signe) {
        this.signe = signe;
    }

    public TypeOperation getType() {
        return this.type;
    }

    public Operation type(TypeOperation type) {
        this.type = type;
        return this;
    }

    public void setType(TypeOperation type) {
        this.type = type;
    }

    public Instant getEcheance() {
        return this.echeance;
    }

    public Operation echeance(Instant echeance) {
        this.echeance = echeance;
        return this;
    }

    public void setEcheance(Instant echeance) {
        this.echeance = echeance;
    }

    public Operation getOperationLiee() {
        return this.operationLiee;
    }

    public Operation operationLiee(Operation operation) {
        this.setOperationLiee(operation);
        return this;
    }

    public void setOperationLiee(Operation operation) {
        this.operationLiee = operation;
    }

    public Compte getCompte() {
        return this.compte;
    }

    public Operation compte(Compte compte) {
        this.setCompte(compte);
        return this;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Client getClient() {
        return this.client;
    }

    public Operation client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operation)) {
            return false;
        }
        return id != null && id.equals(((Operation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operation{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", date='" + getDate() + "'" +
            ", montant=" + getMontant() +
            ", signe=" + getSigne() +
            ", type='" + getType() + "'" +
            ", echeance='" + getEcheance() + "'" +
            "}";
    }
}
