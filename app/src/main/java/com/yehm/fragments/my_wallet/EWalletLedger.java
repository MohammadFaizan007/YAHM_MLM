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
import com.yehm.model.EWalletLedgerResponse;
import com.yehm.model.EWalletLedgerlistItem;
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

public class EWalletLedger extends BaseFragment {
    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.payout_ledger_list)
    RecyclerView ewalletLedgerList;
    @BindView(R.id.goto_top)
    Button gotoTop;
    Unbinder unbinder;
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    List<EWalletLedgerlistItem> ewalletLedger_arr;
    View view;
    LinearLayoutManager linearLayoutManager;
    EwalletLedgerAdapter ewallet_ledger_adapter;
    private UserFilter userFilter;

    public EWalletLedger() {
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
            getEwalletLedger();
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
                ewallet_ledger_adapter.getFilter().filter(s);
            }
        });

        ewalletLedgerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void getEwalletLedger() {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("Fk_MemId", PreferencesManager.getInstance(context).getMEMBERID());
            Call<EWalletLedgerResponse> payoutLedgerCall = apiServices.getEwalletLedger(param);
            payoutLedgerCall.enqueue(new Callback<EWalletLedgerResponse>() {
                @Override
                public void onResponse(@NonNull Call<EWalletLedgerResponse> call, @NonNull Response<EWalletLedgerResponse> response) {
                    hideLoading();
                    LoggerUtil.logItem(response.body());
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        ewalletLedger_arr = response.body().getEWalletLedgerlist();
                        ewalletLedgerList.setVisibility(View.VISIBLE);
                        linearLayoutManager = new LinearLayoutManager(context);
                        ewalletLedgerList.setLayoutManager(linearLayoutManager);
                        ewalletLedgerList.setItemAnimator(new DefaultItemAnimator());
                        ewallet_ledger_adapter = new EwalletLedgerAdapter(context, ewalletLedger_arr);
                        ewalletLedgerList.setAdapter(ewallet_ledger_adapter);
                    } else {
                        searchEt.setVisibility(View.GONE);
                        noRecFound.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EWalletLedgerResponse> call, @NonNull Throwable t) {
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
                ewalletLedgerList.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private static class UserFilter extends Filter {
        EwalletLedgerAdapter adapter;
        List<EWalletLedgerlistItem> myDirectResponses;
        List<EWalletLedgerlistItem> filteredList;

        private UserFilter(EwalletLedgerAdapter adapter, List<EWalletLedgerlistItem> originalList) {
            super();
            this.adapter = adapter;
            this.myDirectResponses = new LinkedList<EWalletLedgerlistItem>(originalList);
            this.filteredList = new ArrayList<EWalletLedgerlistItem>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(myDirectResponses);
            } else {
                final String filterPattern = constraint.toString().trim();
                for (final EWalletLedgerlistItem user : myDirectResponses) {
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
            adapter.response.addAll((ArrayList<EWalletLedgerlistItem>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    public class EwalletLedgerAdapter extends RecyclerView.Adapter<EwalletLedgerAdapter.PaidHolder> implements Filterable {
        View view;
        List<EWalletLedgerlistItem> response;

        public EwalletLedgerAdapter(Context context, List<EWalletLedgerlistItem> list) {
            this.response = list;
        }

        @NonNull
        @Override
        public PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.e_wallet_ledger, parent, false);
            PaidHolder paidHolder = new PaidHolder(view);
            return paidHolder;
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

                holder.remark.setText(response.get(position).getNarration());
                holder.remark.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                holder.remark.setSelected(true);
                holder.remark.setSingleLine(true);

                holder.balance.setText(response.get(position).getBalance());
            } catch (Exception e) {
            }
        }

        @Override
        public int getItemCount() {
            return response.size();
        }

        @Override
        public Filter getFilter() {
            if (userFilter == null)
                userFilter = new UserFilter(this, ewalletLedger_arr);
            return userFilter;
        }

        public class PaidHolder extends RecyclerView.ViewHolder {
            TextView date, debit, credit, remark, balance;

            public PaidHolder(View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.date);
                debit = (TextView) itemView.findViewById(R.id.debit);
                credit = (TextView) itemView.findViewById(R.id.credit);
                remark = (TextView) itemView.findViewById(R.id.remark);
                balance = (TextView) itemView.findViewById(R.id.balance);
            }
        }
    }
}