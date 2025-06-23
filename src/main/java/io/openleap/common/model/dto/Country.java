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

package io.openleap.common.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Country {
    @JsonProperty("name")
    private String name;
    @JsonProperty("alpha2Code")
    private String alpha2Code;
    @JsonProperty("alpha3Code")
    private String alpha3Code;
    @JsonProperty("numericCode")
    private Integer numericCode;
    @JsonProperty("iso3166_2")
    private String iso3166_2;
    @JsonProperty("region")
    private String region;
    @JsonProperty("subRegion")
    private String subRegion;
    @JsonProperty("intermediateRegion")
    private String intermediateRegion;
    @JsonProperty("regionCode")
    private Integer regionCode;
    @JsonProperty("subRegionCode")
    private Integer subRegionCode;
    @JsonProperty("intermediateRegionCode")
    private Integer intermediateRegionCode;
}