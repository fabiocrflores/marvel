package com.marvel.developer.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marvel.developer.onboarding.databinding.ActivitySplashScreenBinding
import com.marvel.developer.shareutils.Actions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var action: Actions

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setVersion()
        setAnimations()
    }

    private fun setVersion() {
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        binding.textVersion.text = versionName
    }

    private fun setAnimations() {
        binding.apply {
            imageMarvel.animate().setDuration(2500).alpha(1f).withEndAction {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            textVersion.animate().setDuration(2500).alpha(1f).withEndAction {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                goToMain()
            }
        }
    }

    private fun goToMain() {
        startActivity(action.openMainIntent(this))
        finish()
    }
}