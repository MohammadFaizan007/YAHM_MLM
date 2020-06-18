package com.yehm.fragments.my_income;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yehm.model.MyIncomeResponse;
import com.yehm.model.MyincomelistItem;
import com.yehm.model.PayoutdetailslistItem;
import com.yehm.model.ViewIncomeResponse;
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

public class PayoutReport extends BaseFragment {
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    Unbinder unbinder;
    View view;
    RecyclerView payout_report_list, viewAllIncome_list;
    LinearLayoutManager linearLayoutManager;
    Dialog view_doc_dialog;
    PayoutReport_adapter payout_report_adapter;
    List<MyincomelistItem> payoutReportResponse_list;
    EditText search_et;
    RelativeLayout no_rec_found;
    private UserFilter userFilter;
    private Button goto_top;

    public PayoutReport() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.payout_report, container, false);
        payout_report_list = view.findViewById(R.id.my_direct_list);
        search_et = view.findViewById(R.id.search_et);
        goto_top = view.findViewById(R.id.goto_top);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getPayoutReport();
        } else {
            createInfoDialog(context, "Alert", getString(R.string.alert_internet));
        }
        payout_report_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        goto_top.setOnClickListener(v -> {
            payout_report_list.smoothScrollToPosition(0);
        });

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                payout_report_adapter.getFilter().filter(s);
            }
        });

    }

    private void getPayoutReport() {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("FK_MemID", PreferencesManager.getInstance(context).getMEMBERID());
            param.addProperty("PayoutNo", "");
            LoggerUtil.logItem(param);
            Call<MyIncomeResponse> payoutLedgerCall = apiServices.getIncome(param);
            payoutLedgerCall.enqueue(new Callback<MyIncomeResponse>() {
                @Override
                public void onResponse(@NonNull Call<MyIncomeResponse> call, @NonNull Response<MyIncomeResponse> response) {
                    hideLoading();
//                    Log.e( ">>>>>", response.toString() );
                    LoggerUtil.logItem(response.body());
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        payoutReportResponse_list = response.body().getMyincomelist();
                        linearLayoutManager = new LinearLayoutManager(context);
                        payout_report_list.setLayoutManager(linearLayoutManager);
                        payout_report_list.setItemAnimator(new DefaultItemAnimator());
                        payout_report_adapter = new PayoutReport_adapter(context, payoutReportResponse_list);
                        payout_report_list.setAdapter(payout_report_adapter);
                    } else {
                        search_et.setVisibility(View.GONE);
                        noRecFound.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MyIncomeResponse> call, @NonNull Throwable t) {
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

    private void viewIncome(String payout_no) {
        hideKeyboard();
        view_doc_dialog = new Dialog(context);
        view_doc_dialog.setCanceledOnTouchOutside(false);
        view_doc_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        view_doc_dialog.setContentView(R.layout.view_income_dialog);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
        view_doc_dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        view_doc_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        viewAllIncome_list = view_doc_dialog.findViewById(R.id.viewAllIncome_list);
        ImageView cancel = view_doc_dialog.findViewById(R.id.cancel);
        Button goto_top_d = view_doc_dialog.findViewById(R.id.goto_top);
        no_rec_found = view_doc_dialog.findViewById(R.id.noRecFound);

        viewAllIncome_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItem >= 1) {
                    goto_top_d.setVisibility(View.VISIBLE);
                } else {
                    goto_top_d.setVisibility(View.GONE);
                }
            }
        });

        goto_top_d.setOnClickListener(v -> {
            viewAllIncome_list.smoothScrollToPosition(0);
        });

        getAllIncome(payout_no);

        cancel.setOnClickListener(v -> view_doc_dialog.dismiss());

        view_doc_dialog.show();
    }

    private void getAllIncome(String p_no) {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("FK_MemID", PreferencesManager.getInstance(context).getMEMBERID());
            param.addProperty("PayoutNo", p_no);
            Call<ViewIncomeResponse> payoutLedgerCall = apiServices.getViewIncomeDetails(param);
            payoutLedgerCall.enqueue(new Callback<ViewIncomeResponse>() {
                @Override
                public void onResponse(@NonNull Call<ViewIncomeResponse> call, @NonNull Response<ViewIncomeResponse> response) {
                    hideLoading();
                    LoggerUtil.logItem(response.body());
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        ViewIncome_adapter listAdapter = new ViewIncome_adapter(context, response.body().getPayoutdetailslist());
                        linearLayoutManager = new LinearLayoutManager(context);
                        viewAllIncome_list.setLayoutManager(linearLayoutManager);
                        viewAllIncome_list.setAdapter(listAdapter);
                    } else {
                        viewAllIncome_list.setVisibility(View.GONE);
//                        searchEt.setVisibility( View.GONE );
                        no_rec_found.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ViewIncomeResponse> call, @NonNull Throwable t) {
                    hideLoading();
                }
            });
        } catch (Exception e) {
            Log.e("", "");
        }
    }

    private static class UserFilter extends Filter {
        PayoutReport_adapter adapter;
        List<MyincomelistItem> myDirectResponses;
        List<MyincomelistItem> filteredList;

        private UserFilter(PayoutReport_adapter adapter, List<MyincomelistItem> originalList) {
            super();
            this.adapter = adapter;
            this.myDirectResponses = new LinkedList<MyincomelistItem>(originalList);
            this.filteredList = new ArrayList<MyincomelistItem>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(myDirectResponses);
            } else {
                final String filterPattern = constraint.toString().trim();
                for (final MyincomelistItem user : myDirectResponses) {
                    if (Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getGrossAmount()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getDisplayName()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getClosingDate()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getNetAmount()).find()) {
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
            adapter.response.addAll((ArrayList<MyincomelistItem>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    public class PayoutReport_adapter extends RecyclerView.Adapter<PayoutReport_adapter.PaidHolder> implements Filterable {
        View view;
        List<MyincomelistItem> response;

        PayoutReport_adapter(Context context, List<MyincomelistItem> list) {
            this.response = list;
        }

        @NonNull
        @Override
        public PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_report_adapter, parent, false);
            return new PaidHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PaidHolder holder, int position) {
            holder.payout_no.setText(response.get(position).getPayoutNo());
            holder.display_name.setText(response.get(position).getDisplayName());
            holder.display_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.display_name.setSelected(true);
            holder.display_name.setSingleLine(true);

            holder.gross_amt.setText(response.get(position).getGrossAmount().toString());
            holder.gross_amt.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.gross_amt.setSelected(true);
            holder.gross_amt.setSingleLine(true);

            holder.processing_fee.setText(response.get(position).getProcessingFee().toString());
            holder.processing_fee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.processing_fee.setSelected(true);
            holder.processing_fee.setSingleLine(true);

            holder.net_amt.setText(response.get(position).getNetAmount().toString());
            holder.net_amt.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.net_amt.setSelected(true);
            holder.net_amt.setSingleLine(true);

            holder.tds_amount.setText(response.get(position).getTdsAmount().toString());
            holder.direct_income.setText(response.get(position).getDirectIncome().toString());
            holder.team_performance_bonus.setText(response.get(position).getTeamPerformanceBonus().toString());
            holder.star_club.setText(response.get(position).getStarClub().toString());
            holder.super_star_club.setText(response.get(position).getSuperStarClub().toString());
            holder.bronze_club.setText(response.get(position).getBronzeClub().toString());
            holder.silver_club.setText(response.get(position).getSilverClub().toString());
            holder.gold_club.setText(response.get(position).getGoldClub().toString());
            holder.platinum_club.setText(response.get(position).getPlatinumClub().toString());
            holder.super_platinum_club.setText(response.get(position).getSuperPlatinumClub().toString());
            holder.closing_date.setText(response.get(position).getClosingDate().toString());

            holder.view.setOnClickListener(v -> viewIncome(response.get(position).getPayoutNo().toString()));
        }

        @Override
        public int getItemCount() {
            return response.size();
        }

        @Override
        public Filter getFilter() {
            if (userFilter == null)
                userFilter = new UserFilter(this, payoutReportResponse_list);
            return userFilter;
        }

        public class PaidHolder extends RecyclerView.ViewHolder {
            TextView payout_no, display_name, gross_amt, net_amt, processing_fee, tds_amount, direct_income, team_performance_bonus, star_club, super_star_club, bronze_club, silver_club,
                    gold_club, platinum_club, super_platinum_club, closing_date;
            TextView view_more, view;
            LinearLayout detail_lo;

            public PaidHolder(View itemView) {
                super(itemView);
                view_more = itemView.findViewById(R.id.view_more);
                view = itemView.findViewById(R.id.view);
                detail_lo = itemView.findViewById(R.id.detail_lo);
                payout_no = itemView.findViewById(R.id.payout_no);
                display_name = itemView.findViewById(R.id.display_name);
                gross_amt = itemView.findViewById(R.id.gross_amt);
                net_amt = itemView.findViewById(R.id.net_amt);
                processing_fee = itemView.findViewById(R.id.processing_fee);
                tds_amount = itemView.findViewById(R.id.tds_amount);
                direct_income = itemView.findViewById(R.id.direct_income);
                team_performance_bonus = itemView.findViewById(R.id.team_performance_bonus);
                star_club = itemView.findViewById(R.id.star_club);
                super_star_club = itemView.findViewById(R.id.super_star_club);
                bronze_club = itemView.findViewById(R.id.bronze_club);
                silver_club = itemView.findViewById(R.id.silver_club);
                gold_club = itemView.findViewById(R.id.gold_club);
                platinum_club = itemView.findViewById(R.id.platinum_club);
                super_platinum_club = itemView.findViewById(R.id.super_platinum_club);
                closing_date = itemView.findViewById(R.id.closing_date);

                view_more.setOnClickListener(v -> {
                    if (detail_lo.getVisibility() == View.VISIBLE) {
                        detail_lo.setVisibility(View.GONE);
                        view_more.setText("VIEW MORE");
                    } else {
                        detail_lo.setVisibility(View.VISIBLE);
                        view_more.setText("COLLAPSE");
                    }
                });

            }
        }

    }

    public class ViewIncome_adapter extends RecyclerView.Adapter<ViewIncome_adapter.SupportListViewHolder> {
        private final Context context;
        private final List<PayoutdetailslistItem> items;

        public ViewIncome_adapter(Context context, List<PayoutdetailslistItem> items) {
            this.items = items;
            this.context = context;
        }

        @NonNull
        @Override
        public SupportListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(context).inflate(R.layout.view_income_adapter, parent, false);
            return new SupportListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final SupportListViewHolder holder, int position) {
            holder.date.setText(items.get(position).getDate());
            holder.date.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.date.setSelected(true);
            holder.date.setSingleLine(true);

            holder.login_id.setText(items.get(position).getLoginId());
            holder.login_id.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.login_id.setSelected(true);
            holder.login_id.setSingleLine(true);

            holder.name.setText(items.get(position).getName());
            holder.name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.name.setSelected(true);
            holder.name.setSingleLine(true);

            holder.income_type.setText(items.get(position).getIncomeType());
            holder.income_type.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.income_type.setSelected(true);
            holder.income_type.setSingleLine(true);

            holder.business_amt.setText(items.get(position).getBusinessAmount());
            holder.business_amt.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.business_amt.setSelected(true);
            holder.business_amt.setSingleLine(true);

            holder.comission_percent.setText(items.get(position).getCommission());
            holder.comission_percent.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.comission_percent.setSelected(true);
            holder.comission_percent.setSingleLine(true);

            holder.amount.setText(items.get(position).getAmount());
            holder.amount.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.amount.setSelected(true);
            holder.amount.setSingleLine(true);

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class SupportListViewHolder extends RecyclerView.ViewHolder {
            TextView date, login_id, name, income_type, business_amt, comission_percent, amount;

            public SupportListViewHolder(View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.date);
                login_id = (TextView) itemView.findViewById(R.id.login_id);
                name = (TextView) itemView.findViewById(R.id.name);
                income_type = (TextView) itemView.findViewById(R.id.income_type);
                business_amt = (TextView) itemView.findViewById(R.id.business_amt);
                comission_percent = (TextView) itemView.findViewById(R.id.comission_percent);
                amount = (TextView) itemView.findViewById(R.id.amount);
            }
        }
    }

}
