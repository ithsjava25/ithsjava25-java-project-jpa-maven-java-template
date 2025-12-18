package org.example.dto;

import java.util.List;

public record CreditsDTO(
    int id,
    List<CastDTO> cast,
    List<CrewDTO> crew
) {}
