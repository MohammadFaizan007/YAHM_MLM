package com.yehm.fragments.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yehm.R;
import com.yehm.model.SupportTicketsItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupportListAdapter extends RecyclerView.Adapter<SupportListAdapter.MyViewHolder> {
    private Context context;
    private List<SupportTicketsItem> supportList;

    public SupportListAdapter(Context mContext, List<SupportTicketsItem> supportList) {
        this.context = mContext;
        this.supportList = supportList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.support_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.requestType.setText(supportList.get(position).getSubject());
        holder.requestType.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.requestType.setSelected(true);
        holder.requestType.setSingleLine(true);

        holder.requestdate.setText(supportList.get(position).getRequestDate());
        holder.requestdate.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.requestdate.setSelected(true);
        holder.requestdate.setSingleLine(true);

        holder.requestmsge.setText(supportList.get(position).getMessage());
        holder.requestmsge.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.requestmsge.setSelected(true);
        holder.requestmsge.setSingleLine(true);

        holder.reliedmsge.setText(supportList.get(position).getReplyMessage());
        holder.reliedmsge.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.reliedmsge.setSelected(true);
        holder.reliedmsge.setSingleLine(true);

        holder.status.setText(supportList.get(position).getStatus());

        holder.repliedon.setText(supportList.get(position).getReplyOn());
        holder.repliedon.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.repliedon.setSelected(true);
        holder.repliedon.setSingleLine(true);
    }

    @Override
    public int getItemCount() {
        return supportList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.request_type)
        TextView requestType;
        @BindView(R.id.request_date)
        TextView requestdate;
        @BindView(R.id.request_msge)
        TextView requestmsge;
        @BindView(R.id.relied_msge)
        TextView reliedmsge;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.replied_on)
        TextView repliedon;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

