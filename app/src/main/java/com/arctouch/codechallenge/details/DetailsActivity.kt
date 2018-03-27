package com.arctouch.codechallenge.details

import android.os.Bundle
import android.util.Log
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_details.*
import kotlinx.android.synthetic.main.details_activity.*

class DetailsActivity : BaseActivity() {
    private val logTag = "DetailsActivity"
    private val movieImageUrlBuilder = MovieImageUrlBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        setSupportActionBar(toolbar)
        title = ""
    }

    override fun onResume() {
        super.onResume()

        val movieId: Long = intent.getLongExtra(EXTRA_MOVIE_ID, 0L)
        if (movieId == 0L) {
            Log.w(logTag, "Missing movie identifier")
            return
        }

        api.movie(movieId, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    it?.let {
                        titleTextView.text = it.title
                        genresTextView.text = it.genres?.joinToString(separator = ", ") { it.name }
                        releaseDateTextView.text = it.releaseDate ?: ""
                        overviewTextView.text = it.overview ?: ""

                        loadBackdropImage(it.backdropPath)

                        loadPosterImage(it.posterPath)
                    }
                }
    }

    private fun loadPosterImage(posterPath: String?) {
        Glide.with(rootScrollView)
                .load(posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(posterImageView)
    }

    private fun loadBackdropImage(backdropPath: String?) {
        Glide.with(rootScrollView)
                .load(backdropPath?.let { movieImageUrlBuilder.buildBackdropUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(backdropImageView)
    }

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }
}
