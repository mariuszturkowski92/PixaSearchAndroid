package com.andmar.ui.state

import kotlinx.coroutines.flow.Flow
import java.util.Optional


interface RetryHandler<ED : ErrorData> {


    val errorData: Flow<Optional<ED>>


    fun showError(errorData: ED)

    /**
     * Should be called on retry button click from ErrorDialog
     *
     * @param tag - identifier helping to determine flow which should be relaunched
     */
    fun retry(tag: String?)

    /**
     * Should be called on attempt of closing ErrorDialog
     *
     * @return - if close was successful (if there was data to be shown). Otherwise we'd like to close the screen entirely
     */

    /**
     * Should be called on attempt of closing ErrorDialog
     *
     * @return - if close was successful (if there was data to be shown). Otherwise we'd like to close the screen entirely
     */
    fun onCloseErrorClicked(): Boolean

    /**
     * Called if dialog is dismissed
     */

    /**
     * Called if dialog is dismissed
     */
    fun onDismissErrorDialog()
}