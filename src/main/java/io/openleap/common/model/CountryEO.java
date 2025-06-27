/*
 * This file is part of this software project.
 *
 *  Copyright (C) 2025 Dr.-Ing. Sören Kemmann
 *
 * This software is dual-licensed under:
 *
 * 1. The European Union Public License v.1.2 (EUPL)
 *    https://joinup.ec.europa.eu/collection/eupl
 *
 *     You may use, modify and redistribute this file under the terms of the EUPL.
 *
 *  2. A commercial license available from:
 *
 *     B+B Unternehmensberatung GmbH & Co.KG
 *     Robert-Bunsen-Straße 10
 *     67098 Bad Dürkheim
 *     Germany
 *     Contact: license@bb-online.de
 *
 *  You may choose which license to apply.
 */

package io.openleap.common.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Map;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "common_iso_3166_1_country",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_country_numeric", columnNames = {"numeric_code"}),
                @UniqueConstraint(name = "uc_country_alpha2", columnNames = {"alpha2_code"}),
                @UniqueConstraint(name = "uc_country_alpha3", columnNames = {"alpha3_code"})
        }

)
public class CountryEO extends OlEntity {

    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "alpha2_code", nullable = false, length = 2)
    private String alpha2Code;

    @NotBlank
    @Size(min = 3, max = 3)
    @Column(name = "alpha3_code", nullable = false, length = 3)
    private String alpha3Code;

    @NotNull
    @Column(name = "numeric_code", nullable = false)
    private Integer numericCode;

    @Column(name = "region")
    private String region;

    @Column(name = "subregion")
    private String subRegion;

    @Embedded
    private Name name;

    /**
     * top-level "name" structure containing common, official, and native translations
     */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class Name {

        @NotBlank
        @Column(name = "name_common", nullable = false)
        private String common;

        @NotBlank
        @Column(name = "name_official", nullable = false)
        private String official;

    }


}
