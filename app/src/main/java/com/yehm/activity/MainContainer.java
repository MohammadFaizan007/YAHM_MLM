package com.yehm.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;
import com.yehm.R;
import com.yehm.constants.BaseActivity;
import com.yehm.fragments.dashboard.Dashboard;
import com.yehm.fragments.profile.EditProfile;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainContainer extends BaseActivity implements IPickResult {
    public ActionBarDrawerToggle mDrawerToggle;
    public Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    //    DrawerMenuItems drawerMenuItems;
    //    @BindView(R.id.nav_view)
//    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    //    Fragment currentFragment;
    LinearLayout visibleLayout = null;
    int drawable;
    TextView selectedText;
    boolean once = true;
    Fragment currFragment;
    private AppUpdater updater;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currFragment.onActivityResult(requestCode, resultCode, data);
    }



    private void hideOpenedLayout(LinearLayout ll, TextView tv, int drawable) {
        if (visibleLayout != null && visibleLayout != ll) {
            visibleLayout.setVisibility(View.GONE);
            selectedText.setCompoundDrawablesWithIntrinsicBounds(this.drawable, 0, R.drawable.arrow, 0);
        }
        visibleLayout = ll;
        selectedText = tv;
        this.drawable = drawable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        Fragment dashboard = new Dashboard();
        ReplaceFragement(dashboard, "Dashboard");


        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(false);

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(Html.fromHtml("<small><b>" + "Dashboard" + "</b></small>"));
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        if (once) {
            checkUpdate();
            once = false;
        }
    }

    private void checkUpdate() {
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        Log.d("Latest Version", update.getLatestVersion());
                        Log.d("Latest Version Code", "=" + update.getLatestVersionCode());
                        Log.d("Release notes", update.getReleaseNotes());
                        Log.d("URL", "=" + update.getUrlToDownload());
                        Log.d("Is update available?", Boolean.toString(isUpdateAvailable));
                        if (isUpdateAvailable) {
                            if (updater == null) {
                                updater = new AppUpdater(MainContainer.this).setDisplay(Display.DIALOG);
                                updater.setContentOnUpdateAvailable("Update " + update.getLatestVersion() + " is available to download. Download the latest version to get latest" +
                                        "features, improvements and bug fixes.");
                                updater.setButtonDoNotShowAgain("");
                                updater.setButtonDismiss(" ");
                                updater.setCancelable(false);
                                updater.start();
                            } else {
                                updater.start();
                            }
                        }
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");

                    }
                });
        appUpdaterUtils.start();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.logout) {//Code to run when the create order item is clicked
//            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//            builder.setMessage("Do you want to Logout?").setTitle("LOGOUT")
//                    .setPositiveButton("Yes", (dialog, id) -> {
//                        dialog.cancel();
//                        PreferencesManager.getInstance(context).clear();
//                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    }).setNegativeButton("No", (dialog, id) -> dialog.cancel());
//            android.app.AlertDialog alert = builder.create();
//            alert.show();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        DrawerLayout drawer = findViewById( R.id.drawer_layout );
//        drawer.closeDrawer( GravityCompat.START );
//        return false;
//    }

    public void ReplaceFragement(Fragment setFragment, String title) {
        currFragment = setFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, setFragment, title);
        getSupportActionBar().setTitle(Html.fromHtml("<small><b>" + title + "</b></small>"));
        transaction.commit();
//        drawer_layout.closeDrawer( GravityCompat.START );
    }

    public void ReplaceFragementWithBack(Fragment setFragment, String title) {
        currFragment = setFragment;
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, setFragment, title).addToBackStack(null);
        getSupportActionBar().setTitle(Html.fromHtml("<small><b>" + title + "</b></small>"));
        transaction.commit();
    }

    public void addFragment(Bundle param, Fragment setFragment, String title) {
        currFragment = setFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        setFragment.setArguments(param);
        transaction.replace(R.id.frame, setFragment, title).addToBackStack(null).commit();
    }

    @Override
    public void onPickResult(PickResult r) {
        ((EditProfile) currFragment).onPickResult(r);
    }

