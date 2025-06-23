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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class OlPersistenceEntity implements Serializable {

    @Id
    @Column(
            name = "pk"
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "generator"
    )
    private Long pKey;

    @Column(name = "uuid")
    private UUID uuid;


    @Version
    @Column(
            name = "opt_lock"
    )
    private long optLock;

    @Column(
            name = "created"
    )
    @CreatedDate
    private LocalDateTime created;

    @Column(
            name = "created_by"
    )
    @CreatedBy
    private String createdBy;

    @Column(
            name = "updated"
    )
    @LastModifiedDate
    private LocalDateTime updated;

    @Column(
            name = "updated_by"
    )
    @LastModifiedBy
    private String updatedBy;

}
