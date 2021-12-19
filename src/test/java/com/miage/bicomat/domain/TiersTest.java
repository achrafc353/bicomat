package com.miage.bicomat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.miage.bicomat.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TiersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tiers.class);
        Tiers tiers1 = new Tiers();
        tiers1.setId(1L);
        Tiers tiers2 = new Tiers();
        tiers2.setId(tiers1.getId());
        assertThat(tiers1).isEqualTo(tiers2);
        tiers2.setId(2L);
        assertThat(tiers1).isNotEqualTo(tiers2);
        tiers1.setId(null);
        assertThat(tiers1).isNotEqualTo(tiers2);
    }
}