//    public class DrawerMenuItems {
//        @BindView(R.id.tv_name)
//        TextView tv_name;
//        @BindView(R.id.dojDate)
//        TextView dojDate;
//        @BindView(R.id.doaDate)
//        TextView doaDate;
//        @BindViews({R.id.team_lo, R.id.wallet_lo, R.id.order_lo, R.id.myincome_lo})
//        List<LinearLayout> sidemenuViews_lo;
//        @BindViews({R.id.dashboard, R.id.registration, R.id.profile, R.id.team, R.id.myWallet, R.id.myIncome, R.id.order, R.id.support, R.id.logout, R.id.product_list, R.id.business_plan})
//        List<TextView> sidemenuViewsImg;
//
//        DrawerMenuItems(View itemView) {
//            ButterKnife.bind(this, itemView);
//        }
//
//        @OnClick({R.id.dashboard, R.id.registration, R.id.profile, R.id.team, R.id.treeView, R.id.directMember, R.id.downline, R.id.myWallet, R.id.eWalletRequest, R.id.payoutLedger,
//                R.id.eWalletLedger, R.id.myIncome, R.id.order, R.id.manageOrder, R.id.support, R.id.logout, R.id.payout_report, R.id.direct_income, R.id.team_performance_bonus, R.id.star_club,
//                R.id.super_star_club, R.id.bronze_club, R.id.silver_club, R.id.gold_club, R.id.platinum_club, R.id.super_platinum_club, R.id.product_list, R.id.business_plan})
//
//        public void onViewClicked(View view) {
//            switch (view.getId()) {
//                case R.id.dashboard:
//                    ReplaceFragement(new Dashboard(), "Dashboard");
//                    break;
//                case R.id.registration:
//                    drawer_layout.closeDrawer(GravityCompat.START);
//                    goToActivity(MainContainer.this, SignUp.class, null);
//                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                    break;
//                case R.id.profile:
//                    ReplaceFragement(new ViewProfile(), "Profile");
//                    break;
//                case R.id.team:
//                    hideOpenedLayout(sidemenuViews_lo.get(0), sidemenuViewsImg.get(3), R.drawable.my_team);
//                    changeUiOnClick(0, 3, R.drawable.my_team);
//                    break;
//                case R.id.treeView:
//                    ReplaceFragement(new TreeView(), "Tree View");
//                    break;
//                case R.id.directMember:
//                    ReplaceFragement(new DirectMember(), "Direct Member");
//                    break;
//                case R.id.downline:
//                    ReplaceFragement(new Downline(), "Downline");
//                    break;
//                case R.id.myWallet:
//                    hideOpenedLayout(sidemenuViews_lo.get(1), sidemenuViewsImg.get(4), R.drawable.wallet);
//                    changeUiOnClick(1, 4, R.drawable.wallet);
//                    break;
//                case R.id.eWalletRequest:
//                    ReplaceFragement(new EWalletRequest(), "E-Wallet Request");
//                    break;
//                case R.id.payoutLedger:
//                    ReplaceFragement(new PayoutLedger(), "Payout Ledger");
//                    break;
//                case R.id.eWalletLedger:
//                    ReplaceFragement(new EWalletLedger(), "E-Wallet Ledger");
//                    break;
//                case R.id.myIncome:
//                    hideOpenedLayout(sidemenuViews_lo.get(3), sidemenuViewsImg.get(5), R.drawable.my_income);
//                    changeUiOnClick(3, 5, R.drawable.my_income);
//                    break;
//                case R.id.payout_report:
//                    ReplaceFragement(new PayoutReport(), "Payout Report");
//                    break;
//                case R.id.direct_income:
//                    PreferencesManager.getInstance(context).setIncomePlanId("1");
//                    ReplaceFragement(new AllIncome(), "Direct Income");
//                    break;
//                case R.id.team_performance_bonus:
//                    PreferencesManager.getInstance(context).setIncomePlanId("2");
//                    ReplaceFragement(new AllIncome(), "Team Performance Bonus");
//                    break;
//                case R.id.star_club:
//                    PreferencesManager.getInstance(context).setIncomePlanId("3");
//                    ReplaceFragement(new AllIncome(), "Star Club");
//                    break;
//                case R.id.super_star_club:
//                    PreferencesManager.getInstance(context).setIncomePlanId("4");
//                    ReplaceFragement(new AllIncome(), "Super Star Club");
//                    break;
//                case R.id.bronze_club:
//                    PreferencesManager.getInstance(context).setIncomePlanId("5");
//                    ReplaceFragement(new AllIncome(), "Bronze Club");
//                    break;
//                case R.id.silver_club:
//                    PreferencesManager.getInstance(context).setIncomePlanId("6");
//                    ReplaceFragement(new AllIncome(), "Silver Club");
//                    break;
//                case R.id.gold_club:
//                    PreferencesManager.getInstance(context).setIncomePlanId("7");
//                    ReplaceFragement(new AllIncome(), "Gold Club");
//                    break;
//                case R.id.platinum_club:
//                    PreferencesManager.getInstance(context).setIncomePlanId("8");
//                    ReplaceFragement(new AllIncome(), "Platinum Club");
//                    break;
//                case R.id.super_platinum_club:
//                    PreferencesManager.getInstance(context).setIncomePlanId("9");
//                    ReplaceFragement(new AllIncome(), "Super Platinum Club");
//                    break;
//                case R.id.manageOrder:
//                    ReplaceFragement(new ManageOrder(), "Manage Order");
//                    break;
//                case R.id.order:
//                    hideOpenedLayout(sidemenuViews_lo.get(2), sidemenuViewsImg.get(6), R.drawable.order);
//                    changeUiOnClick(2, 6, R.drawable.order);
//                    break;
//                case R.id.product_list:
//                    ReplaceFragement(new ProductList(), "Product List");
//                    break;
//                case R.id.business_plan:
//                    ReplaceFragement(new WebviewFragment(), "Business Plan");
//                    break;
//                case R.id.support:
//                    ReplaceFragement(new SuportList(), "Support");
//                    break;
//                case R.id.logout:
//                    drawer_layout.closeDrawer(GravityCompat.START);
//                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//                    builder.setMessage("Do you want to Logout?").setTitle("LOGOUT")
//                            .setPositiveButton("Yes", (dialog, id) -> {
//                                dialog.cancel();
//                                PreferencesManager.getInstance(context).clear();
//                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
//                            }).setNegativeButton("No", (dialog, id) -> dialog.cancel());
//                    android.app.AlertDialog alert = builder.create();
//                    alert.show();
//                    break;
//            }
//        }
//    }




}
