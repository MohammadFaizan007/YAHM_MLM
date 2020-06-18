package com.yehm.fragments.my_income;


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
import com.yehm.model.MyIncomeResponse;
import com.yehm.model.MyincomelistItem;
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

public class AllIncomeNew extends BaseFragment {
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    Unbinder unbinder;
    View view;
    RecyclerView my_income_list;
    LinearLayoutManager linearLayoutManager;
    List<MyincomelistItem> binary_income_list;
    EditText search_et;
    Binary_Income_adapter binary_income_adapter;
    private UserFilter userFilter;
    private Button goto_top;

    public AllIncomeNew() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_income, container, false);
        my_income_list = view.findViewById(R.id.my_direct_list);
        search_et = view.findViewById(R.id.search_et);
        goto_top = view.findViewById(R.id.goto_top);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getIncome();
        } else {
            createInfoDialog(context, "Alert", getString(R.string.alert_internet));
        }
        my_income_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            my_income_list.smoothScrollToPosition(0);
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
                binary_income_adapter.getFilter().filter(s);
            }
        });
    }

    private void getIncome() {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("FK_MemID", PreferencesManager.getInstance(context).getMEMBERID());
            param.addProperty("PayoutNo", "");
            Call<MyIncomeResponse> incomeCall = apiServices.getIncome(param);
            incomeCall.enqueue(new Callback<MyIncomeResponse>() {
                @Override
                public void onResponse(@NonNull Call<MyIncomeResponse> call, @NonNull Response<MyIncomeResponse> response) {
                    hideLoading();
                    LoggerUtil.logItem(response.body());
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        binary_income_list = response.body().getMyincomelist();
                        linearLayoutManager = new LinearLayoutManager(context);
                        my_income_list.setLayoutManager(linearLayoutManager);
                        my_income_list.setItemAnimator(new DefaultItemAnimator());
                        binary_income_adapter = new Binary_Income_adapter(context, binary_income_list);
                        my_income_list.setAdapter(binary_income_adapter);
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

    private static class UserFilter extends Filter {
        Binary_Income_adapter adapter;
        List<MyincomelistItem> myDirectResponses;
        List<MyincomelistItem> filteredList;

        private UserFilter(Binary_Income_adapter adapter, List<MyincomelistItem> originalList) {
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
                    if (Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getPayoutNo()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getClosingDate()).find()) {
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

    public class Binary_Income_adapter extends RecyclerView.Adapter<Binary_Income_adapter.PaidHolder> implements Filterable {
        View view;
        List<MyincomelistItem> response;

        Binary_Income_adapter(Context context, List<MyincomelistItem> list) {
            this.response = list;
        }

        @NonNull
        @Override
        public PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_income_adapter_new, parent, false);
            return new PaidHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PaidHolder holder, int position) {
            holder.closing_date.setText(response.get(position).getClosingDate().toString());
            holder.closing_date.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.closing_date.setSelected(true);
            holder.closing_date.setSingleLine(true);

            holder.payout_no.setText(response.get(position).getPayoutNo().toString());

            holder.direct_income.setText(response.get(position).getDirectIncome().toString());
            holder.team_perf_bonus.setText(response.get(position).getTeamPerformanceBonus().toString());
            holder.star_club.setText(response.get(position).getStarClub().toString());
            holder.super_star_club.setText(response.get(position).getSuperStarClub().toString());
            holder.bronze_club.setText(response.get(position).getBronzeClub().toString());
            holder.silver_club.setText(response.get(position).getSilverClub().toString());
            holder.gold_club.setText(response.get(position).getGoldClub().toString());
            holder.platinum_club.setText(response.get(position).getPlatinumClub().toString());
            holder.super_platinum_club.setText(response.get(position).getSuperPlatinumClub().toString());
        }

        @Override
        public int getItemCount() {
            return response.size();
        }

        @Override
        public Filter getFilter() {
            if (userFilter == null)
                userFilter = new UserFilter(this, binary_income_list);
            return userFilter;
        }

        public class PaidHolder extends RecyclerView.ViewHolder {
            TextView closing_date, payout_no, direct_income, team_perf_bonus, star_club, super_star_club, bronze_club, silver_club, gold_club, platinum_club, super_platinum_club;

            public PaidHolder(View itemView) {
                super(itemView);
                closing_date = itemView.findViewById(R.id.closing_date);
                payout_no = itemView.findViewById(R.id.payout_no);
                direct_income = itemView.findViewById(R.id.direct_income);
                team_perf_bonus = itemView.findViewById(R.id.team_perf_bonus);
                star_club = itemView.findViewById(R.id.star_club);
                super_star_club = itemView.findViewById(R.id.super_star_club);
                bronze_club = itemView.findViewById(R.id.bronze_club);
                silver_club = itemView.findViewById(R.id.silver_club);
                gold_club = itemView.findViewById(R.id.gold_club);
                platinum_club = itemView.findViewById(R.id.platinum_club);
                super_platinum_club = itemView.findViewById(R.id.super_platinum_club);
            }
        }
    }
}


