package com.buyer.redman

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.zl.zlibrary.base.activity.BaseSingleActivity
import com.zl.zlibrary.ext.loge
import com.zl.zlibrary.manager.NavManager
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.generic.instance


class SplashActivity : BaseSingleActivity() {

    private val navController get() = navHostFragment.findNavController()

    private val navManager: NavManager by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Integer.parseInt("10000000", 2).toString().loge()
        setStatusBarTextColor(true)
        initNavManager()
    }

    private fun initNavManager() {
        navManager.setOnNavEvent {
            navController.navigate(it)
        }
    }
}
