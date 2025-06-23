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

package io.openleap.common.mapper;

import io.openleap.common.model.PhoneTypeEO;
import io.openleap.common.model.dto.PhoneType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhoneTypeMapper {

    PhoneTypeMapper INSTANCE = Mappers.getMapper(PhoneTypeMapper.class);

    @Mapping(target = "optLock", ignore = true)
    @Mapping(target = "pKey", ignore = true)
    PhoneTypeEO toEntity(PhoneType dto);

    PhoneType toDto(PhoneTypeEO entity);

    List<PhoneType> toDtoList(List<PhoneTypeEO> entities);
    List<PhoneTypeEO> toEntityList(List<PhoneType> dtos);
}