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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.misc.NotNull;

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
    @Column(name = "name", nullable = false)
    private String name;

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

    @Column(name = "iso_3166_2")
    private String iso3166_2;

    @Column(name = "region")
    private String region;

    @Column(name = "sub_region")
    private String subRegion;

    @Column(name = "intermediate_region")
    private String intermediateRegion;

    @Column(name = "region_code")
    private Integer regionCode;

    @Column(name = "sub_region_code")
    private Integer subRegionCode;

    @Column(name = "intermediate_region_code")
    private Integer intermediateRegionCode;
}
