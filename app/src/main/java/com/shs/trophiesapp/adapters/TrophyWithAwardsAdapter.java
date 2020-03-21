package com.shs.trophiesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shs.trophiesapp.PersonalPlayerAwardsActivity;
import com.shs.trophiesapp.R;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.utils.ColorGeneratorByYear;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrophyWithAwardsAdapter extends RecyclerView.Adapter<TrophyWithAwardsAdapter.TrophyWithAwardsViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private static final String TAG = "TrophyWithAwardsAdapter";
    private Context context;
    private List<TrophyAward> awards;
    private int trophyColor;


    public TrophyWithAwardsAdapter(Context context, List<TrophyAward> awards, int trophyColor) {
        this.context = context;
        this.awards = awards;
        this.trophyColor = trophyColor;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: awardsFiltered.size()=" + awards.size());
        return awards == null ? 0 : awards.size();
    }

    @Override
    @NonNull
    public TrophyWithAwardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new NormalTrophyWithAwardsViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_item_trophy_player_and_year, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressTrophyWithAwardsViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_item_trophy_player_and_year_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(TrophyWithAwardsViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(isLoaderVisible) return position == awards.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        else return VIEW_TYPE_NORMAL;
    }

    public void addLoading(TrophyAward ta) {
        isLoaderVisible = true;
        awards.add(ta);
        notifyItemInserted(awards.size() - 1);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = awards.size() - 1;
        TrophyAward item = getItem(position);
        if (item != null) {
            awards.remove(position);
            notifyDataSetChanged();
        }
    }
    public void clear() {
        awards.clear();
        notifyDataSetChanged();
    }

    TrophyAward getItem(int position) {
        return awards.get(position);
    }


    abstract static class TrophyWithAwardsViewHolder extends RecyclerView.ViewHolder {
        private int mCurrentPosition;

        private TextView txtPlayer;
        private TextView txtYear;
        CardView cardView;

        TrophyWithAwardsViewHolder(View itemView) {
            super(itemView);
            txtPlayer = itemView.findViewById(R.id.txtPlayer);
            txtYear = itemView.findViewById(R.id.txtYear);
            cardView = itemView.findViewById(R.id.cardview_trophy_player_and_year_id);
        }

        public void onBind(int position) {
            mCurrentPosition = position;
            clear();
        }
        public int getCurrentPosition() {
            return mCurrentPosition;
        }

        protected abstract void clear();
    }


    class NormalTrophyWithAwardsViewHolder extends TrophyWithAwardsViewHolder {

        @BindView(R.id.txtPlayer) TextView txtPlayer;
        @BindView(R.id.txtYear) TextView txtYear;
        @BindView(R.id.cardview_trophy_player_and_year_id) CardView cardView;

        NormalTrophyWithAwardsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {}

        @Override
        public void onBind(int position) {
            super.onBind(position);
            TrophyAward ta = awards.get(position);
            txtPlayer.setText(ta.getPlayer());
            txtYear.setText(String.valueOf(ta.getYear()));

            int color = ColorGeneratorByYear.getInstance().getColorForYear(ta.getYear());
            ta.setColor(color);
            cardView.setBackgroundColor(ta.getColor());

            View.OnClickListener listener = view -> {
                Intent intent = new Intent(context, PersonalPlayerAwardsActivity.class);
                intent.putExtra("playerName", ta.getPlayer());
                intent.putExtra("color", ta.getColor());
                intent.putExtra("trophyId", ta.getTrophyId());
                context.startActivity(intent);
            };

            cardView.setOnClickListener(listener);
        }
    }

    static class ProgressTrophyWithAwardsViewHolder extends TrophyWithAwardsViewHolder {

        ProgressTrophyWithAwardsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {

        }
    }


    public abstract static class TrophyPaginationListener extends RecyclerView.OnScrollListener {
        public static final int PAGE_START = 1;
        public static final int PAGE_SIZE = 29;

        private GridLayoutManager layoutManager;

        public TrophyPaginationListener(@NonNull GridLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            Log.d(TAG, "visibleItemCount: " + visibleItemCount + ", totalItemCount: " + totalItemCount + ", firstVisibleItemPosition: " + firstVisibleItemPosition);
            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    loadMoreItems();
                }
            }

        }

        protected abstract void loadMoreItems();
        public abstract boolean isLastPage();
        public abstract boolean isLoading();
    }
}
