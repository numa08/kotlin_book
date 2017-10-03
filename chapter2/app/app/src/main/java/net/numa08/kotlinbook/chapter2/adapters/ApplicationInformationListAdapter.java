package net.numa08.kotlinbook.chapter2.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.numa08.kotlinbook.chapter2.R;
import net.numa08.kotlinbook.chapter2.databinding.ViewApplicationInformationListRowBinding;
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;
import net.numa08.kotlinbook.chapter2.models.ProcessInformation;
import net.numa08.kotlinbook.chapter2.viewmodels.ApplicationInformationListRowViewModel;

import java.util.List;

public final class ApplicationInformationListAdapter extends RecyclerView.Adapter<ApplicationInformationListAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(int index, ViewApplicationInformationListRowBinding binding);
    }

    private final ObservableList<Pair<ApplicationInformation, ProcessInformation>> informationList = new ObservableArrayList<>();
    @Nullable
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<Pair<ApplicationInformation, ProcessInformation>> getInformationList() {
        return informationList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ViewApplicationInformationListRowBinding binding;

        ViewHolder(ViewApplicationInformationListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    private final ObservableList.OnListChangedCallback<ObservableList<Pair<ApplicationInformation, ProcessInformation>>> listChangedCallback = new ObservableList.OnListChangedCallback<ObservableList<Pair<ApplicationInformation, ProcessInformation>>>() {
        @Override
        public void onChanged(ObservableList<Pair<ApplicationInformation, ProcessInformation>> sender) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<Pair<ApplicationInformation, ProcessInformation>> sender, int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList<Pair<ApplicationInformation, ProcessInformation>> sender, int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<Pair<ApplicationInformation, ProcessInformation>> sender, int fromPosition, int toPosition, int itemCount) {}

        @Override
        public void onItemRangeRemoved(ObservableList<Pair<ApplicationInformation, ProcessInformation>> sender, int positionStart, int itemCount) {}
    };

    {
        informationList.addOnListChangedCallback(listChangedCallback);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final ViewApplicationInformationListRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_application_information_list_row, parent, false);
        binding.setViewModel(new ApplicationInformationListRowViewModel(new ApplicationInformationListRowViewModel.Injector() {
            @Override
            public Context context() {
                return parent.getContext();
            }
        }));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.getViewModel().setApplicationInformation(informationList.get(position).first);
        holder.binding.getViewModel().setProcessInformation(informationList.get(position).second);
        holder.binding.getViewModel().setOnClickListener(new ApplicationInformationListRowViewModel.OnClickListener() {
            @Override
            public void onClick(ApplicationInformationListRowViewModel viewModel) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(holder.getAdapterPosition(), holder.binding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return informationList.size();
    }

    @Override
    protected void finalize() throws Throwable {
        informationList.removeOnListChangedCallback(listChangedCallback);
        super.finalize();
    }
}

