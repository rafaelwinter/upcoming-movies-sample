package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.details.DetailsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.NextPageScrollListener


/**
 * The Home Activity is the main screen of the application. It holds a list of movies.
 *
 * Selecting a movie from the list will open the Movie Details screen.
 */
class HomeActivity : BaseActivity() {
    private val logTag = "HomeActivity"

    /**
     * A mutable list that holds all movies loaded on the Recycler View.
     */
    private val movieList: MutableList<Movie> = mutableListOf()

    /**
     * The total of pages of the currently loaded movie query.
     */
    private var pageCount = 0

    /**
     * A scroll listener that loads automatically the next page.
     */
    private var scrollListener: NextPageScrollListener? = null

    /**
     * The adapter of the home Recycler View
     */
    private val homeAdapter = HomeAdapter(movieList) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, it.id.toLong())
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = homeAdapter

        scrollListener = object : NextPageScrollListener(linearLayoutManager) {
            override fun onFetchNextPage(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (page > pageCount) {
                    return
                }

                loadUpcomingMoviesPage(page)
            }
        }

        recyclerView.addOnScrollListener(scrollListener)

        // Trigger manually the loading method for the first page.
        loadUpcomingMoviesPage(1)
    }

    /**
     * Load a page of upcoming movies and add the result to the movies list.
     */
    private fun loadUpcomingMoviesPage(page: Int) {
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page.toLong(), TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d(logTag, "Finished loading page ${it.page} of ${it.totalPages} (with ${it.totalResults} movies)")

                    pageCount = it.totalPages

                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }

                    val previousCount = movieList.count()
                    val newCount = previousCount + moviesWithGenres.count()

                    movieList.addAll(moviesWithGenres)
                    homeAdapter.notifyItemRangeInserted(previousCount, newCount - 1)

                    progressBar.visibility = View.GONE
                }
    }
}
