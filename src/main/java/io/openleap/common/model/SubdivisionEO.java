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
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "common_iso_3166_2_subdivisions")
public class SubdivisionEO extends OlEntity {

    @NotBlank
    @Column(name = "country_code_alpha2", nullable = false, length = 2)
    private String countryCodeAlpha2;

    @NotBlank
    @Column(name = "subdivision_code", nullable = false)
    private String subdivisionCode;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String subdivisionName;

    @Column(name = "language_code", nullable = false, length = 2)
    private String languageCode;

    @Column(name = "parent_subdivision")
    private String parentSubdivision;

    @Column(name = "category")
    private String category;

    @Column(name = "local_variant")
    private String localVariant;

}
