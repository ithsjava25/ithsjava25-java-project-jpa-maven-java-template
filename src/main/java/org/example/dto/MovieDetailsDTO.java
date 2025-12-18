package org.example.dto;

import com.google.gson.annotations.SerializedName;

public record MovieDetailsDTO(
    int id,
    String title,
    String overview,
    @SerializedName("release_date") String releaseDate,
    int runtime,
    @SerializedName("vote_average") double voteAverage,
    @SerializedName("poster_path") String posterPath

    // Should we add a genre DTO?
) {}
