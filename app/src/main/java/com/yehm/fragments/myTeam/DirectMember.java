package com.yehm.fragments.myTeam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.model.ResponseDirectMember;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectMember extends BaseFragment {
    @BindView(R.id.rv_mydirect)
    RecyclerView rv_mydirect;
    @BindView(R.id.goto_top)
    Button goto_top;
    @BindView(R.id.goto_root)
    TextView goto_root;
    Unbinder unbinder;
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtId)
    TextView txtId;

    public DirectMember() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydirect, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        txtName.setText(PreferencesManager.getInstance(context).getNAME());
        txtId.setText(PreferencesManager.getInstance(context).getUSERID());
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getMyDirect(PreferencesManager.getInstance(context).getUSERID());
        } else {
            showAlert(getResources().getString(R.string.alert_internet), R.color.red, R.drawable.error);
        }
        goto_top.setOnClickListener(view1 -> rv_mydirect.smoothScrollToPosition(0));

        rv_mydirect.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItem >= 1) {
                    goto_top.setVisibility(View.VISIBLE);
                } else {
                    goto_top.setVisibility(View.GONE);
                }
            }
        });
    }

    public void getMyDirect(String loginid) {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("LoginId", loginid);
        Call<ResponseDirectMember> directMemberCall = apiServices.getDirects(object);
        directMemberCall.enqueue(new Callback<ResponseDirectMember>() {
            @Override
            public void onResponse(Call<ResponseDirectMember> call, Response<ResponseDirectMember> response) {
                hideLoading();
                LoggerUtil.logItem(response.body());
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        MyDirectAdapter myDirectAdapter = new MyDirectAdapter(getActivity(), response.body().getDirectMembers(), DirectMember.this);
                        rv_mydirect.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_mydirect.setAdapter(myDirectAdapter);
                        rv_mydirect.setHasFixedSize(true);
                        noRecFound.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), "No records found.", Toast.LENGTH_SHORT).show();
                        noRecFound.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDirectMember> call, Throwable t) {
                hideLoading();
            }
        });
    }

    @OnClick(R.id.goto_root)
    public void onViewClicked() {
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            txtName.setText(PreferencesManager.getInstance(context).getNAME());
            txtId.setText(PreferencesManager.getInstance(context).getUSERID());
            getMyDirect(PreferencesManager.getInstance(context).getUSERID());
        } else {
            showAlert(getResources().getString(R.string.alert_internet), R.color.red, R.drawable.error);
        }

    }

}
