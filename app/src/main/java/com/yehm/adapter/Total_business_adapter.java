package com.yehm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yehm.R;
import com.yehm.model.TotalBusinessItem;

import java.util.List;

public class Total_business_adapter extends RecyclerView.Adapter<Total_business_adapter.PaidHolder> {
    private List<TotalBusinessItem> response;
    private Context context;


    public Total_business_adapter(Context context, List<TotalBusinessItem> list) {
        this.response = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Total_business_adapter.PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_business_adapetr, parent, false);
        return new PaidHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Total_business_adapter.PaidHolder holder, int position) {
        holder.loginId.setText(response.get(position).getLoginId());
        holder.name.setText(response.get(position).getDisplayName());
        holder.activeDate.setText(response.get(position).getPermanentDate());
        holder.joiningDate.setText(response.get(position).getJoiningDate());
        holder.leg.setText(response.get(position).getLeg());
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class PaidHolder extends RecyclerView.ViewHolder {

        TextView loginId, name, joiningDate, activeDate, leg;

        public PaidHolder(View itemView) {
            super(itemView);

            loginId = itemView.findViewById(R.id.loginId);
            name = itemView.findViewById(R.id.name);
            joiningDate = itemView.findViewById(R.id.joiningDate);
            activeDate = itemView.findViewById(R.id.activeDate);
            leg = itemView.findViewById(R.id.leg);
        }
    }
}
