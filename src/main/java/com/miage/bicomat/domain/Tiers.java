package com.miage.bicomat.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tiers.
 */
@Entity
@Table(name = "tiers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tiers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "etat_tiers")
    private String etatTiers;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tiers id(Long id) {
        this.id = id;
        return this;
    }

    public String getEtatTiers() {
        return this.etatTiers;
    }

    public Tiers etatTiers(String etatTiers) {
        this.etatTiers = etatTiers;
        return this;
    }

    public void setEtatTiers(String etatTiers) {
        this.etatTiers = etatTiers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tiers)) {
            return false;
        }
        return id != null && id.equals(((Tiers) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tiers{" +
            "id=" + getId() +
            ", etatTiers='" + getEtatTiers() + "'" +
            "}";
    }
}
