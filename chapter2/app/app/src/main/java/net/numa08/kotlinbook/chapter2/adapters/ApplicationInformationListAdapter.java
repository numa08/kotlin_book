package net.numa08.kotlinbook.chapter2.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.numa08.kotlinbook.chapter2.R;
import net.numa08.kotlinbook.chapter2.databinding.ViewApplicationInformationListRowBinding;
import net.numa08.kotlinbook.chapter2.models.ApplicationInformation;
import net.numa08.kotlinbook.chapter2.viewmodels.ApplicationInformationListRowViewModel;

import java.util.List;

public final class ApplicationInformationListAdapter extends RecyclerView.Adapter<ApplicationInformationListAdapter.ViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(int index, ViewApplicationInformationListRowBinding binding);
    }

    private final ObservableList<ApplicationInformation> informationList = new ObservableArrayList<>();
    @Nullable
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<ApplicationInformation> getInformationList() {
        return informationList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ViewApplicationInformationListRowBinding binding;

        ViewHolder(ViewApplicationInformationListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    private final ObservableList.OnListChangedCallback<ObservableList<ApplicationInformation>> listChangedCallback = new ObservableList.OnListChangedCallback<ObservableList<ApplicationInformation>>() {
        @Override
        public void onChanged(ObservableList<ApplicationInformation> sender) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<ApplicationInformation> sender, int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList<ApplicationInformation> sender, int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<ApplicationInformation> sender, int fromPosition, int toPosition, int itemCount) {}

        @Override
        public void onItemRangeRemoved(ObservableList<ApplicationInformation> sender, int positionStart, int itemCount) {}
    };

    {
        informationList.addOnListChangedCallback(listChangedCallback);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewApplicationInformationListRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_application_information_list_row, parent, false);
        binding.setViewModel(new ApplicationInformationListRowViewModel());
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.binding.getViewModel().setApplicationInformation(informationList.get(position));
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

