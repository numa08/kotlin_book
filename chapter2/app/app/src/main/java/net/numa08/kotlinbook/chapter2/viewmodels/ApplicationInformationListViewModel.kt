package net.numa08.kotlinbook.chapter2.viewmodels

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v4.util.Pair
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import net.numa08.kotlinbook.chapter2.BR
import net.numa08.kotlinbook.chapter2.adapters.ApplicationInformationListAdapter
import net.numa08.kotlinbook.chapter2.databinding.ViewApplicationInformationListRowBinding
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation
import net.numa08.kotlinbook.chapter2.models.ProcessInformation
import net.numa08.kotlinbook.chapter2.repositories.ApplicationInformationRepository
import net.numa08.kotlinbook.chapter2.repositories.ProcessInformationRepository

typealias ApplicationInformationListViewModelOnClickListener = (ApplicationInformationListViewModel, Int, ViewApplicationInformationListRowBinding) -> Unit
class ApplicationInformationListViewModel(
        private val applicationInformationRepository: ApplicationInformationRepository,
        private val processInformationRepository: ProcessInformationRepository) : BaseObservable() {

    val adapter = ApplicationInformationListAdapter()
    private var onClickListener: ApplicationInformationListViewModelOnClickListener? = null

    @get:Bindable
    var isLoading: Boolean = false
        private set(loading) {
            field = loading
            notifyPropertyChanged(BR.loading)
        }
    var isVisible: Boolean = false
        private set
    var job: Job? = null
        private set
    @get:Bindable
    var applicationInformationList: List<Pair<ApplicationInformation, ProcessInformation>>? = null
        private set(applicationInformationList) {
            val list = requireNotNull(applicationInformationList, { "リストにNullをセットしないでください" })
            field = list
            notifyPropertyChanged(BR.applicationInformationList)
            adapter.informationList.clear()
            adapter.informationList.addAll(list)
        }

    fun setOnClickListener(onClickListener: ApplicationInformationListViewModelOnClickListener?) {
        this.onClickListener = onClickListener
    }

    fun onCreate() {
        isVisible = true
        isLoading = true
        adapter.setOnItemClickListener { index, binding ->
            onClickListener?.invoke(this@ApplicationInformationListViewModel, index, binding)
        }
    }

    fun fetchApplication() {
        job = launch(UI) {
            val applications = applicationInformationRepository.findAllApplicationsAsync().await()
            val list = applications.map {
                val task = processInformationRepository.findProcessInformationByNameAsync(it.packageName)
                it to task
            }.map { (appInfo, task) ->
                val processInfo = task.await()
                Pair.create(appInfo, processInfo)
            }
            isLoading = false
            applicationInformationList = list
        }
    }


//    private fun recursiveFetchProcessInformation(list: MutableList<Pair<ApplicationInformation, ProcessInformation>>, infoList: List<ApplicationInformation>) {
//        if (infoList.isEmpty()) {
//            isLoading = false
//            applicationInformationList = list
//            return
//        }
//        val info = infoList[0]
//        processInformationRepository.findProcessInformationByName(info.packageName) { processInformation ->
//            list.add(Pair.create(info, processInformation))
//            val copy = ArrayList(infoList)
//            copy.removeAt(0)
//            recursiveFetchProcessInformation(list, copy)
//        }
//    }

        fun onDestroy() {
            isVisible = false
            job?.cancel()
        }


    }
