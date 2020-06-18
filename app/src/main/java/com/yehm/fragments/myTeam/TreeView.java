package com.yehm.fragments.myTeam;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.activity.SignUp;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.constants.UrlConstants;
import com.yehm.model.CheckDownlineResponse;
import com.yehm.model.GenealogytreelistItem;
import com.yehm.model.ResponseTreeView;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreeView extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<GenealogytreelistItem> tree_view_arr;
    ImageView tree_l_one_photo, tree_l_two_a_photo, tree_l_two_b_photo, tree_l_three_a_photo, tree_l_three_b_photo, tree_l_three_c_photo, tree_l_three_d_photo,
            tree_l_four_one_photo, tree_l_four_two_photo, tree_l_four_three_photo, tree_l_four_four_photo, tree_l_four_five_photo, tree_l_four_six_photo,
            tree_l_four_seven_photo, tree_l_four_eight_photo;
    TextView moveup, go_btn, movetoroot, title, tree_l_one_customerid, tree_l_one_customer_name, tree_l_one_customer_spillby,
            tree_l_two_a_customerid, tree_l_two_a_customer_name, tree_l_two_a_customer_spillby,
            tree_l_two_b_customerid, tree_l_two_b_customer_name, tree_l_two_b_customer_spillby,
            tree_l_three_a_customerid, tree_l_three_a_customer_name, tree_l_three_a_customer_spillby,
            tree_l_three_b_customerid, tree_l_three_b_customer_name, tree_l_three_b_customer_spillby,
            tree_l_three_c_customerid, tree_l_three_c_customer_name, tree_l_three_c_customer_spillby,
            tree_l_three_d_customerid, tree_l_three_d_customer_name, tree_l_three_d_customer_spillby,
            tree_l_four_one_customerid, tree_l_four_two_customerid, tree_l_four_three_customerid, tree_l_four_four_customerid,
            tree_l_four_five_customerid, tree_l_four_six_customerid, tree_l_four_seven_customerid, tree_l_four_eight_customerid,
            tree_l_four_one_customer_name, tree_l_four_two_customer_name, tree_l_four_three_customer_name, tree_l_four_four_customer_name,
            tree_l_four_five_customer_name, tree_l_four_six_customer_name, tree_l_four_seven_customer_name, tree_l_four_eight_customer_name,
            tree_l_four_one_customer_spillby, tree_l_four_two_customer_spillby, tree_l_four_three_customer_spillby, tree_l_four_four_customer_spillby,
            tree_l_four_five_customer_spillby, tree_l_four_six_customer_spillby, tree_l_four_seven_customer_spillby, tree_l_four_eight_customer_spillby,
            tree_l_one_reg, tree_l_two_a_reg, tree_l_two_b_reg, tree_l_three_a_reg, tree_l_three_b_reg, tree_l_three_c_reg, tree_l_three_d_reg, tree_l_four_one_reg, tree_l_four_two_reg, tree_l_four_three_reg, tree_l_four_four_reg, tree_l_four_five_reg,
            tree_l_four_six_reg, tree_l_four_seven_reg, tree_l_four_eight_reg;
    String twoL = "", twoR = "", three1 = "", three2 = "", three3 = "", three4 = "";
    EditText search_et;
    Dialog dialog, BusinessDialog;
    int start_once = 0;
    View view;
    private LinearLayout main_lo, node_one, tree_l_one_lo, tree_l_two_a_lo, tree_l_two_b_lo, tree_l_three_a_lo, tree_l_three_b_lo,
            tree_l_three_c_lo, tree_l_three_d_lo, tree_l_four_one_lo,
            tree_l_four_two_lo, tree_l_four_three_lo, tree_l_four_four_lo, tree_l_four_five_lo, tree_l_four_six_lo, tree_l_four_seven_lo,
            tree_l_four_eight_lo,
            node_two, node_three, node_four, node_five, node_six, node_seven, node_eight, node_nine, node_ten, node_eleven, node_twelve, node_thirteen, node_fourteen, node_fifteen;
    private String loginId = "", top_node_parentID, login_name = "";
    private String mParam1;
    private String mParam2;

    public TreeView() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tree_view, container, false);
        movetoroot = view.findViewById(R.id.movetoroot);
        go_btn = view.findViewById(R.id.go_btn);
        moveup = view.findViewById(R.id.moveup);
        try {
            loginId = PreferencesManager.getInstance(context).getUSERID();
            findid(view);
            if (start_once == 0) {
                if (UrlConstants.treeview_registered.equalsIgnoreCase("1")) {
                    UrlConstants.treeview_registered = "0";
                    getTreeViewDetail(UrlConstants.treeview_top_loginID, getActivity());
                } else {
                    getTreeViewDetail(loginId, getActivity());
                }
            }
        } catch (Error e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (start_once == 1) {
            getTreeViewDetail(UrlConstants.treeview_top_loginID, getActivity());
        }
    }

    public void findid(View view) {
        showToastS("Long press to view business.");
        tree_l_one_photo = view.findViewById(R.id.tree_l_one_photo);
        tree_l_two_a_photo = view.findViewById(R.id.tree_l_two_a_photo);
        tree_l_two_b_photo = view.findViewById(R.id.tree_l_two_b_photo);
        tree_l_three_a_photo = view.findViewById(R.id.tree_l_three_a_photo);
        tree_l_three_b_photo = view.findViewById(R.id.tree_l_three_b_photo);
        tree_l_three_c_photo = view.findViewById(R.id.tree_l_three_c_photo);
        tree_l_three_d_photo = view.findViewById(R.id.tree_l_three_d_photo);

        tree_l_four_one_photo = view.findViewById(R.id.tree_l_four_one_photo);
        tree_l_four_two_photo = view.findViewById(R.id.tree_l_four_two_photo);
        tree_l_four_three_photo = view.findViewById(R.id.tree_l_four_three_photo);
        tree_l_four_four_photo = view.findViewById(R.id.tree_l_four_four_photo);
        tree_l_four_five_photo = view.findViewById(R.id.tree_l_four_five_photo);
        tree_l_four_six_photo = view.findViewById(R.id.tree_l_four_six_photo);
        tree_l_four_seven_photo = view.findViewById(R.id.tree_l_four_seven_photo);
        tree_l_four_eight_photo = view.findViewById(R.id.tree_l_four_eight_photo);

        tree_l_four_one_customerid = view.findViewById(R.id.tree_l_four_one_customerid);
        tree_l_four_two_customerid = view.findViewById(R.id.tree_l_four_two_customerid);
        tree_l_four_three_customerid = view.findViewById(R.id.tree_l_four_three_customerid);
        tree_l_four_four_customerid = view.findViewById(R.id.tree_l_four_four_customerid);
        tree_l_four_five_customerid = view.findViewById(R.id.tree_l_four_five_customerid);
        tree_l_four_six_customerid = view.findViewById(R.id.tree_l_four_six_customerid);
        tree_l_four_seven_customerid = view.findViewById(R.id.tree_l_four_seven_customerid);
        tree_l_four_eight_customerid = view.findViewById(R.id.tree_l_four_eight_customerid);

        tree_l_four_one_customer_name = view.findViewById(R.id.tree_l_four_one_customer_name);
        tree_l_four_two_customer_name = view.findViewById(R.id.tree_l_four_two_customer_name);
        tree_l_four_three_customer_name = view.findViewById(R.id.tree_l_four_three_customer_name);
        tree_l_four_four_customer_name = view.findViewById(R.id.tree_l_four_four_customer_name);
        tree_l_four_five_customer_name = view.findViewById(R.id.tree_l_four_five_customer_name);
        tree_l_four_six_customer_name = view.findViewById(R.id.tree_l_four_six_customer_name);
        tree_l_four_seven_customer_name = view.findViewById(R.id.tree_l_four_seven_customer_name);
        tree_l_four_eight_customer_name = view.findViewById(R.id.tree_l_four_eight_customer_name);

        tree_l_four_one_customer_spillby = view.findViewById(R.id.tree_l_four_one_customer_spillby);
        tree_l_four_two_customer_spillby = view.findViewById(R.id.tree_l_four_two_customer_spillby);
        tree_l_four_three_customer_spillby = view.findViewById(R.id.tree_l_four_three_customer_spillby);
        tree_l_four_four_customer_spillby = view.findViewById(R.id.tree_l_four_four_customer_spillby);
        tree_l_four_five_customer_spillby = view.findViewById(R.id.tree_l_four_five_customer_spillby);
        tree_l_four_six_customer_spillby = view.findViewById(R.id.tree_l_four_six_customer_spillby);
        tree_l_four_seven_customer_spillby = view.findViewById(R.id.tree_l_four_seven_customer_spillby);
        tree_l_four_eight_customer_spillby = view.findViewById(R.id.tree_l_four_eight_customer_spillby);

        search_et = view.findViewById(R.id.search_et);
        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    checkDownlineForTree(search_et.getText().toString().trim(), "null", getActivity());
                    return true;
                }
                return false;
            }
        });

        tree_l_one_customerid = view.findViewById(R.id.tree_l_one_customerid);
        tree_l_one_customer_name = view.findViewById(R.id.tree_l_one_customer_name);
        tree_l_one_customer_spillby = view.findViewById(R.id.tree_l_one_customer_spillby);
        tree_l_two_a_customerid = view.findViewById(R.id.tree_l_two_a_customerid);
        tree_l_two_a_customer_name = view.findViewById(R.id.tree_l_two_a_customer_name);
        tree_l_two_a_customer_spillby = view.findViewById(R.id.tree_l_two_a_customer_spillby);
        tree_l_two_b_customerid = view.findViewById(R.id.tree_l_two_b_customerid);
        tree_l_two_b_customer_name = view.findViewById(R.id.tree_l_two_b_customer_name);
        tree_l_two_b_customer_spillby = view.findViewById(R.id.tree_l_two_b_customer_spillby);
        tree_l_three_a_customerid = view.findViewById(R.id.tree_l_three_a_customerid);
        tree_l_three_a_customer_name = view.findViewById(R.id.tree_l_three_a_customer_name);
        tree_l_three_a_customer_spillby = view.findViewById(R.id.tree_l_three_a_customer_spillby);
        tree_l_three_b_customerid = view.findViewById(R.id.tree_l_three_b_customerid);
        tree_l_three_b_customer_name = view.findViewById(R.id.tree_l_three_b_customer_name);
        tree_l_three_b_customer_spillby = view.findViewById(R.id.tree_l_three_b_customer_spillby);
        tree_l_three_c_customerid = view.findViewById(R.id.tree_l_three_c_customerid);
        tree_l_three_c_customer_name = view.findViewById(R.id.tree_l_three_c_customer_name);
        tree_l_three_c_customer_spillby = view.findViewById(R.id.tree_l_three_c_customer_spillby);
        tree_l_three_d_customerid = view.findViewById(R.id.tree_l_three_d_customerid);
        tree_l_three_d_customer_name = view.findViewById(R.id.tree_l_three_d_customer_name);
        tree_l_three_d_customer_spillby = view.findViewById(R.id.tree_l_three_d_customer_spillby);

        tree_l_one_reg = view.findViewById(R.id.tree_l_one_reg);
        tree_l_two_a_reg = view.findViewById(R.id.tree_l_two_a_reg);
        tree_l_two_b_reg = view.findViewById(R.id.tree_l_two_b_reg);
        tree_l_three_a_reg = view.findViewById(R.id.tree_l_three_a_reg);
        tree_l_three_b_reg = view.findViewById(R.id.tree_l_three_b_reg);
        tree_l_three_c_reg = view.findViewById(R.id.tree_l_three_c_reg);
        tree_l_three_d_reg = view.findViewById(R.id.tree_l_three_d_reg);
        tree_l_four_one_reg = view.findViewById(R.id.tree_l_four_one_reg);
        tree_l_four_two_reg = view.findViewById(R.id.tree_l_four_two_reg);
        tree_l_four_three_reg = view.findViewById(R.id.tree_l_four_three_reg);
        tree_l_four_four_reg = view.findViewById(R.id.tree_l_four_four_reg);
        tree_l_four_five_reg = view.findViewById(R.id.tree_l_four_five_reg);
        tree_l_four_six_reg = view.findViewById(R.id.tree_l_four_six_reg);
        tree_l_four_seven_reg = view.findViewById(R.id.tree_l_four_seven_reg);
        tree_l_four_eight_reg = view.findViewById(R.id.tree_l_four_eight_reg);

        main_lo = view.findViewById(R.id.main_lo);
        node_one = view.findViewById(R.id.node_one);
        node_two = view.findViewById(R.id.node_two);
        node_three = view.findViewById(R.id.node_three);
        node_four = view.findViewById(R.id.node_four);
        node_five = view.findViewById(R.id.node_five);
        node_six = view.findViewById(R.id.node_six);
        node_seven = view.findViewById(R.id.node_seven);

        node_eight = view.findViewById(R.id.node_eight);
        node_nine = view.findViewById(R.id.node_nine);
        node_ten = view.findViewById(R.id.node_ten);
        node_eleven = view.findViewById(R.id.node_eleven);
        node_twelve = view.findViewById(R.id.node_twelve);
        node_thirteen = view.findViewById(R.id.node_thirteen);
        node_fourteen = view.findViewById(R.id.node_fourteen);
        node_fifteen = view.findViewById(R.id.node_fifteen);

        tree_l_one_lo = view.findViewById(R.id.tree_l_one_lo);
        tree_l_two_a_lo = view.findViewById(R.id.tree_l_two_a_lo);
        tree_l_two_b_lo = view.findViewById(R.id.tree_l_two_b_lo);
        tree_l_three_a_lo = view.findViewById(R.id.tree_l_three_a_lo);
        tree_l_three_b_lo = view.findViewById(R.id.tree_l_three_b_lo);
        tree_l_three_c_lo = view.findViewById(R.id.tree_l_three_c_lo);
        tree_l_three_d_lo = view.findViewById(R.id.tree_l_three_d_lo);
        tree_l_four_one_lo = view.findViewById(R.id.tree_l_four_one_lo);
        tree_l_four_two_lo = view.findViewById(R.id.tree_l_four_two_lo);
        tree_l_four_three_lo = view.findViewById(R.id.tree_l_four_three_lo);
        tree_l_four_four_lo = view.findViewById(R.id.tree_l_four_four_lo);
        tree_l_four_five_lo = view.findViewById(R.id.tree_l_four_five_lo);
        tree_l_four_six_lo = view.findViewById(R.id.tree_l_four_six_lo);
        tree_l_four_seven_lo = view.findViewById(R.id.tree_l_four_seven_lo);
        tree_l_four_eight_lo = view.findViewById(R.id.tree_l_four_eight_lo);

        moveup.setOnClickListener(this);
        go_btn.setOnClickListener(this);
        movetoroot.setOnClickListener(this);
        node_one.setOnClickListener(this);
        node_two.setOnClickListener(this);
        node_three.setOnClickListener(this);
        node_four.setOnClickListener(this);
        node_five.setOnClickListener(this);
        node_six.setOnClickListener(this);
        node_seven.setOnClickListener(this);
        node_eight.setOnClickListener(this);
        node_nine.setOnClickListener(this);
        node_ten.setOnClickListener(this);
        node_eleven.setOnClickListener(this);
        node_twelve.setOnClickListener(this);
        node_thirteen.setOnClickListener(this);
        node_fourteen.setOnClickListener(this);
        node_fifteen.setOnClickListener(this);

        node_one.setOnLongClickListener(v -> {
            if (!tree_l_one_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_one_customerid.getText().toString(), context);
            }
            return true;
        });
        node_two.setOnLongClickListener(v -> {
            if (!tree_l_two_a_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_two_a_customerid.getText().toString(), context);
            }
            return true;
        });
        node_three.setOnLongClickListener(v -> {
            if (!tree_l_two_b_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_two_b_customerid.getText().toString(), context);
            }
            return true;
        });
        node_four.setOnLongClickListener(v -> {
            if (!tree_l_three_a_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_three_a_customerid.getText().toString(), context);
            }
            return true;
        });
        node_five.setOnLongClickListener(v -> {
            if (!tree_l_three_b_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_three_b_customerid.getText().toString(), context);
            }
            return true;
        });
        node_six.setOnLongClickListener(v -> {
            if (!tree_l_three_c_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_three_c_customerid.getText().toString(), context);
            }
            return true;
        });
        node_seven.setOnLongClickListener(v -> {
            if (!tree_l_three_d_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_three_d_customerid.getText().toString(), context);
            }
            return true;
        });
        node_eight.setOnLongClickListener(v -> {
            if (!tree_l_four_one_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_four_one_customerid.getText().toString(), context);
            }
            return true;
        });
        node_nine.setOnLongClickListener(v -> {
            if (!tree_l_four_two_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_four_two_customerid.getText().toString(), context);
            }
            return true;
        });
        node_ten.setOnLongClickListener(v -> {
            if (!tree_l_four_three_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_four_three_customerid.getText().toString(), context);
            }
            return true;
        });
        node_eleven.setOnLongClickListener(v -> {
            if (!tree_l_four_four_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_four_four_customerid.getText().toString(), context);
            }
            return true;
        });
        node_twelve.setOnLongClickListener(v -> {
            if (!tree_l_four_five_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_four_five_customerid.getText().toString(), context);
            }
            return true;
        });
        node_thirteen.setOnLongClickListener(v -> {
            if (!tree_l_four_six_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_four_six_customerid.getText().toString(), context);
            }
            return true;
        });
        node_fourteen.setOnLongClickListener(v -> {
            if (!tree_l_four_seven_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_four_seven_customerid.getText().toString(), context);
            }
            return true;
        });
        node_fifteen.setOnLongClickListener(v -> {
            if (!tree_l_four_eight_customerid.getText().toString().equalsIgnoreCase("")) {
                BusinessDialog(tree_l_four_eight_customerid.getText().toString(), context);
            }
            return true;
        });
    }

    private void getTreeViewDetail(String loginid, final Context context) {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("LoginId", loginid);
            Call<ResponseTreeView> geneologyCall = apiServices.getTreeView(param);
            LoggerUtil.logItem(param);
            geneologyCall.enqueue(new Callback<ResponseTreeView>() {
                @Override
                public void onResponse(@NonNull Call<ResponseTreeView> call, @NonNull Response<ResponseTreeView> response) {
                    hideLoading();
                    LoggerUtil.logItem(response.body());
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        main_lo.setVisibility(View.VISIBLE);
                        removepreviousData();
                        LoggerUtil.logItem(response.body());
                        tree_view_arr = response.body().getGenealogytreelist();
                        setTreeView(tree_view_arr);
                    } else {
                        showAlert(response.body().getResponse(), R.color.red, R.drawable.error);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseTreeView> call, @NonNull Throwable t) {
                    hideLoading();
                }
            });
        } catch (Exception e) {
            hideLoading();
        }
    }

    public void setTreeView(List<GenealogytreelistItem> tree_view_arr) {
        try {
            hideKeyboard();
            for (int j = 0; j < tree_view_arr.size(); j++) {
                try {
                    //CASE 1
                    if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("1")) {
                        top_node_parentID = tree_view_arr.get(j).getParentId();
                        UrlConstants.treeview_top_loginID = tree_view_arr.get(j).getLoginId();
                        tree_l_one_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_one_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_one_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_one_customer_spillby.setVisibility(View.GONE);
                            tree_l_one_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_one_photo);

                        Log.e(">>>>getIdStatus", tree_view_arr.get(j).getIdStatus());
                        //CASE 2
                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("2") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("L")) {
                        twoL = tree_view_arr.get(j).getMemId();
                        tree_l_two_a_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_two_a_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_two_a_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_two_a_customer_spillby.setVisibility(View.GONE);
                            tree_l_two_a_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_two_a_photo);

                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("2") &&
                            tree_view_arr.get(j).getLeg().equalsIgnoreCase("R")) {
                        twoR = tree_view_arr.get(j).getMemId();
                        tree_l_two_b_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_two_b_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_two_b_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_two_b_customer_spillby.setVisibility(View.GONE);
                            tree_l_two_b_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_two_b_photo);
                        //CASE 3
                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("3") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("L")
                            && twoL.equals(tree_view_arr.get(j).getParentId())) {
                        three1 = tree_view_arr.get(j).getMemId();
                        tree_l_three_a_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_three_a_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_three_a_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_three_a_customer_spillby.setVisibility(View.GONE);
                            tree_l_three_a_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_three_a_photo);


                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("3") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("R")
                            && twoL.equals(tree_view_arr.get(j).getParentId())) {
                        three2 = tree_view_arr.get(j).getMemId();
                        tree_l_three_b_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_three_b_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_three_b_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_three_b_customer_spillby.setVisibility(View.GONE);
                            tree_l_three_b_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_three_b_photo);

                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("3") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("L")
                            && twoR.equals(tree_view_arr.get(j).getParentId())) {
                        three3 = tree_view_arr.get(j).getMemId();
                        tree_l_three_c_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_three_c_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_three_c_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_three_c_customer_spillby.setVisibility(View.GONE);
                            tree_l_three_c_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_three_c_photo);

                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("3") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("R")
                            && twoR.equals(tree_view_arr.get(j).getParentId())) {
                        three4 = tree_view_arr.get(j).getMemId();
                        tree_l_three_d_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_three_d_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_three_d_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_three_d_customer_spillby.setVisibility(View.GONE);
                            tree_l_three_d_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_three_d_photo);

                    }
                    // ######### Case 4
                    else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("4") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("L")
                            && three1.equals(tree_view_arr.get(j).getParentId())) {
                        tree_l_four_one_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_four_one_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_four_one_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_four_one_customer_spillby.setVisibility(View.GONE);
                            tree_l_four_one_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_four_one_photo);


                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("4") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("R")
                            && three1.equals(tree_view_arr.get(j).getParentId())) {
                        tree_l_four_two_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_four_two_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_four_two_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_four_two_customer_spillby.setVisibility(View.GONE);
                            tree_l_four_two_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_four_two_photo);

                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("4") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("L")
                            && three2.equals(tree_view_arr.get(j).getParentId())) {
                        tree_l_four_three_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_four_three_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_four_three_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_four_three_customer_spillby.setVisibility(View.GONE);
                            tree_l_four_three_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_four_three_photo);


                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("4") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("R")
                            && three2.equals(tree_view_arr.get(j).getParentId())) {
                        tree_l_four_four_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_four_four_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_four_four_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_four_four_customer_spillby.setVisibility(View.GONE);
                            tree_l_four_four_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_four_four_photo);


                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("4") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("L")
                            && three3.equals(tree_view_arr.get(j).getParentId())) {
                        tree_l_four_five_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_four_five_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_four_five_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_four_five_customer_spillby.setVisibility(View.GONE);
                            tree_l_four_five_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_four_five_photo);

                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("4") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("R")
                            && three3.equals(tree_view_arr.get(j).getParentId())) {
                        tree_l_four_six_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_four_six_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_four_six_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_four_six_customer_spillby.setVisibility(View.GONE);
                            tree_l_four_six_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_four_six_photo);


                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("4") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("L")
                            && three4.equals(tree_view_arr.get(j).getParentId())) {
                        tree_l_four_seven_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_four_seven_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_four_seven_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_four_seven_customer_spillby.setVisibility(View.GONE);
                            tree_l_four_seven_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }
