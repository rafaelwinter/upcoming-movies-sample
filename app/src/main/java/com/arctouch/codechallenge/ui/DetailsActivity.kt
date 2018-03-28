package com.arctouch.codechallenge.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.viewmodel.DetailsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.content_details.*
import kotlinx.android.synthetic.main.details_activity.*

class DetailsActivity : AppCompatActivity() {
    private val logTag = "DetailsActivity"

    /**
     * The Details view model.
     */
    private val viewModel by lazy { ViewModelProviders.of(this).get(DetailsViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        setSupportActionBar(toolbar)
        title = ""

        val movieId: Long = intent.getLongExtra(EXTRA_MOVIE_ID, 0L)
        if (movieId == 0L) {
            Log.w(logTag, "Missing movie identifier")
            return
        }

        viewModel.apply {
            title.observe(this@DetailsActivity, Observer<String> {
                it?.let {
                    titleTextView.text = it
                }
            })

            genreNames.observe(this@DetailsActivity, Observer<String> {
                it?.let {
                    genresTextView.text = it
                }
            })

            releaseDate.observe(this@DetailsActivity, Observer<String> {
                it?.let {
                    releaseDateTextView.text = it
                }
            })

            overview.observe(this@DetailsActivity, Observer<String> {
                it?.let {
                    overviewTextView.text = it
                }
            })

            backdropImageUrl.observe(this@DetailsActivity, Observer<String> {
                it?.let {
                    Glide.with(rootScrollView)
                            .load(it)
                            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                            .into(posterImageView)
                }
            })

            posterImageUrl.observe(this@DetailsActivity, Observer<String> {
                it?.let {
                    Glide.with(rootScrollView)
                            .load(it)
                            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                            .into(backdropImageView)
                }
            })

            // Load the details
            loadMovie(movieId)
        }
    }

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }
}
