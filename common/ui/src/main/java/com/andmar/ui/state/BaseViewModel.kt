package com.andmar.ui.state

import androidx.lifecycle.ViewModel
import timber.log.Timber

abstract class BaseViewModel : ViewModel(), RetryHandler {
    override fun retry(tag: String?) {
        Timber.d("retry")
    }

    override fun onCloseErrorClicked(): Boolean {
        Timber.d("onCloseErrorClicked")
        return false
    }

    override fun <T> onDismissErrorDialog(state: State<T>?) {
        Timber.d("onDismissErrorDialog")
    }
}