//
                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_four_seven_photo);


                    } else if (tree_view_arr.get(j).getMemberLevel().equalsIgnoreCase("4") && tree_view_arr.get(j).getLeg().equalsIgnoreCase("R")
                            && three4.equals(tree_view_arr.get(j).getParentId())) {
                        tree_l_four_eight_customerid.setText(tree_view_arr.get(j).getLoginId());
                        tree_l_four_eight_customer_name.setText(tree_view_arr.get(j).getDisplayName());
                        if (tree_view_arr.get(j).getSponsorId().equalsIgnoreCase("")) {
                            tree_l_four_eight_customer_spillby.setVisibility(View.GONE);
                        } else {
                            tree_l_four_eight_customer_spillby.setVisibility(View.GONE);
                            tree_l_four_eight_customer_spillby.setText("spill by :" + tree_view_arr.get(j).getSponsorId());
                        }

                        Glide.with(context).load(tree_view_arr.get(j).getIdStatus())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.join_now)
                                        .error(R.drawable.join_now))
                                .into(tree_l_four_eight_photo);

                    }
                } catch (Error r) {
                    r.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ///set REGISTER////////////

            if (tree_l_one_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_one_customerid.getText().toString().trim().equalsIgnoreCase("")) {
//                                set ReGISTER
                tree_l_one_reg.setVisibility(View.VISIBLE);
                tree_l_one_lo.setVisibility(View.GONE);
                tree_l_one_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_one_reg.setVisibility(View.GONE);
                tree_l_one_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_two_a_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_two_a_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_two_a_reg.setVisibility(View.VISIBLE);
                tree_l_two_a_lo.setVisibility(View.GONE);
                tree_l_two_a_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_two_a_reg.setVisibility(View.GONE);
                tree_l_two_a_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_two_b_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_two_b_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_two_b_reg.setVisibility(View.VISIBLE);
                tree_l_two_b_lo.setVisibility(View.GONE);
                tree_l_two_b_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_two_b_reg.setVisibility(View.GONE);
                tree_l_two_b_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_three_a_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_three_a_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_three_a_reg.setVisibility(View.VISIBLE);
                tree_l_three_a_lo.setVisibility(View.GONE);
                tree_l_three_a_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_three_a_reg.setVisibility(View.GONE);
                tree_l_three_a_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_three_b_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_three_b_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_three_b_reg.setVisibility(View.VISIBLE);
                tree_l_three_b_lo.setVisibility(View.GONE);
                tree_l_three_b_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_three_b_reg.setVisibility(View.GONE);
                tree_l_three_b_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_three_c_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_three_c_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_three_c_reg.setVisibility(View.VISIBLE);
                tree_l_three_c_lo.setVisibility(View.GONE);
                tree_l_three_c_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_three_c_reg.setVisibility(View.GONE);
                tree_l_three_c_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_three_d_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_three_d_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_three_d_reg.setVisibility(View.VISIBLE);
                tree_l_three_d_lo.setVisibility(View.GONE);
                tree_l_three_d_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_three_d_reg.setVisibility(View.GONE);
                tree_l_three_d_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_four_one_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_four_one_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_four_one_reg.setVisibility(View.VISIBLE);
                tree_l_four_one_lo.setVisibility(View.GONE);
                tree_l_four_one_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_four_one_reg.setVisibility(View.GONE);
                tree_l_four_one_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_four_two_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_four_two_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_four_two_reg.setVisibility(View.VISIBLE);
                tree_l_four_two_lo.setVisibility(View.GONE);
                tree_l_four_two_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_four_two_reg.setVisibility(View.GONE);
                tree_l_four_two_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_four_three_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_four_three_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_four_three_reg.setVisibility(View.VISIBLE);
                tree_l_four_three_lo.setVisibility(View.GONE);
                tree_l_four_three_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_four_three_reg.setVisibility(View.GONE);
                tree_l_four_three_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_four_four_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_four_four_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_four_four_reg.setVisibility(View.VISIBLE);
                tree_l_four_four_lo.setVisibility(View.GONE);
                tree_l_four_four_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_four_four_reg.setVisibility(View.GONE);
                tree_l_four_four_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_four_five_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_four_five_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_four_five_reg.setVisibility(View.VISIBLE);
                tree_l_four_five_lo.setVisibility(View.GONE);
                tree_l_four_five_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_four_five_reg.setVisibility(View.GONE);
                tree_l_four_five_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_four_six_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_four_six_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_four_six_reg.setVisibility(View.VISIBLE);
                tree_l_four_six_lo.setVisibility(View.GONE);
                tree_l_four_six_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_four_six_reg.setVisibility(View.GONE);
                tree_l_four_six_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_four_seven_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_four_seven_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_four_seven_reg.setVisibility(View.VISIBLE);
                tree_l_four_seven_lo.setVisibility(View.GONE);
                tree_l_four_seven_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_four_seven_reg.setVisibility(View.GONE);
                tree_l_four_seven_lo.setVisibility(View.VISIBLE);
            }
            if (tree_l_four_eight_customer_name.getText().toString().trim().equalsIgnoreCase("")
                    && tree_l_four_eight_customerid.getText().toString().trim().equalsIgnoreCase("")) {
                tree_l_four_eight_reg.setVisibility(View.VISIBLE);
                tree_l_four_eight_lo.setVisibility(View.GONE);
                tree_l_four_eight_photo.setImageDrawable(getResources().getDrawable(R.drawable.join_now));
            } else {
                tree_l_four_eight_reg.setVisibility(View.GONE);
                tree_l_four_eight_lo.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_btn:
                hideKeyboard();
                checkDownlineForTree(search_et.getText().toString().trim(), "null", getActivity());
                break;
            case R.id.moveup:
                if (!(top_node_parentID.equalsIgnoreCase("0"))) {
                    checkDownlineForTree("null", top_node_parentID, getActivity());
                }
                break;
            case R.id.movetoroot:
                loginId = PreferencesManager.getInstance(context).getUSERID();
                getTreeViewDetail(loginId, getActivity());
                break;
            case R.id.node_one:
                loginId = tree_l_one_customerid.getText().toString();
                execute(node_one, tree_l_one_customerid, loginId, getActivity(), "");
                break;
            case R.id.node_two:
                loginId = tree_l_two_a_customerid.getText().toString();
                execute(node_two, tree_l_two_a_customerid, loginId, getActivity(), "L");
                break;
            case R.id.node_three:
                loginId = tree_l_two_b_customerid.getText().toString();
                execute(node_three, tree_l_two_b_customerid, loginId, getActivity(), "R");
                break;
            case R.id.node_four:
                loginId = tree_l_three_a_customerid.getText().toString();
                execute(node_four, tree_l_three_a_customerid, loginId, getActivity(), "L");
                break;
            case R.id.node_five:
                loginId = tree_l_three_b_customerid.getText().toString();
                execute(node_five, tree_l_three_b_customerid, loginId, getActivity(), "R");
                break;
            case R.id.node_six:
                loginId = tree_l_three_c_customerid.getText().toString();
                execute(node_six, tree_l_three_c_customerid, loginId, getActivity(), "L");
                break;
            case R.id.node_seven:
                loginId = tree_l_three_d_customerid.getText().toString();
                execute(node_seven, tree_l_three_d_customerid, loginId, getActivity(), "R");
                break;

            case R.id.node_eight:
                loginId = tree_l_four_one_customerid.getText().toString();
                execute(node_eight, tree_l_four_one_customerid, loginId, getActivity(), "L");
                break;
            case R.id.node_nine:
                loginId = tree_l_four_two_customerid.getText().toString();
                execute(node_nine, tree_l_four_two_customerid, loginId, getActivity(), "R");
                break;
            case R.id.node_ten:
                loginId = tree_l_four_three_customerid.getText().toString();
                execute(node_ten, tree_l_four_three_customerid, loginId, getActivity(), "L");
                break;
            case R.id.node_eleven:
                loginId = tree_l_four_four_customerid.getText().toString();
                execute(node_eleven, tree_l_four_four_customerid, loginId, getActivity(), "R");
                break;
            case R.id.node_twelve:
                loginId = tree_l_four_five_customerid.getText().toString();
                execute(node_twelve, tree_l_four_five_customerid, loginId, getActivity(), "L");
                break;
            case R.id.node_thirteen:
                loginId = tree_l_four_six_customerid.getText().toString();
                execute(node_thirteen, tree_l_four_six_customerid, loginId, getActivity(), "R");
                break;
            case R.id.node_fourteen:
                loginId = tree_l_four_seven_customerid.getText().toString();
                execute(node_fourteen, tree_l_four_seven_customerid, loginId, getActivity(), "L");
                break;
            case R.id.node_fifteen:
                loginId = tree_l_four_eight_customerid.getText().toString();
                execute(node_fifteen, tree_l_four_eight_customerid, loginId, getActivity(), "R");
                break;

        }
    }

    private void execute(LinearLayout clickedView, TextView view, String loginId, final Context context, String selected_leg) {
        Bundle param = new Bundle();
        if (loginId.equalsIgnoreCase("")) {
            if (view != tree_l_one_customerid) {
                if (view == tree_l_two_a_customerid) {
                    loginId = tree_l_one_customerid.getText().toString();
                    login_name = tree_l_one_customer_name.getText().toString();
                } else if (view == tree_l_two_b_customerid) {
                    loginId = tree_l_one_customerid.getText().toString();
                    login_name = tree_l_one_customer_name.getText().toString();
                } else if (view == tree_l_three_a_customerid) {
                    loginId = tree_l_two_a_customerid.getText().toString();
                    login_name = tree_l_two_a_customer_name.getText().toString();
                } else if (view == tree_l_three_b_customerid) {
                    loginId = tree_l_two_a_customerid.getText().toString();
                    login_name = tree_l_two_a_customer_name.getText().toString();
                } else if (view == tree_l_three_c_customerid) {
                    loginId = tree_l_two_b_customerid.getText().toString();
                    login_name = tree_l_two_b_customer_name.getText().toString();
                } else if (view == tree_l_three_d_customerid) {
                    loginId = tree_l_two_b_customerid.getText().toString();
                    login_name = tree_l_two_b_customer_name.getText().toString();
                } else if (view == tree_l_four_one_customerid) {
                    loginId = tree_l_three_a_customerid.getText().toString();
                    login_name = tree_l_three_a_customer_name.getText().toString();
                } else if (view == tree_l_four_two_customerid) {
                    loginId = tree_l_three_a_customerid.getText().toString();
                    login_name = tree_l_three_a_customer_name.getText().toString();
                } else if (view == tree_l_four_three_customerid) {
                    loginId = tree_l_three_b_customerid.getText().toString();
                    login_name = tree_l_three_b_customer_name.getText().toString();
                } else if (view == tree_l_four_four_customerid) {
                    loginId = tree_l_three_b_customerid.getText().toString();
                    login_name = tree_l_three_b_customer_name.getText().toString();
                } else if (view == tree_l_four_five_customerid) {
                    loginId = tree_l_three_c_customerid.getText().toString();
                    login_name = tree_l_three_c_customer_name.getText().toString();
                } else if (view == tree_l_four_six_customerid) {
                    loginId = tree_l_three_c_customerid.getText().toString();
                    login_name = tree_l_three_c_customer_name.getText().toString();
                } else if (view == tree_l_four_seven_customerid) {
                    loginId = tree_l_three_d_customerid.getText().toString();
                    login_name = tree_l_three_d_customer_name.getText().toString();
                } else if (view == tree_l_four_eight_customerid) {
                    loginId = tree_l_three_d_customerid.getText().toString();
                    login_name = tree_l_three_d_customer_name.getText().toString();
                }
            }


            if (loginId.equalsIgnoreCase("")) {
                showToastS("Invalid Position.");
            } else {
                param.putString("place_under_id", loginId);
                param.putString("place_under_name", login_name);
                param.putString("referrence_id", PreferencesManager.getInstance(context).getUSERID());
                param.putString("selected_leg", selected_leg);
                //showToastS("" + param);
//                gotoActivity(context, TreeView_Register_Here.class, param);

                start_once = 1;
                Utils.hideSoftKeyboard(getActivity());
                Intent intent = new Intent(context, SignUp.class);
                intent.putExtra("android.intent.extra.INTENT", param);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        } else {
            Log.e("ELSE  ", " = = " + loginId);
            getTreeViewDetail(loginId, context);
        }
    }

    private void removepreviousData() {
        search_et.setText("");
        tree_l_one_customerid.setText("");
        tree_l_one_customer_name.setText("");
        tree_l_one_customer_spillby.setText("");
        tree_l_two_a_customerid.setText("");
        tree_l_two_a_customer_name.setText("");
        tree_l_two_a_customer_spillby.setText("");
        tree_l_two_b_customerid.setText("");
        tree_l_two_b_customer_name.setText("");
        tree_l_two_b_customer_spillby.setText("");
        tree_l_three_a_customerid.setText("");
        tree_l_three_a_customer_name.setText("");
        tree_l_three_a_customer_spillby.setText("");
        tree_l_three_b_customerid.setText("");
        tree_l_three_b_customer_name.setText("");
        tree_l_three_b_customer_spillby.setText("");
        tree_l_three_c_customerid.setText("");
        tree_l_three_c_customer_name.setText("");
        tree_l_three_c_customer_spillby.setText("");
        tree_l_three_d_customerid.setText("");
        tree_l_three_d_customer_name.setText("");
        tree_l_three_d_customer_spillby.setText("");

        tree_l_four_one_customerid.setText("");
        tree_l_four_two_customerid.setText("");
        tree_l_four_three_customerid.setText("");
        tree_l_four_four_customerid.setText("");
        tree_l_four_five_customerid.setText("");
        tree_l_four_six_customerid.setText("");
        tree_l_four_seven_customerid.setText("");
        tree_l_four_eight_customerid.setText("");

        tree_l_four_one_customer_name.setText("");
        tree_l_four_two_customer_name.setText("");
        tree_l_four_three_customer_name.setText("");
        tree_l_four_four_customer_name.setText("");
        tree_l_four_five_customer_name.setText("");
        tree_l_four_six_customer_name.setText("");
        tree_l_four_seven_customer_name.setText("");
        tree_l_four_eight_customer_name.setText("");

        tree_l_four_one_customer_spillby.setText("");
        tree_l_four_two_customer_spillby.setText("");
        tree_l_four_three_customer_spillby.setText("");
        tree_l_four_four_customer_spillby.setText("");
        tree_l_four_five_customer_spillby.setText("");
        tree_l_four_six_customer_spillby.setText("");
        tree_l_four_seven_customer_spillby.setText("");
        tree_l_four_eight_customer_spillby.setText("");
    }

    private void checkDownlineForTree(String searchLoginId, String parentId, final Context context) {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("LoginId", PreferencesManager.getInstance(context).getUSERID());
            param.addProperty("SearchLoginId", searchLoginId);
            param.addProperty("ParentId", parentId);
            Call<CheckDownlineResponse> checkDownlineCall = apiServices.getCheckDownline(param);
            checkDownlineCall.enqueue(new Callback<CheckDownlineResponse>() {
                @Override
                public void onResponse(@NonNull Call<CheckDownlineResponse> call, @NonNull Response<CheckDownlineResponse> response) {
                    hideLoading();
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        getTreeViewDetail(response.body().getLoginId(), getActivity());
                    } else {
//                        showAlert( response.body().getMsg().toString(), R.color.red, R.drawable.error );
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CheckDownlineResponse> call, @NonNull Throwable t) {
                    hideLoading();
                }
            });
        } catch (Exception e) {
            Log.e("", "");
        }
    }

