package com.yehm.fragments.dashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.activity.LoginActivity;
import com.yehm.activity.MainContainer;
import com.yehm.activity.SignUp;
import com.yehm.activity.WebViewActivity;
import com.yehm.adapter.DashboardGridAdapter;
import com.yehm.adapter.News_Adapter;
import com.yehm.adapter.Total_business_adapter;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.fragments.business_plan.WebviewFragment;
import com.yehm.fragments.manageOrder.ManageOrder;
import com.yehm.fragments.myTeam.DirectMember;
import com.yehm.fragments.myTeam.Downline;
import com.yehm.fragments.myTeam.TreeView;
import com.yehm.fragments.my_income.AllIncomeNew;
import com.yehm.fragments.my_income.PayoutReport;
import com.yehm.fragments.my_wallet.EWalletLedger;
import com.yehm.fragments.my_wallet.EWalletRequest;
import com.yehm.fragments.my_wallet.PayoutLedger;
import com.yehm.fragments.product_list.ProductList;
import com.yehm.fragments.profile.ViewProfile;
import com.yehm.fragments.support.SuportList;
import com.yehm.model.BusinessDetailsItem;
import com.yehm.model.MyCommunityItem;
import com.yehm.model.ResponseDashboard;
import com.yehm.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends BaseFragment {
    @BindView(R.id.currentBV)
    TextView currentBV;
    @BindView(R.id.payout)
    TextView payout;
    @BindView(R.id.designation)
    TextView designation;
    @BindView(R.id.righttBV)
    TextView righttBV;
    @BindView(R.id.leftBV)
    TextView leftBV;
    @BindView(R.id.activememberLeft)
    TextView activememberLeft;
    @BindView(R.id.inactiveMemberLeft)
    TextView inactiveMemberLeft;
    @BindView(R.id.totalMemberLeft)
    TextView totalMemberLeft;
    @BindView(R.id.directMemberLeft)
    TextView directMemberLeft;
    @BindView(R.id.totalBusinessLeft)
    TextView totalBusinessLeft;
    @BindView(R.id.carryForwordLeft)
    TextView carryForwordLeft;
    @BindView(R.id.activememberRight)
    TextView activememberRight;
    @BindView(R.id.inactiveMemberRight)
    TextView inactiveMemberRight;
    @BindView(R.id.totalMemberRight)
    TextView totalMemberRight;
    @BindView(R.id.directMemberRight)
    TextView directMemberRight;
    @BindView(R.id.totalBusinessRight)
    TextView totalBusinessRight;
    @BindView(R.id.carryForwordRight)
    TextView carryForwordRight;
    @BindView(R.id.paidBusiness)
    TextView paidBusiness;
    @BindView(R.id.flushBusiness)
    TextView flushBusiness;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.sponsorUserName)
    TextView sponsorUserName;
    @BindView(R.id.sponsorName)
    TextView sponsorName;
    @BindView(R.id.totalBusinessRv)
    RecyclerView totalBusinessRv;
    @BindView(R.id.view_more)
    ImageView view_more;
    @BindView(R.id.currentbusinessleft)
    TextView currentbusinessleft;
    @BindView(R.id.currentbuisinessright)
    TextView currentbuisinessright;
    private int total_business_size, news_size;
    private boolean news_exp = false, total_bus_exp = false;
    Unbinder unbinder;
    @BindView(R.id.news_rv)
    RecyclerView news_rv;
    @BindView(R.id.view_more_news)
    ImageView view_more_news;
    @BindView(R.id.grid_view_sidemenu_item)
    RecyclerView gridViewSidemenuItem;

    private String[] gridViewStringSideMenuItem = {"Registration", "Profile", "Tree View", "Direct Member", "Downline", "E-Wallet Request", "Payout Ledger", "E-Wallet Ledger", "My Income",
            "Payout Report", "Manage Order", "Product List", "Business Plan", "Support Center", "Catalog", "Logout"};
    private int[] gridViewImageIdSideMenuItem = {R.drawable.svg_registration, R.drawable.svg_myprofile, R.drawable.svg_treeview, R.drawable.svg_mydirect, R.drawable.svg_downline,
            R.drawable.svg_wallet, R.drawable.svg_payout_ledger, R.drawable.svg_payout, R.drawable.svg_myincome, R.drawable.svg_payout_report, R.drawable.svg_orderlist,
            R.drawable.svg_products, R.drawable.svg_business_plan, R.drawable.svg_support, R.drawable.ic_catalog, R.drawable.svg_logout};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        DashboardGridAdapter adapterViewAndroidRecharge = new DashboardGridAdapter(getActivity(), gridViewStringSideMenuItem, gridViewImageIdSideMenuItem, this);
        gridViewSidemenuItem.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        gridViewSidemenuItem.setAdapter(adapterViewAndroidRecharge);
        gridViewSidemenuItem.setHasFixedSize(true);

        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getDashBoarddata();
        } else {
            createInfoDialog(context, "Alert", getString(R.string.alert_internet));
        }
    }

    private void getDashBoarddata() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("Fk_MemId", PreferencesManager.getInstance(context).getMEMBERID());
        Call<ResponseDashboard> dashboardCall = apiServices.getDashboard(object);
        dashboardCall.enqueue(new Callback<ResponseDashboard>() {
            @Override
            public void onResponse(Call<ResponseDashboard> call, Response<ResponseDashboard> response) {
                hideLoading();
//                LoggerUtil.logItem(response.body()););
                try {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        currentBV.setText(response.body().getCurrentBV());
                        payout.setText(response.body().getPayout());
                        designation.setText(response.body().getLabel());
                        righttBV.setText(String.format("Right %s", response.body().getRightBV()));
                        leftBV.setText(String.format("Left %s", response.body().getLeftBV()));
                        setDashBoardData(response.body());
                    }
                } catch (Error | Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseDashboard> call, Throwable t) {
                hideLoading();
            }
        });

    }

    public void setDashBoardData(ResponseDashboard responseDashboard) {
        for (int i = 0; i < responseDashboard.getDashboard().size(); i++) {
            switch (responseDashboard.getDashboard().get(i).getSectionTitle()) {
                case "BusinessDetails": {
                    setBusinessDetails(responseDashboard.getDashboard().get(i).getBusinessDetails());
                    break;
                }
                case "MyCommunity": {
                    setMyCommunity(responseDashboard.getDashboard().get(i).getMyCommunity());
                    break;
                }
                case "TotalBusiness": {
                    Total_business_adapter total_business_adapter = new Total_business_adapter(context, responseDashboard.getDashboard().get(i).getTotalBusiness());
                    total_business_size = responseDashboard.getDashboard().get(i).getTotalBusiness().size();
                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
                    totalBusinessRv.setLayoutManager(linearLayoutManager1);
                    totalBusinessRv.setAdapter(total_business_adapter);
                    break;
                }
                case "WebNewsList": {
                    News_Adapter news_adapter = new News_Adapter(context, responseDashboard.getDashboard().get(i).getWebNewsList());
                    news_size = responseDashboard.getDashboard().get(i).getWebNewsList().size();
                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
                    news_rv.setLayoutManager(linearLayoutManager2);
                    news_rv.setAdapter(news_adapter);
                    break;
                }
            }
        }
    }

    public void setBusinessDetails(List<BusinessDetailsItem> data) {
        activememberLeft.setText(data.get(0).getActiveLeft());
        inactiveMemberLeft.setText(data.get(0).getNonActiveLeft());
        totalMemberLeft.setText(data.get(0).getLeftTotal());
        directMemberLeft.setText(data.get(0).getDirectMemberLeft());
        totalBusinessLeft.setText(data.get(0).getTotalBusinessLeft());
        carryForwordLeft.setText(data.get(0).getCarryForwardLeft());
        activememberRight.setText(data.get(0).getActiveRight());
        inactiveMemberRight.setText(data.get(0).getNonActiveRight());
        totalMemberRight.setText(data.get(0).getRightTotal());
        directMemberRight.setText(data.get(0).getDirectMemberRight());
        totalBusinessRight.setText(data.get(0).getTotalBusinessRight());
        carryForwordRight.setText(data.get(0).getCarryForwardRight());
        flushBusiness.setText(data.get(0).getFlushBusiness());
        paidBusiness.setText(data.get(0).getPaidBusiness());
        currentbusinessleft.setText(data.get(0).getCurrLeft());
        currentbuisinessright.setText(data.get(0).getCurrRight());
    }

    public void setMyCommunity(List<MyCommunityItem> data) {
        userName.setText(data.get(0).getUserId());
        name.setText(data.get(0).getName());
        phone.setText(data.get(0).getPhoneNo());
        sponsorUserName.setText(data.get(0).getSponsorUserId());
        sponsorName.setText(data.get(0).getSponsorName());
        PreferencesManager.getInstance(context).setSponsorid(data.get(0).getSponsorUserId());
        PreferencesManager.getInstance(context).setSponsorname(data.get(0).getSponsorName());
    }

    @OnClick({R.id.view_more, R.id.view_more_news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_more:
                try {
                    if (total_bus_exp) {
                        total_bus_exp = false;
                        ViewGroup.LayoutParams params = totalBusinessRv.getLayoutParams();
                        params.height = 180;
                        totalBusinessRv.setLayoutParams(params);
                        view_more.setBackground(getResources().getDrawable(R.drawable.arrow_up_black));
                    } else {
                        total_bus_exp = true;
                        ViewGroup.LayoutParams params = totalBusinessRv.getLayoutParams();
                        params.height = 100 * total_business_size;
                        totalBusinessRv.setLayoutParams(params);
                        view_more.setBackground(getResources().getDrawable(R.drawable.arrow_down_black));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.view_more_news:
                try {
                    if (news_exp) {
                        news_exp = false;
                        ViewGroup.LayoutParams params = news_rv.getLayoutParams();
                        params.height = 180;
                        news_rv.setLayoutParams(params);
                        view_more_news.setBackground(getResources().getDrawable(R.drawable.arrow_up_black));
                    } else {
                        news_exp = true;
                        ViewGroup.LayoutParams params = news_rv.getLayoutParams();
                        params.height = 180 * news_size;
                        news_rv.setLayoutParams(params);
                        view_more_news.setBackground(getResources().getDrawable(R.drawable.arrow_down_black));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    public void getClickPosition(int position, String tag) {
        switch (tag) {
            case "Registration": {
                goToActivity(SignUp.class, null);
                break;
            }
            case "Profile": {
                ((MainContainer) context).ReplaceFragementWithBack(new ViewProfile(), "Profile");
                break;
            }
            case "Tree View": {
                ((MainContainer) context).ReplaceFragementWithBack(new TreeView(), "Tree View");
                break;
            }
            case "Direct Member": {
                ((MainContainer) context).ReplaceFragementWithBack(new DirectMember(), "Direct Member");
                break;
            }
            case "Downline": {
                ((MainContainer) context).ReplaceFragementWithBack(new Downline(), "Downline");
                break;
            }

            case "E-Wallet Request": {
                ((MainContainer) context).ReplaceFragementWithBack(new EWalletRequest(), "E-Wallet Request");
                break;
            }

            case "Payout Ledger": {
                ((MainContainer) context).ReplaceFragementWithBack(new PayoutLedger(), "Payout Ledger");
                break;
            }

            case "E-Wallet Ledger": {
                ((MainContainer) context).ReplaceFragementWithBack(new EWalletLedger(), "E-Wallet Ledger");
                break;
            }

            case "My Income": {
                ((MainContainer) context).ReplaceFragementWithBack(new AllIncomeNew(), "My Income");
                break;
            }

            case "Payout Report": {
                ((MainContainer) context).ReplaceFragementWithBack(new PayoutReport(), "Payout Report");
                break;
            }

            case "Manage Order": {
                ((MainContainer) context).ReplaceFragementWithBack(new ManageOrder(), "Manage Order");
                break;
            }

            case "Product List": {
                ((MainContainer) context).ReplaceFragementWithBack(new ProductList(), "Product List");
                break;
            }

            case "Business Plan": {
                ((MainContainer) context).ReplaceFragementWithBack(new WebviewFragment(), "Business Plan");
                break;
            }
            case "Support Center": {
                ((MainContainer) context).ReplaceFragementWithBack(new SuportList(), "Support");
                break;
            }
            case "Catalog": {
                Bundle bundleN = new Bundle();
                bundleN.putString("link", "http://yehm.co.in/catlog");
                goToActivity(WebViewActivity.class, bundleN);
                break;
            }

            case "Logout": {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to Logout?").setTitle("LOGOUT")
                        .setPositiveButton("Yes", (dialog, id) -> {
                            dialog.cancel();
                            PreferencesManager.getInstance(context).clear();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        }).setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
                break;
            }

        }

    }


    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small><b>" + "Dashboard" + "</b></small>"));
        ((MainContainer) context).mDrawerToggle.setDrawerIndicatorEnabled(false);
        ((MainContainer) context).toolbar.setNavigationIcon(null);
//        ((MainContainer) context).mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

}