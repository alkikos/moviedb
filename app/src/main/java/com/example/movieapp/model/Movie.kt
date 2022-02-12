package com.example.movieapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey @field:SerializedName("id") val id: Long,
    @field:SerializedName("name")val title: String,
    @field:SerializedName("backdrop_path")val imageEndPoint: String?,
    @field:SerializedName("vote_average")val averageVot: Double,
    @field:SerializedName("popularity")val popularity: Double

)