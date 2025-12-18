package org.example.dto;

import com.google.gson.annotations.SerializedName;

public record MovieDTO(
   int id,
   String title,
   String overview,
   @SerializedName("release_date") String releaseDate,
   @SerializedName("vote_average") double voteAverage,
   @SerializedName("poster_path") String posterPath
) {}
