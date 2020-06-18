package com.yehm.fragments.my_wallet;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.model.PayoutLedgerResponse;
import com.yehm.model.PayoutRequestlistItem;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayoutLedger extends BaseFragment {
    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.payout_ledger_list)
    RecyclerView payoutLedgerList;
    @BindView(R.id.goto_top)
    Button gotoTop;
    Unbinder unbinder;
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    List<PayoutRequestlistItem> payoutLedger_arr;
    View view;
    LinearLayoutManager linearLayoutManager;
    PayoutLedgerAdapter payout_ledger_adapter;
    private UserFilter userFilter;

    public PayoutLedger() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.payout_ledger, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getPayoutLedger();
        } else {
            createInfoDialog(context, "Alert", getString(R.string.alert_internet));
        }
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                payout_ledger_adapter.getFilter().filter(s);
            }
        });

        payoutLedgerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItem >= 1) {
                    gotoTop.setVisibility(View.VISIBLE);
                } else {
                    gotoTop.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getPayoutLedger() {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("Fk_MemId", PreferencesManager.getInstance(context).getMEMBERID());
            Call<PayoutLedgerResponse> payoutLedgerCall = apiServices.getPayoutLedger(param);
            payoutLedgerCall.enqueue(new Callback<PayoutLedgerResponse>() {
                @Override
                public void onResponse(@NonNull Call<PayoutLedgerResponse> call, @NonNull Response<PayoutLedgerResponse> response) {
                    hideLoading();
                    LoggerUtil.logItem(response.body());
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        payoutLedger_arr = response.body().getPayoutRequestlist();
                        payoutLedgerList.setVisibility(View.VISIBLE);
                        linearLayoutManager = new LinearLayoutManager(context);
                        payoutLedgerList.setLayoutManager(linearLayoutManager);
                        payoutLedgerList.setItemAnimator(new DefaultItemAnimator());
                        payout_ledger_adapter = new PayoutLedgerAdapter(context, payoutLedger_arr);
                        payoutLedgerList.setAdapter(payout_ledger_adapter);
                    } else {
                        searchEt.setVisibility(View.GONE);
                        noRecFound.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PayoutLedgerResponse> call, @NonNull Throwable t) {
                    hideLoading();
                }
            });
        } catch (Exception e) {
            Log.e("", "");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.goto_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.goto_top:
                payoutLedgerList.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private static class UserFilter extends Filter {
        PayoutLedgerAdapter adapter;
        List<PayoutRequestlistItem> myDirectResponses;
        List<PayoutRequestlistItem> filteredList;

        private UserFilter(PayoutLedgerAdapter adapter, List<PayoutRequestlistItem> originalList) {
            super();
            this.adapter = adapter;
            this.myDirectResponses = new LinkedList<PayoutRequestlistItem>(originalList);
            this.filteredList = new ArrayList<PayoutRequestlistItem>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(myDirectResponses);
            } else {
                final String filterPattern = constraint.toString().trim();
                for (final PayoutRequestlistItem user : myDirectResponses) {
                    if (Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getTransDate()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getDrAmount()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getBalance()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getCrAmount()).find()) {
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
            adapter.response.addAll((ArrayList<PayoutRequestlistItem>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    public class PayoutLedgerAdapter extends RecyclerView.Adapter<PayoutLedgerAdapter.PaidHolder> implements Filterable {
        View view;
        List<PayoutRequestlistItem> response;

        PayoutLedgerAdapter(Context context, List<PayoutRequestlistItem> list) {
            this.response = list;
        }

        @NonNull
        @Override
        public PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.e_wallet_ledger, parent, false);
            return new PaidHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PaidHolder holder, int position) {
            try {
                holder.date.setText(response.get(position).getTransDate());
                holder.date.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                holder.date.setSelected(true);
                holder.date.setSingleLine(true);

                holder.debit.setText(response.get(position).getDrAmount());
                holder.credit.setText(response.get(position).getCrAmount());
                holder.credit.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                holder.credit.setSelected(true);
                holder.credit.setSingleLine(true);

                holder.balance.setText(response.get(position).getBalance());
                holder.remark.setText(response.get(position).getNarration());
                holder.remark.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                holder.remark.setSelected(true);
                holder.remark.setSingleLine(true);

                if (Float.parseFloat(response.get(position).getCrAmount()) == 0) {
                    holder.left_view.setBackgroundColor(context.getResources().getColor(R.color.blocked));
                } else {
                    holder.left_view.setBackgroundColor(context.getResources().getColor(R.color.active));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return response.size();
        }

        @Override
        public Filter getFilter() {
            if (userFilter == null)
                userFilter = new UserFilter(this, payoutLedger_arr);
            return userFilter;
        }

        public class PaidHolder extends RecyclerView.ViewHolder {
            TextView date, debit, credit, remark, balance;
            View left_view;

            public PaidHolder(View itemView) {
                super(itemView);
                date = itemView.findViewById(R.id.date);
                debit = itemView.findViewById(R.id.debit);
                credit = itemView.findViewById(R.id.credit);
                remark = itemView.findViewById(R.id.remark);
                balance = itemView.findViewById(R.id.balance);
            }
        }
    }

}