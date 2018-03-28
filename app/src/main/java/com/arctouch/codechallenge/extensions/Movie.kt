package com.arctouch.codechallenge.extensions

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie

private const val POSTER_URL = "https://image.tmdb.org/t/p/w154"
private const val BACKDROP_URL = "https://image.tmdb.org/t/p/w780"

/**
 * The complete poster image URL
 */
val Movie.posterImageUrl: String
    get() = POSTER_URL + posterPath + "?api_key=" + TmdbApi.API_KEY

/**
 * The complete backdrop image URL
 */
val Movie.backdropImageUrl: String
    get() = BACKDROP_URL + posterPath + "?api_key=" + TmdbApi.API_KEY

/**
 * The names of the movie genres
 */
val Movie.genreNames: String
    get() = genres?.joinToString(separator = ", ") { it.name } ?: ""