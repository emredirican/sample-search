package com.kallavistudios.samplesearch.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kallavistudios.samplesearch.R;
import com.kallavistudios.samplesearch.rest.response.model.SearchResultItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<SearchResultItem> searchResults;

    public SearchResultAdapter(List<SearchResultItem> searchResults) {
        this.searchResults = searchResults;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchResultItem item = searchResults.get(position);
        String title = item.getTitle();
        String body = searchResults.get(position).getSnippet();
        holder.bodyTextView.setText(body);
        holder.titleTextView.setText(title);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void setItems(List<SearchResultItem> resultItems){
        this.searchResults = resultItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_snippet)
        TextView bodyTextView;
        @Bind(R.id.tv_title)
        TextView titleTextView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
