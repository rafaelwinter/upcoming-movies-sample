package com.arctouch.codechallenge.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.arctouch.codechallenge.CodeChallengeApplication
import com.arctouch.codechallenge.model.api.TmdbApi
import com.arctouch.codechallenge.model.data.Cache
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val logTag = "HomeViewModel"

    @Inject
    lateinit var api: TmdbApi

    init {
        (application as CodeChallengeApplication).codeChallengeComponent.inject(this)
    }

    /**
     * The index of the first page to be retrieved
     */
    private val firstPage = 1

    /**
     * The total number of pages of the current query
     */
    private var pageCount = 0

    /**
     * A list that holds the all loaded movies
     */
    val allLoadedMovies: MutableLiveData<List<Movie>> = MutableLiveData()

    /**
     * The list with movies from the current page
     */
    val currentMovies: MutableLiveData<List<Movie>> = MutableLiveData()

    /**
     * The count of movies loaded before adding the current page items
     */
    val previousMovieCount: MutableLiveData<Int> = MutableLiveData()


    /**
     * Load a page of upcoming movies and add the result to the movies list.
     */
    fun loadUpcomingMovies(page: Int = firstPage) {

        // If the genres cache is empty, load it and retry fetching the upcoming movies
        if (Cache.genres.isEmpty()) {
            loadMovieGenres {
                loadUpcomingMovies(page)
            }

            return
        }

        if (page == firstPage) {
            pageCount = 0
        }

        api.upcomingMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d(logTag, "Finished loading page ${it.page} of ${it.totalPages} (with ${it.totalResults} movies)")

                    pageCount = it.totalPages

                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }

                    currentMovies.value = moviesWithGenres

                    val allMovies = allLoadedMovies.value
                    if (allMovies == null) {
                        allLoadedMovies.value = moviesWithGenres
                        previousMovieCount.value = 0
                    } else {
                        allLoadedMovies.value = allMovies + moviesWithGenres
                        previousMovieCount.value = allMovies.count()
                    }
                }
    }

    /**
     * Load the movies genres. This is a pre-requisite to load movies
     */
    private fun loadMovieGenres(onLoaded: (List<Genre>) -> Unit) {
        api.genres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    onLoaded(it.genres)
                }
    }
}