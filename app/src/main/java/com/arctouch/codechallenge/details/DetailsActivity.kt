package com.arctouch.codechallenge.details

import android.os.Bundle
import android.util.Log
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.extensions.backdropImageUrl
import com.arctouch.codechallenge.extensions.posterImageUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_details.*
import kotlinx.android.synthetic.main.details_activity.*

class DetailsActivity : BaseActivity() {
    private val logTag = "DetailsActivity"

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

        api.movie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    it?.let {
                        titleTextView.text = it.title
                        genresTextView.text = it.genres?.joinToString(separator = ", ") { it.name }
                        releaseDateTextView.text = it.releaseDate ?: ""
                        overviewTextView.text = it.overview ?: ""

                        loadBackdropImage(it.backdropImageUrl)

                        loadPosterImage(it.posterImageUrl)
                    }
                }
    }

    private fun loadPosterImage(posterUrl: String?) {
        posterUrl?.let {
            Glide.with(rootScrollView)
                    .load(it)
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(posterImageView)
        }
    }

    private fun loadBackdropImage(backdropUrl: String?) {
        backdropUrl?.let {
            Glide.with(rootScrollView)
                    .load(it)
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(backdropImageView)
        }
    }

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }
}
