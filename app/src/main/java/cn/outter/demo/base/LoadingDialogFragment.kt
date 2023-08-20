package cn.outter.demo.base

import androidx.fragment.app.DialogFragment
import cn.outter.demo.R

class LoadingDialogFragment:DialogFragment(R.layout.outter_base_loading_dialog) {

    init {
        isCancelable = false
    }
}