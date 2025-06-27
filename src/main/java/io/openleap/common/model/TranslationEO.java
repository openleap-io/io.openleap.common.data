package io.openleap.common.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "common_translations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TranslationEO extends OlEntity{

    @EmbeddedId
    private TranslationKey id;

    @NotBlank
    @Column(name = "value", nullable = false)
    private String value;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class TranslationKey implements java.io.Serializable {

        @Column(name = "entity_type", length = 100, nullable = false)
        private String entityType;

        @Column(name = "entity_id", nullable = false)
        private Long entityId;

        @Column(name = "field_name", length = 100, nullable = false)
        private String fieldName;

        @Column(name = "lang_code", length = 3, nullable = false)
        private String langCode;
    }
}