package com.shs.trophiesapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.shs.trophiesapp.R;
import com.shs.trophiesapp.data.Suggestion;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.relations.SportWithTrophies;

import java.util.ArrayList;

/**
 * Created by mancj on 27.01.17.
 */

public class CustomSuggestionsAdapter extends SuggestionsAdapter<Suggestion, CustomSuggestionsAdapter.SuggestionHolder> {

    private static final String TAG = "CustomSuggestionsAdapte";

    private SuggestionsAdapter.OnItemViewClickListener listener;

    public void setListener(SuggestionsAdapter.OnItemViewClickListener listener) {
        this.listener = listener;
    }

    public CustomSuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public int getSingleViewHeight() {
        return 30;
    }

    public int getCount() { return suggestions.size(); }

    public Suggestion getItem(int position) {
        return suggestions_clone.get(position);
    }

    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_custom_suggestion, parent, false);
        return new SuggestionHolder(view);
    }

    @Override
    public void onBindSuggestionHolder(Suggestion suggestion, SuggestionHolder holder, int position) {
        Log.d(TAG, "onBindSuggestionHolder: suggestion=" + suggestion.getTitle() + " --- " + suggestion.getSubtitle());
        holder.title.setText(suggestion.getTitle());
        holder.subtitle.setText(suggestion.getSubtitle());
    }

    public interface OnItemViewClickListener {
        void OnItemClickListener(int position, View v);

        void OnItemDeleteListener(int position, View v);
    }

    /**
     * <b>Override to customize functionality</b>
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     * <p>
     * <p>This method is usually implemented by {@link androidx.recyclerview.widget.RecyclerView.Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String term = constraint.toString();
                if(term.isEmpty())
                    suggestions = suggestions_clone;
                else {
                    suggestions = new ArrayList<>();
                    for (Suggestion item: suggestions_clone)
                        if(item.getTitle().toLowerCase().contains(term.toLowerCase()))
                            suggestions.add(item);
                }
                results.values = suggestions;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                suggestions = (ArrayList<Suggestion>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class SuggestionHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected TextView subtitle;
        protected ImageView image;
        private ImageView iv_delete;


        public SuggestionHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            iv_delete = itemView.findViewById(R.id.iv_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setTag(getSuggestions().get(getAdapterPosition()));
                    listener.OnItemClickListener(getAdapterPosition(), v);
                }
            });

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position > 0 && position < getSuggestions().size()) {
                        v.setTag(getSuggestions().get(getAdapterPosition()));
                        listener.OnItemDeleteListener(getAdapterPosition(), v);
                    }
                }
            });
        }


    }

}
