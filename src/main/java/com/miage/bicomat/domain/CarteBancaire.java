package com.miage.bicomat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miage.bicomat.domain.enumeration.TypeCarte;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CarteBancaire.
 */
@Entity
@Table(name = "carte_bancaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CarteBancaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeCarte type;

    @Column(name = "echeance")
    private Instant echeance;

    @Column(name = "code_crypto")
    private String codeCrypto;

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

    public CarteBancaire id(Long id) {
        this.id = id;
        return this;
    }

    public String getNumero() {
        return this.numero;
    }

    public CarteBancaire numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TypeCarte getType() {
        return this.type;
    }

    public CarteBancaire type(TypeCarte type) {
        this.type = type;
        return this;
    }

    public void setType(TypeCarte type) {
        this.type = type;
    }

    public Instant getEcheance() {
        return this.echeance;
    }

    public CarteBancaire echeance(Instant echeance) {
        this.echeance = echeance;
        return this;
    }

    public void setEcheance(Instant echeance) {
        this.echeance = echeance;
    }

    public String getCodeCrypto() {
        return this.codeCrypto;
    }

    public CarteBancaire codeCrypto(String codeCrypto) {
        this.codeCrypto = codeCrypto;
        return this;
    }

    public void setCodeCrypto(String codeCrypto) {
        this.codeCrypto = codeCrypto;
    }

    public Client getClient() {
        return this.client;
    }

    public CarteBancaire client(Client client) {
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
        if (!(o instanceof CarteBancaire)) {
            return false;
        }
        return id != null && id.equals(((CarteBancaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarteBancaire{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", type='" + getType() + "'" +
            ", echeance='" + getEcheance() + "'" +
            ", codeCrypto='" + getCodeCrypto() + "'" +
            "}";
    }
}
