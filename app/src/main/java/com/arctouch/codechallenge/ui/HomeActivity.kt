package com.arctouch.codechallenge.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.home_activity.*


/**
 * The Home Activity is the main screen of the application. It holds a list of movies.
 *
 * Selecting a movie from the list will open the Movie Details screen.
 */
class HomeActivity : AppCompatActivity() {
    private val logTag = "HomeActivity"

    /**
     * The Home view model.
     */
    private val viewModel by lazy { ViewModelProviders.of(this).get(HomeViewModel::class.java) }

    /**
     * A scroll listener that loads automatically the next page.
     */
    private var scrollListener: NextPageScrollListener? = null

    /**
     * A mutable list that holds all movies loaded on the home adapter.
     */
    private val movieList: MutableList<Movie> = mutableListOf()

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
                viewModel.loadUpcomingMovies(page)
            }
        }

        recyclerView.addOnScrollListener(scrollListener)

        viewModel.apply {

            // Load new movies to the list
            currentMovies.observe(this@HomeActivity, Observer<List<Movie>> {
                it?.let {
                    movieList.addAll(it)
                    progressBar.visibility = View.GONE
                }
            })

            // Notify the home adapter that new items were loaded
            previousMovieCount.observe(this@HomeActivity, Observer<Int> {
                it?.let {
                    val movieCount = viewModel.allLoadedMovies.value?.count() ?: 0

                    homeAdapter.notifyItemRangeInserted(it, movieCount - 1)
                }
            })

            // Start loading upcoming movies
            loadUpcomingMovies()
        }
    }
}