//    void ChooseFreomTreeDialog(String loginId, final Context context) {
//        dialog = new Dialog(context);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
//        dialog.setContentView(R.layout.dialog_choose_from_tree);
//
//        Button btn_tree_details = dialog.findViewById(R.id.btn_tree_details);
//        Button btn_show_business = dialog.findViewById(R.id.btn_show_business);
//
//        btn_show_business.setOnClickListener(v -> {
//            BusinessDialog(loginId, context);
//            dialog.dismiss();
//        });
//
//        btn_tree_details.setOnClickListener(v -> {
//            getTreeViewDetail(loginId, context);
//            dialog.dismiss();
//        });
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setCancelable(true);
//        dialog.show();
//    }

    void BusinessDialog(String loginId, final Context context) {
        BusinessDialog = new Dialog(context);
        BusinessDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        BusinessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BusinessDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        BusinessDialog.setContentView(R.layout.dialog_tree_business);

        Button btn_ok = BusinessDialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(v -> BusinessDialog.dismiss());

        TextView tv_user_name = BusinessDialog.findViewById(R.id.tv_user_name);
        TextView tv_login_id = BusinessDialog.findViewById(R.id.tv_login_id);
        TextView tv_activate_with = BusinessDialog.findViewById(R.id.tv_activate_with);
        TextView tv_sponsor_id = BusinessDialog.findViewById(R.id.tv_sponsor_id);
        TextView tv_join_on = BusinessDialog.findViewById(R.id.tv_join_on);

        TextView active_left = BusinessDialog.findViewById(R.id.active_left);
        TextView inactive_left = BusinessDialog.findViewById(R.id.inactive_left);
        TextView totalmembers_left = BusinessDialog.findViewById(R.id.totalmembers_left);
        TextView totalbuis_left = BusinessDialog.findViewById(R.id.totalbuis_left);

        TextView active_right = BusinessDialog.findViewById(R.id.active_right);
        TextView inactive_right = BusinessDialog.findViewById(R.id.inactive_right);
        TextView totalmembers_right = BusinessDialog.findViewById(R.id.totalmembers_right);
        TextView totalbuis_right = BusinessDialog.findViewById(R.id.totalbuis_right);

        TextView active_total = BusinessDialog.findViewById(R.id.active_total);
        TextView inactive_total = BusinessDialog.findViewById(R.id.inactive_total);
        TextView totalmembers_total = BusinessDialog.findViewById(R.id.totalmembers_total);
        TextView totalbuis_total = BusinessDialog.findViewById(R.id.totalbuis_total);
        TextView currentbvreight=BusinessDialog.findViewById(R.id.txt_cbright);
        TextView currentbvleft=BusinessDialog.findViewById(R.id.txt_cbleft);
        TextView currentbvtotal=BusinessDialog.findViewById(R.id.txti_cb_total);


        try {
            for (int i = 0; i < tree_view_arr.size(); i++) {
                if (tree_view_arr.get(i).getLoginId().equalsIgnoreCase(loginId)) {
                    tv_user_name.setText(tree_view_arr.get(i).getDisplayName());

                    tv_login_id.setText(tree_view_arr.get(i).getLoginId());
//                    tv_activate_with.setText(array.getJSONObject(i).getString("PackageAmount"));
                    tv_sponsor_id.setText(tree_view_arr.get(i).getSponsorLoginId());
                    tv_join_on.setText(tree_view_arr.get(i).getJoiningDate());

                    active_left.setText(tree_view_arr.get(i).getPermanentLeg1());
                    inactive_left.setText(tree_view_arr.get(i).getLeftNonActive());
                    totalmembers_left.setText(tree_view_arr.get(i).getAllLeg1());
                    totalbuis_left.setText(tree_view_arr.get(i).getBvLeft());

                    active_right.setText(tree_view_arr.get(i).getPermanentLeg2());
                    inactive_right.setText(tree_view_arr.get(i).getRightNonActive());
                    totalmembers_right.setText(tree_view_arr.get(i).getAllLeg2());
                    totalbuis_right.setText(tree_view_arr.get(i).getBvRight());
                    currentbvleft.setText(tree_view_arr.get(i).getCurrLeft());
                    currentbvreight.setText(tree_view_arr.get(i).getCurrRight());
                    Log.e("left",String.valueOf(tree_view_arr.get(i).getCurrLeft()));
                    Log.e("right",String.valueOf(tree_view_arr.get(i).getCurrRight()));


                    String totalcb= String.valueOf(Float.parseFloat(tree_view_arr.get(i).getCurrLeft())+Float.parseFloat(tree_view_arr.get(i).getCurrRight()));
                    Log.e("total",String.valueOf(totalcb));

                    currentbvtotal.setText(totalcb);

                    active_total.setText(String.valueOf(Integer.parseInt(tree_view_arr.get(i).getPermanentLeg1()) + Integer.parseInt(tree_view_arr.get(i).getPermanentLeg2())));
                    inactive_total.setText(String.valueOf(Integer.parseInt(tree_view_arr.get(i).getLeftNonActive()) + Integer.parseInt(tree_view_arr.get(i).getRightNonActive())));
                    totalmembers_total.setText(String.valueOf(Integer.parseInt(tree_view_arr.get(i).getAllLeg1()) + Integer.parseInt(tree_view_arr.get(i).getAllLeg2())));
                    totalbuis_total.setText(String.valueOf((Double.parseDouble(tree_view_arr.get(i).getBvLeft()) + Double.parseDouble(tree_view_arr.get(i).getBvRight()))));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BusinessDialog.setCanceledOnTouchOutside(true);
        BusinessDialog.setCancelable(true);
        BusinessDialog.show();
    }
}
