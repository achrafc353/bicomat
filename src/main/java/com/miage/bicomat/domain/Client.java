package com.miage.bicomat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miage.bicomat.domain.enumeration.TypeClient2;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "admel")
    private String admel;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeClient2 type;

    @Column(name = "login")
    private String login;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Column(name = "annee_arrivee")
    private Instant anneeArrivee;

    @Column(name = "num_contrat")
    private String numContrat;

    @Column(name = "agency")
    private String agency;

    @Column(name = "num_portable")
    private String numPortable;

    @OneToOne
    @JoinColumn(unique = true)
    private Tiers tiers;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<CarteBancaire> cartes = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "banque" }, allowSetters = true)
    private Set<Compte> comptes = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operationLiee", "compte", "client" }, allowSetters = true)
    private Set<Operation> operations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "clients" }, allowSetters = true)
    private Conseiller conseiller;

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

    public Client id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Client nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Client prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdmel() {
        return this.admel;
    }

    public Client admel(String admel) {
        this.admel = admel;
        return this;
    }

    public void setAdmel(String admel) {
        this.admel = admel;
    }

    public TypeClient2 getType() {
        return this.type;
    }

    public Client type(TypeClient2 type) {
        this.type = type;
        return this;
    }

    public void setType(TypeClient2 type) {
        this.type = type;
    }

    public String getLogin() {
        return this.login;
    }

    public Client login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public Client motDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Instant getAnneeArrivee() {
        return this.anneeArrivee;
    }

    public Client anneeArrivee(Instant anneeArrivee) {
        this.anneeArrivee = anneeArrivee;
        return this;
    }

    public void setAnneeArrivee(Instant anneeArrivee) {
        this.anneeArrivee = anneeArrivee;
    }

    public String getNumContrat() {
        return this.numContrat;
    }

    public Client numContrat(String numContrat) {
        this.numContrat = numContrat;
        return this;
    }

    public void setNumContrat(String numContrat) {
        this.numContrat = numContrat;
    }

    public String getAgency() {
        return this.agency;
    }

    public Client agency(String agency) {
        this.agency = agency;
        return this;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getNumPortable() {
        return this.numPortable;
    }

    public Client numPortable(String numPortable) {
        this.numPortable = numPortable;
        return this;
    }

    public void setNumPortable(String numPortable) {
        this.numPortable = numPortable;
    }

    public Tiers getTiers() {
        return this.tiers;
    }

    public Client tiers(Tiers tiers) {
        this.setTiers(tiers);
        return this;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public Set<CarteBancaire> getCartes() {
        return this.cartes;
    }

    public Client cartes(Set<CarteBancaire> carteBancaires) {
        this.setCartes(carteBancaires);
        return this;
    }

    public Client addCartes(CarteBancaire carteBancaire) {
        this.cartes.add(carteBancaire);
        carteBancaire.setClient(this);
        return this;
    }

    public Client removeCartes(CarteBancaire carteBancaire) {
        this.cartes.remove(carteBancaire);
        carteBancaire.setClient(null);
        return this;
    }

    public void setCartes(Set<CarteBancaire> carteBancaires) {
        if (this.cartes != null) {
            this.cartes.forEach(i -> i.setClient(null));
        }
        if (carteBancaires != null) {
            carteBancaires.forEach(i -> i.setClient(this));
        }
        this.cartes = carteBancaires;
    }

    public Set<Compte> getComptes() {
        return this.comptes;
    }

    public Client comptes(Set<Compte> comptes) {
        this.setComptes(comptes);
        return this;
    }

    public Client addComptes(Compte compte) {
        this.comptes.add(compte);
        compte.setClient(this);
        return this;
    }

    public Client removeComptes(Compte compte) {
        this.comptes.remove(compte);
        compte.setClient(null);
        return this;
    }

    public void setComptes(Set<Compte> comptes) {
        if (this.comptes != null) {
            this.comptes.forEach(i -> i.setClient(null));
        }
        if (comptes != null) {
            comptes.forEach(i -> i.setClient(this));
        }
        this.comptes = comptes;
    }

    public Set<Operation> getOperations() {
        return this.operations;
    }

    public Client operations(Set<Operation> operations) {
        this.setOperations(operations);
        return this;
    }

    public Client addOperations(Operation operation) {
        this.operations.add(operation);
        operation.setClient(this);
        return this;
    }

    public Client removeOperations(Operation operation) {
        this.operations.remove(operation);
        operation.setClient(null);
        return this;
    }

    public void setOperations(Set<Operation> operations) {
        if (this.operations != null) {
            this.operations.forEach(i -> i.setClient(null));
        }
        if (operations != null) {
            operations.forEach(i -> i.setClient(this));
        }
        this.operations = operations;
    }

    public Conseiller getConseiller() {
        return this.conseiller;
    }

    public Client conseiller(Conseiller conseiller) {
        this.setConseiller(conseiller);
        return this;
    }

    public void setConseiller(Conseiller conseiller) {
        this.conseiller = conseiller;
    }

    public Banque getBanque() {
        return this.banque;
    }

    public Client banque(Banque banque) {
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
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", admel='" + getAdmel() + "'" +
            ", type='" + getType() + "'" +
            ", login='" + getLogin() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", anneeArrivee='" + getAnneeArrivee() + "'" +
            ", numContrat='" + getNumContrat() + "'" +
            ", agency='" + getAgency() + "'" +
            ", numPortable='" + getNumPortable() + "'" +
            "}";
    }
}
