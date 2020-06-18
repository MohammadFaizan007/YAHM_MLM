package com.yehm.fragments.support;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.yehm.model.ResponseSupportList;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuportList extends BaseFragment {
    @BindView(R.id.rv_orderlist)
    RecyclerView rvorderlist;
    @BindView(R.id.tv_addrequest)
    TextView tv_addrequest;
    Unbinder unbinder;
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;

    public SuportList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supportlist, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getSupportList();
        } else {
            showAlert(getResources().getString(R.string.alert_internet), R.color.red, R.drawable.error);
        }
    }

    @OnClick({R.id.tv_addrequest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_addrequest:
                goToActivity(SupportAddRequest.class, null);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
        }
    }

    private void getSupportList() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("Fk_MemId", PreferencesManager.getInstance(context).getMEMBERID());
        Call<ResponseSupportList> supportListCall = apiServices.getTicketsSupport(object);
        supportListCall.enqueue(new Callback<ResponseSupportList>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSupportList> call, @NonNull Response<ResponseSupportList> response) {
                hideLoading();
                LoggerUtil.logItem(response.body());
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        SupportListAdapter supportListAdapter = new SupportListAdapter(getActivity(), response.body().getSupportTickets());
                        rvorderlist.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvorderlist.setAdapter(supportListAdapter);
                        rvorderlist.setHasFixedSize(true);
                    } else {
                        Toast.makeText(getActivity(), "No records found.", Toast.LENGTH_SHORT).show();
                        noRecFound.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSupportList> call, @NonNull Throwable t) {
                hideLoading();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

