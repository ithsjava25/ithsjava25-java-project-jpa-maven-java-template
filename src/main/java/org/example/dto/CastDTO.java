package org.example.dto;

import com.google.gson.annotations.SerializedName;

public record CastDTO (
    int id,
    String name,
    @SerializedName("profile_path") String profilePath,
    int order // 0 = main actor, 2 = second actor, 3 = third actor etc.
) {}
