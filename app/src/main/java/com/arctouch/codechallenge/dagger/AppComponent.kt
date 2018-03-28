package com.arctouch.codechallenge.dagger

import com.arctouch.codechallenge.viewmodel.DetailsViewModel
import com.arctouch.codechallenge.viewmodel.HomeViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(target: HomeViewModel)
    fun inject(target: DetailsViewModel)
}
