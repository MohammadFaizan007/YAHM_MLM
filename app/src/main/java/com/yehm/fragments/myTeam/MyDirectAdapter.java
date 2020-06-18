package com.yehm.fragments.myTeam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yehm.R;
import com.yehm.model.DirectMembersItem;
import com.yehm.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyDirectAdapter extends RecyclerView.Adapter<MyDirectAdapter.MyViewHolder> {
    private Context context;
    private List<DirectMembersItem> productList;
    private DirectMember directMember;

    public MyDirectAdapter(Context mContext, List<DirectMembersItem> productList, DirectMember directMember) {
        this.context = mContext;
        this.productList = productList;
        this.directMember = directMember;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_direct_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (!productList.get(position).getBv().equalsIgnoreCase(""))
            holder.bv.setText(productList.get(position).getBv());
        else
            holder.bv.setText("N/A");

        holder.joiningDate.setText(productList.get(position).getJoiningDate());
        holder.member_name.setText(productList.get(position).getMemberName());
        holder.member_id.setText(productList.get(position).getMemberId());
        holder.package_name.setText(productList.get(position).getPackageName());
        holder.package_amount.setText(productList.get(position).getPackageAmount());
        holder.status.setText(productList.get(position).getStatus());

        String sta = productList.get(position).getStatus();

        if (sta.equalsIgnoreCase("Active")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.active));
        } else if (sta.equalsIgnoreCase("InActive")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.inactive));
        } else if (sta.equalsIgnoreCase("Bolcked")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.blocked));
        }

        holder.action.setOnClickListener(view -> {
            if (NetworkUtils.getConnectivityStatus(context) != 0) {
                directMember.txtName.setText(productList.get(position).getMemberName());
                directMember.txtId.setText(productList.get(position).getMemberId());
                directMember.getMyDirect(productList.get(position).getMemberId());
            } else {
                directMember.showAlert(context.getResources().getString(R.string.alert_internet), R.color.red, R.drawable.error);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bv)
        TextView bv;
        @BindView(R.id.joiningDate)
        TextView joiningDate;
        @BindView(R.id.member_name)
        TextView member_name;
        @BindView(R.id.member_id)
        TextView member_id;
        @BindView(R.id.package_name)
        TextView package_name;
        @BindView(R.id.package_amount)
        TextView package_amount;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.action)
        TextView action;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}

