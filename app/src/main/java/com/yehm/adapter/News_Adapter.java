package com.yehm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yehm.R;
import com.yehm.model.WebNewsListItem;

import java.util.List;

public class News_Adapter extends RecyclerView.Adapter<News_Adapter.PaidHolder> {
    private List<WebNewsListItem> response;
    private Context context;


    public News_Adapter(Context context, List<WebNewsListItem> list) {
        this.response = list;
        this.context = context;
    }

    @NonNull
    @Override
    public News_Adapter.PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_adapter, parent, false);
        return new PaidHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull News_Adapter.PaidHolder holder, int position) {
        holder.news_heading.setText(response.get(position).getNewsHeading());
        holder.news_detail.setText(response.get(position).getNews());
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class PaidHolder extends RecyclerView.ViewHolder {

        TextView news_heading, news_detail;

        public PaidHolder(View itemView) {
            super(itemView);

            news_heading = itemView.findViewById(R.id.news_heading);
            news_detail = itemView.findViewById(R.id.news_detail);
        }
    }
}
