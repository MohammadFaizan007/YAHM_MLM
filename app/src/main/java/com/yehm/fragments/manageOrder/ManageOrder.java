package com.yehm.fragments.manageOrder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.model.ManageOrderlistItem;
import com.yehm.model.ResponseManageOrder;
import com.yehm.model.ResponseViewOrderItem;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageOrder extends BaseFragment {
    public static ResponseViewOrderItem view;
    public String order_id_st;
    @BindView(R.id.rv_manageorder)
    RecyclerView rv_manageorder;
    @BindView(R.id.goto_top)
    Button goto_top;
    @BindView(R.id.search_et)
    EditText search_et;
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    List<ManageOrderlistItem> order_arr;
    ManageOrderAdapter manageOrderAdapter;
    LinearLayoutManager linearLayoutManager;
    Dialog view_dialog;
    TextView productName, productCode, quantity, bv_dialog, mrp_dialog, netAmount, cgst, sgst, igst;
    Unbinder unbinder;
    private UserFilter userFilter;

    public ManageOrder() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getManageOrder();
        } else {
            showAlert(getResources().getString(R.string.alert_internet), R.color.red, R.drawable.error);
        }

        goto_top.setOnClickListener(view1 -> rv_manageorder.smoothScrollToPosition(0));

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                manageOrderAdapter.getFilter().filter(s);
            }
        });

        rv_manageorder.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


    private void getManageOrder() {
        showLoading();
        JsonObject object = new JsonObject();
        object.addProperty("Fk_MemId", PreferencesManager.getInstance(context).getMEMBERID());
        Call<ResponseManageOrder> manageOrderCall = apiServices.getManageOrder(object);
        manageOrderCall.enqueue(new Callback<ResponseManageOrder>() {
            @Override
            public void onResponse(Call<ResponseManageOrder> call, Response<ResponseManageOrder> response) {
                hideLoading();
                LoggerUtil.logItem(response.body());
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        order_arr = response.body().getManageOrderlist();
                        rv_manageorder.setVisibility(View.VISIBLE);
                        linearLayoutManager = new LinearLayoutManager(context);
                        rv_manageorder.setLayoutManager(linearLayoutManager);
                        rv_manageorder.setItemAnimator(new DefaultItemAnimator());
                        manageOrderAdapter = new ManageOrderAdapter(context, order_arr);
                        rv_manageorder.setAdapter(manageOrderAdapter);
                    } else {
                        search_et.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "No records found.", Toast.LENGTH_SHORT).show();
                        noRecFound.setVisibility(View.VISIBLE);
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseManageOrder> call, Throwable t) {

            }
        });
    }

    public void getManageOrderView() {
        showLoading();
        JsonObject param = new JsonObject();
        param.addProperty("OrderId", order_id_st);
        Call<ResponseViewOrderItem> responseViewOrderItemCall = apiServices.getViewOrderItem(param);
        responseViewOrderItemCall.enqueue(new Callback<ResponseViewOrderItem>() {
            @Override
            public void onResponse(Call<ResponseViewOrderItem> call, Response<ResponseViewOrderItem> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        viewDownline(response.body());
                        view = response.body();
                    } else {
                        showToastS(response.body().getResponse());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseViewOrderItem> call, Throwable t) {

            }
        });
    }

    private void viewDownline(ResponseViewOrderItem view) {
        try {
            hideKeyboard();
            view_dialog = new Dialog(context);
            view_dialog.setCanceledOnTouchOutside(false);
            view_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            view_dialog.setContentView(R.layout.view_dialog);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
            view_dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            view_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            ImageView cancel = view_dialog.findViewById(R.id.img_cancel);
            Button close = view_dialog.findViewById(R.id.btn_close);

            productName = view_dialog.findViewById(R.id.productName);
            productCode = view_dialog.findViewById(R.id.productCode);
            quantity = view_dialog.findViewById(R.id.quantity);
            bv_dialog = view_dialog.findViewById(R.id.bv_dialog);
            mrp_dialog = view_dialog.findViewById(R.id.mrp_dialog);
            netAmount = view_dialog.findViewById(R.id.netAmount);
            cgst = view_dialog.findViewById(R.id.cgst);
            sgst = view_dialog.findViewById(R.id.sgst);
            igst = view_dialog.findViewById(R.id.igst);

            productName.setText(view.getManageOrderView().getProductName());
            productCode.setText(view.getManageOrderView().getProductCode());
            quantity.setText(view.getManageOrderView().getQuantity());
            bv_dialog.setText(view.getManageOrderView().getBv());
            mrp_dialog.setText(view.getManageOrderView().getMrp());
            netAmount.setText(view.getManageOrderView().getSgst());
            cgst.setText(view.getManageOrderView().getCgst());
            sgst.setText(view.getManageOrderView().getSgst());
            igst.setText(view.getManageOrderView().getIgst());

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view_dialog.dismiss();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view_dialog.dismiss();
                }
            });

            view_dialog.show();
        } catch (Exception e) {

        }
    }

    private static class UserFilter extends Filter {
        ManageOrderAdapter adapter;
        List<ManageOrderlistItem> myDirectResponses;
        List<ManageOrderlistItem> filteredList;

        private UserFilter(ManageOrderAdapter adapter, List<ManageOrderlistItem> originalList) {
            super();
            this.adapter = adapter;
            this.myDirectResponses = new LinkedList<ManageOrderlistItem>(originalList);
            this.filteredList = new ArrayList<ManageOrderlistItem>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(myDirectResponses);
            } else {
                final String filterPattern = constraint.toString().trim();
                for (final ManageOrderlistItem user : myDirectResponses) {
                    if (Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getName()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getOrderDate()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getOrderNo()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getBusinessVolume()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getNetAmount()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getStatus()).find()) {
                        filteredList.add(user);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.response.clear();
            adapter.response.addAll((ArrayList<ManageOrderlistItem>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.PaidHolder> implements Filterable {
        View view;
        List<ManageOrderlistItem> response;

        public ManageOrderAdapter(Context context, List<ManageOrderlistItem> list) {
            this.response = list;
        }

        @NonNull
        @Override
        public PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_order_adapter, parent, false);
            return new PaidHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PaidHolder holder, int position) {
            try {
                order_id_st = response.get(position).getOrderId();
                holder.orderNo.setText(response.get(position).getOrderNo());
                holder.memberName.setText(response.get(position).getName());
                holder.amt.setText(response.get(position).getNetAmount());
                holder.bv.setText(response.get(position).getBusinessVolume());
                holder.orderDate.setText(response.get(position).getOrderDate());

                String sta = response.get(position).getStatus();
                if (sta.equalsIgnoreCase("Confirmed") || sta.equalsIgnoreCase("Invoiced")) {
                    holder.status.setText(sta);
                    holder.status.setTextColor(context.getResources().getColor(R.color.active));
                } else {
                    holder.status.setText(sta);
                    holder.status.setTextColor(context.getResources().getColor(R.color.blocked));
                }

                holder.view.setOnClickListener(v -> {
                    getManageOrderView();

                });

            } catch (Exception e) {
            }
        }

        @Override
        public int getItemCount() {
            return response.size();
        }

        @Override
        public Filter getFilter() {
            if (userFilter == null) userFilter = new UserFilter(this, order_arr);
            return userFilter;
        }

        public class PaidHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.order_no)
            TextView orderNo;
            @BindView(R.id.member_name)
            TextView memberName;
            @BindView(R.id.amt)
            TextView amt;
            @BindView(R.id.order_date)
            TextView orderDate;
            @BindView(R.id.status)
            TextView status;
            @BindView(R.id.bv)
            TextView bv;
            @BindView(R.id.view)
            TextView view;

            public PaidHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}


