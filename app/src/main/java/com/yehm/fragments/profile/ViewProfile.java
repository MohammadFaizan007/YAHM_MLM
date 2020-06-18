package com.yehm.fragments.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.yehm.BuildConfig;
import com.yehm.R;
import com.yehm.activity.MainContainer;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.constants.MLRoundedImageView;
import com.yehm.constants.UrlConstants;
import com.yehm.model.ResponseViewProfile;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProfile extends BaseFragment {
    public static ResponseViewProfile profile;
    @BindView(R.id.prof_image)
    MLRoundedImageView profImage;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvUserId)
    TextView tvUserId;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.sponsorID)
    TextView sponsorID;
    @BindView(R.id.sponsorName)
    TextView sponsorName;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.activation_date)
    TextView activation_date;
    @BindView(R.id.adharStatus)
    TextView adharStatus;
    @BindView(R.id.bankStatus)
    TextView bankStatus;
    @BindView(R.id.joiningDate)
    TextView joiningDate;
    @BindView(R.id.panStatus)
    TextView panStatus;
    @BindView(R.id.editProfile)
    Button editProfile;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getUserProfile();
        } else {
            createInfoDialog(context, "Alert", getString(R.string.alert_internet));
        }
    }

    private void getUserProfile() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("MemID", PreferencesManager.getInstance(context).getMEMBERID());
        Call<ResponseViewProfile> profileCall = apiServices.getUserProfile(object);
        profileCall.enqueue(new Callback<ResponseViewProfile>() {
            @Override
            public void onResponse(Call<ResponseViewProfile> call, Response<ResponseViewProfile> response) {
                hideLoading();
                LoggerUtil.logItem(response.body());
                try {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        UrlConstants.profile = response.body();
                        setUserProfile(response.body());
                    } else {
                        showToastS(response.body().getResponse());
                    }
                } catch (Error | Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseViewProfile> call, Throwable t) {
                hideLoading();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressLint("SetTextI18n")
    private void setUserProfile(ResponseViewProfile profile) {
        sponsorID.setText(PreferencesManager.getInstance(context).getSponsorid());
        sponsorName.setText(PreferencesManager.getInstance(context).getSponsorname());
        Glide.with(context).load(BuildConfig.BASE_URL_IMAGES +profile.getApiUserProfile().getProfilepic()).into(profImage);
        Glide.with(context).load(BuildConfig.BASE_URL_IMAGES +profile.getApiUserProfile().getProfilepic()).
                apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.demo_person)
                        .error(R.drawable.demo_person))
                .into(profImage);

        tvName.setText(profile.getApiUserProfile().getDisplayName());
        tvUserId.setText(profile.getApiUserProfile().getLoginId());
        status.setText(profile.getApiUserProfile().getStatus());
        joiningDate.setText(profile.getApiUserProfile().getJoiningDate());
        activation_date.setText(PreferencesManager.getInstance(context).getDoa().substring(0, 10));
        if (!profile.getApiUserProfile().getMobile().equalsIgnoreCase("")) {
            mobile.setText(profile.getApiUserProfile().getMobile());
        } else {
            mobile.setText("----------");
        }
        if (!profile.getApiUserProfile().getEmail().equalsIgnoreCase("")) {
            email.setText(profile.getApiUserProfile().getEmail());
        } else {
            email.setText("----------");
        }

        if (profile.getApiUserProfile().getIsApprovedAdhaar().equalsIgnoreCase("False")) {
            adharStatus.setText("Pending");
            adharStatus.setTextColor(getResources().getColor(R.color.inactive));
        } else {
            adharStatus.setText("Approved");
            adharStatus.setTextColor(getResources().getColor(R.color.active));
        }
        if (profile.getApiUserProfile().getIsApprovedCheque().equalsIgnoreCase("False")) {
            panStatus.setText("Pending");
            panStatus.setTextColor(getResources().getColor(R.color.inactive));
        } else {
            panStatus.setText("Approved");
            panStatus.setTextColor(getResources().getColor(R.color.active));
        }
        if (profile.getApiUserProfile().getIsApprovedPanCard().equalsIgnoreCase("False")) {
            bankStatus.setText("Pending");
            bankStatus.setTextColor(getResources().getColor(R.color.inactive));
        } else {
            bankStatus.setText("Approved");
            bankStatus.setTextColor(getResources().getColor(R.color.active));
        }
    }

    @OnClick({R.id.editProfile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.editProfile:
                ((MainContainer) context).addFragment(null, new EditProfile(), "Edit Profile");
                break;
        }
    }
}

