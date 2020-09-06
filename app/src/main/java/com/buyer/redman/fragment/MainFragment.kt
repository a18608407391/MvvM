package com.buyer.redman.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.buyer.redman.BR
import com.buyer.redman.R
import com.buyer.redman.databinding.FragmentMainBinding
import com.buyer.redman.ext.init
import com.buyer.redman.viewmodel.MainFragmentViewModel
import com.zl.library.Base.BaseFragment
import com.zl.library.Config.uiContext
import com.zl.zlibrary.annotation.inject.AnnotationInject
import com.zl.zlibrary.base.BaseViewModel
import com.zl.zlibrary.ext.NOTIFY_VIEW_STATE
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>() {
    override fun initVariableId(): Int {
        return BR.fragment_main_model
    }

    override fun setContent(): Int {
        return R.layout.fragment_main
    }

    override fun initData() {
        super.initData()
        CoroutineScope(uiContext).launch {
            delay(3000)
            mViewModel!!.setPageState(BaseViewModel.NotifyLiveData.PageEnum.SHOW_CONTENT)
//        mainViewpager.apply {
//            this.isUserInputEnabled = false
//            this.offscreenPageLimit=5
//            this.adapter = object: FragmentStateAdapter(this@MainFragment){
//                override fun getItemCount(): Int {
//                    return 5
//                }
//
//                override fun createFragment(position: Int): Fragment {
//                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                }
//            }
//        }

            mainBottom.init {
                when (it) {
                    R.id.menu_main -> mainViewpager.setCurrentItem(0, false)
                    R.id.menu_project -> mainViewpager.setCurrentItem(1, false)
//                R.id.menu_system -> mainViewpager.setCurrentItem(2, false)
//                R.id.menu_public -> mainViewpager.setCurrentItem(3, false)
//                R.id.menu_me -> mainViewpager.setCurrentItem(4, false)
                }
            }
        }
    }
}
