package net.numa08.kotlinbook.chapter2.viewmodels

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.view.View

import net.numa08.kotlinbook.chapter2.BR
import net.numa08.kotlinbook.chapter2.R
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation
import net.numa08.kotlinbook.chapter2.models.ProcessInformation
import net.numa08.kotlinbook.chapter2.view.TimerTextView

class ApplicationInformationListRowViewModel(private val injector: Injector) : BaseObservable() {
    var applicationInformation: ApplicationInformation? = null
        set(applicationInformation) {
            field = applicationInformation
            notifyPropertyChanged(BR.label)
            notifyPropertyChanged(BR.icon)
        }
    private var processInformation: ProcessInformation? = null

    private var onClickListener: ((ApplicationInformationListRowViewModel) -> Unit)? = null

    val label: CharSequence?
        @Bindable
        get() = applicationInformation?.label

    val icon: Drawable?
        @Bindable
        get() = applicationInformation?.icon

    val description: CharSequence?
        @Bindable
        get() {
            val info = processInformation ?: return null
            return when(info) {
                is ProcessInformation.InActiveProcessInformation -> injector.context().getString(R.string.description_inactive_row_view_nodel)
                is ProcessInformation.ActiveProcessInformation -> injector.context().getString(R.string.description_active_row_view_model)
                is ProcessInformation.ActiveProcessInformationV21 -> {
                    val spanString = DateUtils.getRelativeTimeSpanString((processInformation as ProcessInformation.ActiveProcessInformationV21).lastStartupTime, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
                    injector.context().getString(R.string.description_active_row_v21_view_model, spanString)
                }
            }
        }

    val timerTask: TimerTextView.TimerTask
        @Bindable
        get() = object : TimerTextView.TimerTask {
            override fun onExecute(textView: TimerTextView): CharSequence? = description
        }

    interface Injector {
        fun context(): Context
        fun currentTimeMillis(): Long
    }

    fun setOnClickListener(onClickListener: ((ApplicationInformationListRowViewModel) -> Unit)?) {
        this.onClickListener = onClickListener
    }

    fun onClick(@Suppress("UNUSED_PARAMETER") view: View) {
        onClickListener?.invoke(this)
    }

    fun setProcessInformation(processInformation: ProcessInformation) {
        this.processInformation = processInformation
        notifyPropertyChanged(BR.description)
    }
}
