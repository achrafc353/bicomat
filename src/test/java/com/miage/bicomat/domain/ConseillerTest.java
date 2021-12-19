package com.miage.bicomat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.miage.bicomat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConseillerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conseiller.class);
        Conseiller conseiller1 = new Conseiller();
        conseiller1.setId(1L);
        Conseiller conseiller2 = new Conseiller();
        conseiller2.setId(conseiller1.getId());
        assertThat(conseiller1).isEqualTo(conseiller2);
        conseiller2.setId(2L);
        assertThat(conseiller1).isNotEqualTo(conseiller2);
        conseiller1.setId(null);
        assertThat(conseiller1).isNotEqualTo(conseiller2);
    }
}
