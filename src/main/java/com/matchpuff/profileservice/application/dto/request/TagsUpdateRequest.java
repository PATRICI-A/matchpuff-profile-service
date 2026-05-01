package com.matchpuff.profileservice.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * DTO para actualizar los tags/intereses de un usuario.
 * Contiene la lista de tags a asignar.
 */
@Data
public class TagsUpdateRequest {
    @NotEmpty
    @Valid
    private List<TagRequest> tags;
}
