package com.marvel.developer.shareutils

import android.content.Context
import android.content.Intent

class Actions {

    companion object {
        private const val PACKAGE = "com.marvel.developer."
    }

    fun openSplashScreenIntent(context: Context): Intent =
        internalIntent(context, PACKAGE.plus("onboarding.open"))

    fun openMainIntent(context: Context): Intent =
        internalIntent(context, PACKAGE.plus("main.open"))

    private fun internalIntent(context: Context, action: String) =
        Intent(action).setPackage(context.packageName)
}