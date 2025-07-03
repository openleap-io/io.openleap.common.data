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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "common_iso_639_language",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "alpha3_b")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class LangEO extends OlEntity {

    @Column(name = "alpha2_code", length = 2, nullable = false)
    private String alpha2Code;

    @Column(name = "alpha3_b", length = 3, nullable = true)
    private String alpha3B;

    @Column(name = "alpha3_t", length = 3, nullable = true)
    private String alpha3T;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "native_name")
    private String nativeName;

}
