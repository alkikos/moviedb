package com.example.movieapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "popular_movies")
data class PopularMovie(
    @PrimaryKey @field:SerializedName("id") override val movieId: Long,
    @field:SerializedName("name") override val title: String,
    @field:SerializedName("backdrop_path") override val imageEndPoint: String?,
    @field:SerializedName("vote_average") override val averageVot: Float,
    @field:SerializedName("popularity")val popularity: Double,
    @field:SerializedName("overview") override val overview: String,
    @field:SerializedName("first_air_date") override val releaseDate: String?

    ): Movie()


@Entity(tableName = "similar_movies")
data class SimilarMovie(
    @PrimaryKey
    var roomId: Long,
    @field:SerializedName("id") override val movieId: Long,
    @field:SerializedName("backdrop_path") override val imageEndPoint: String?,
    @field:SerializedName("overview") override val overview: String,
    @field:SerializedName("name") override val title: String,
    @field:SerializedName("vote_average") override val averageVot: Float,
    @field:SerializedName("first_air_date") override val releaseDate: String?

    ): Movie()

abstract class Movie(){
    abstract val movieId: Long
    abstract val imageEndPoint: String?
    abstract val overview: String
    abstract val title: String
    abstract val averageVot: Float
    abstract val releaseDate: String?
    override fun equals(other: Any?): Boolean {
        if(other is Movie){
            return movieId == other.movieId &&
                    imageEndPoint == other.imageEndPoint &&
                    overview == other.overview &&
                    title == other.title &&
                    averageVot == other.averageVot
        }
        return false
    }

    override fun hashCode(): Int {
        var result = movieId.hashCode()
        result = 31 * result + (imageEndPoint?.hashCode() ?: 0)
        result = 31 * result + overview.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + averageVot.hashCode()
        result = 31 * result + releaseDate.hashCode()
        return result
    }


}