package com.arctouch.codechallenge.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.extensions.backdropImageUrl
import com.arctouch.codechallenge.extensions.genreNames
import com.arctouch.codechallenge.extensions.posterImageUrl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    // TODO: I have to admit, this is far from ideal! Will be fixed soon, I think!
    private val api: TmdbApi = Retrofit.Builder()
            .baseUrl(TmdbApi.URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbApi::class.java)

    /**
     * The title of the movie
     */
    val title: MutableLiveData<String> = MutableLiveData()

    /**
     * A comma-separated string with the movie genres
     */
    val genreNames: MutableLiveData<String> = MutableLiveData()

    /**
     * The date of release
     */
    val releaseDate: MutableLiveData<String> = MutableLiveData()

    /**
     * The overview of the movie
     */
    val overview: MutableLiveData<String> = MutableLiveData()

    /**
     * The backdrop image URL
     */
    val backdropImageUrl: MutableLiveData<String> = MutableLiveData()

    /**
     * The poster image URL
     */
    val posterImageUrl: MutableLiveData<String> = MutableLiveData()

    /**
     * Load details from the specified movie
     */
    fun loadMovie(movieId: Long) {
        api.movie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    it?.let {
                        backdropImageUrl.value = it.backdropImageUrl
                        posterImageUrl.value = it.posterImageUrl
                        title.value = it.title
                        genreNames.value = it.genreNames
                        releaseDate.value = it.releaseDate ?: ""
                        overview.value = it.overview ?: ""
                    }
                }
    }
}
