package com.arctouch.codechallenge

import android.app.Application
import com.arctouch.codechallenge.dagger.AppComponent
import com.arctouch.codechallenge.dagger.AppModule
import com.arctouch.codechallenge.dagger.DaggerAppComponent

class CodeChallengeApplication: Application() {
    lateinit var codeChallengeComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        codeChallengeComponent = initDagger(this)
    }

    private fun initDagger(app: CodeChallengeApplication): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
}
