package org.example.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record TopRatedResponseDTO(
    int page,
    List<MovieDTO> results,
    @SerializedName("total_pages") int totalPages,
    @SerializedName("total_results") int totalResults
) {}
