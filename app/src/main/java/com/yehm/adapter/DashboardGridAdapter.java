package com.yehm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.yehm.R;
import com.yehm.retrofit.MvpView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DashboardGridAdapter extends RecyclerView.Adapter<DashboardGridAdapter.ViewHolder> {

    private final String[] gridViewString;
    private final int[] gridViewImageId;
    private Context mContext;
    private MvpView mvp;

    public DashboardGridAdapter(Context context, String[] gridViewString, int[] gridViewImageId, MvpView mvp) {
        mContext = context;
        this.gridViewImageId = gridViewImageId;
        this.gridViewString = gridViewString;
        this.mvp = mvp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gv_sidemenu_item_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int listPosition) {
        holder.androidGridviewText.setText(gridViewString[listPosition]);
        holder.androidGridviewImage.setImageResource(gridViewImageId[listPosition]);

    }

    @Override
    public int getItemCount() {
        return gridViewString.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.android_gridview_image)
        ImageView androidGridviewImage;
        @BindView(R.id.android_gridview_text)
        TextView androidGridviewText;
        @BindView(R.id.android_custom_gridview_layout)
        ConstraintLayout androidCustomGridviewLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            androidCustomGridviewLayout.setOnClickListener(v -> mvp.getClickPosition(getAdapterPosition(), gridViewString[getAdapterPosition()]));
        }
    }

}