package net.numa08.kotlinbook.chapter2.adapters

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.numa08.kotlinbook.chapter2.R
import net.numa08.kotlinbook.chapter2.databinding.ViewApplicationInformationListRowBinding
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation
import net.numa08.kotlinbook.chapter2.models.ProcessInformation
import net.numa08.kotlinbook.chapter2.viewmodels.ApplicationInformationListRowViewModel

typealias ApplicationInformationListAdapterOnClickListener = (Int, ViewApplicationInformationListRowBinding) -> Unit
class ApplicationInformationListAdapter : RecyclerView.Adapter<ApplicationInformationListAdapter.ViewHolder>() {

    private val _informationList = ObservableArrayList<Pair<ApplicationInformation, ProcessInformation>>()
    val informationList : MutableList<Pair<ApplicationInformation, ProcessInformation>>
                get() = _informationList
    private var onItemClickListener: ApplicationInformationListAdapterOnClickListener? = null

    private val listChangedCallback = object : ObservableList.OnListChangedCallback<ObservableList<Pair<ApplicationInformation, ProcessInformation>>>() {
        override fun onChanged(sender: ObservableList<Pair<ApplicationInformation, ProcessInformation>>) {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(sender: ObservableList<Pair<ApplicationInformation, ProcessInformation>>, positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(sender: ObservableList<Pair<ApplicationInformation, ProcessInformation>>, positionStart: Int, itemCount: Int) {
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableList<Pair<ApplicationInformation, ProcessInformation>>, fromPosition: Int, toPosition: Int, itemCount: Int) {}

        override fun onItemRangeRemoved(sender: ObservableList<Pair<ApplicationInformation, ProcessInformation>>, positionStart: Int, itemCount: Int) {}
    }

    fun setOnItemClickListener(onItemClickListener: ApplicationInformationListAdapterOnClickListener?) {
        this.onItemClickListener = onItemClickListener
    }


    class ViewHolder(val binding: ViewApplicationInformationListRowBinding) : RecyclerView.ViewHolder(binding.root)

    init {
        _informationList.addOnListChangedCallback(listChangedCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ViewApplicationInformationListRowBinding>(LayoutInflater.from(parent.context), R.layout.view_application_information_list_row, parent, false)
        binding.viewModel = ApplicationInformationListRowViewModel(object : ApplicationInformationListRowViewModel.Injector {
            override fun context(): Context = parent.context
            override fun currentTimeMillis(): Long = System.currentTimeMillis()
        })
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.viewModel.setApplicationInformation(informationList[position].first)
        holder.binding.viewModel.setProcessInformation(informationList[position].second)
        holder.binding.viewModel.setOnClickListener {
            onItemClickListener?.invoke(holder.adapterPosition, holder.binding)
        }
    }

    override fun getItemCount() = informationList.size

    @Suppress("unused")
    fun finalize() {
        _informationList.removeOnListChangedCallback(listChangedCallback)
    }
}

