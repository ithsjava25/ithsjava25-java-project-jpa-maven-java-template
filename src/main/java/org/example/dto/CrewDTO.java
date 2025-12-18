package org.example.dto;

import com.google.gson.annotations.SerializedName;

public record CrewDTO(
    int id,
    String name,
    @SerializedName("profile_path") String profilePath,
    String job
) {}
