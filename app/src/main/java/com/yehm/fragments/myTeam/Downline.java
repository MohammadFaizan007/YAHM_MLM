package com.yehm.fragments.myTeam;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.model.DownlineListItem;
import com.yehm.model.DownlineResponse;
import com.yehm.utils.LoggerUtil;
import com.yehm.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Downline extends BaseFragment {
    View view;
    RecyclerView downline_list;
    LinearLayoutManager linearLayoutManager;
    Button goto_top;
    EditText search_et;
    RelativeLayout noRecFound;
    Downline_adapter downline_adapter;
    List<DownlineListItem> downline_arr;
    private UserFilter userFilter;

    public Downline() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.downline, container, false);
        downline_list = view.findViewById(R.id.my_direct_list);
        search_et = view.findViewById(R.id.search_et);
        goto_top = view.findViewById(R.id.goto_top);
        noRecFound = view.findViewById(R.id.noRecFound);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getDownline();
        } else {
            createInfoDialog(context, "Alert", getString(R.string.alert_internet));
        }
        goto_top.setOnClickListener(view1 -> downline_list.smoothScrollToPosition(0));

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                downline_adapter.getFilter().filter(s);
            }
        });

        downline_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void getDownline() {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("LoginId", PreferencesManager.getInstance(context).getUSERID());
            Call<DownlineResponse> downlineCall = apiServices.getDownline(param);
            downlineCall.enqueue(new Callback<DownlineResponse>() {
                @Override
                public void onResponse(@NonNull Call<DownlineResponse> call, @NonNull Response<DownlineResponse> response) {
                    hideLoading();
                    LoggerUtil.logItem(response.body());
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        downline_arr = response.body().getDownlineList();
                        downline_list.setVisibility(View.VISIBLE);
                        linearLayoutManager = new LinearLayoutManager(context);
                        downline_list.setLayoutManager(linearLayoutManager);
                        downline_list.setItemAnimator(new DefaultItemAnimator());
                        downline_adapter = new Downline_adapter(context, downline_arr);
                        downline_list.setAdapter(downline_adapter);
                    } else {
                        noRecFound.setVisibility(View.VISIBLE);
                        search_et.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DownlineResponse> call, @NonNull Throwable t) {
                    hideLoading();
                }
            });
        } catch (Exception e) {
            Log.e("", e.toString());
        }
    }

    private static class UserFilter extends Filter {
        Downline_adapter adapter;
        List<DownlineListItem> myDirectResponses;
        List<DownlineListItem> filteredList;

        private UserFilter(Downline_adapter adapter, List<DownlineListItem> originalList) {
            super();
            this.adapter = adapter;
            this.myDirectResponses = new LinkedList<DownlineListItem>(originalList);
            this.filteredList = new ArrayList<DownlineListItem>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(myDirectResponses);
            } else {
                final String filterPattern = constraint.toString().trim();
                for (final DownlineListItem user : myDirectResponses) {
                    if (Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getMemberId()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getMemberName()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getJoiningDate()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getStatus()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getSponsorId()).find()) {
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
            adapter.response.addAll((ArrayList<DownlineListItem>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    public class Downline_adapter extends RecyclerView.Adapter<Downline_adapter.PaidHolder> implements Filterable {
        View view;
        List<DownlineListItem> response;

        public Downline_adapter(Context context, List<DownlineListItem> list) {
            this.response = list;
        }

        @NonNull
        @Override
        public PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downline_adapter, parent, false);
            PaidHolder paidHolder = new PaidHolder(view);
            return paidHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PaidHolder holder, int position) {
            try {
                if (position % 2 == 0) {
                    holder.left_lo.setVisibility(View.VISIBLE);
                    holder.card_view.setVisibility(View.VISIBLE);
                    holder.card_right.setVisibility(View.GONE);
                    holder.right_lo.setVisibility(View.GONE);

                    holder.member_id.setText(response.get(position).getMemberId());
                    holder.member_id.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.member_id.setSelected(true);
                    holder.member_id.setSingleLine(true);

                    holder.status.setText(response.get(position).getStatus());
                    holder.member_name.setText(response.get(position).getMemberName());
                    holder.member_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.member_name.setSelected(true);
                    holder.member_name.setSingleLine(true);

                    holder.sponsor_id.setText(response.get(position).getSponsorId());
                    holder.sponsor_id.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.sponsor_id.setSelected(true);
                    holder.sponsor_id.setSingleLine(true);

                    holder.sponsor_name.setText(response.get(position).getSponsorName());
                    holder.sponsor_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.sponsor_name.setSelected(true);
                    holder.sponsor_name.setSingleLine(true);

                    holder.joining_date.setText(response.get(position).getJoiningDate().substring(0, 10));
                    holder.joining_date.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.joining_date.setSelected(true);
                    holder.joining_date.setSingleLine(true);

                    holder.bv.setText(response.get(position).getBv());
                    holder.package_name.setText(response.get(position).getPackageName());
                    holder.package_amt.setText(response.get(position).getPackageAmount());

                    String sta = response.get(position).getStatus();
                    if (sta.equalsIgnoreCase("Active")) {
                        holder.status.setTextColor(getResources().getColor(R.color.active));
                    } else if (sta.equalsIgnoreCase("InActive")) {
                        holder.status.setTextColor(getResources().getColor(R.color.inactive));
                    } else {
                        holder.status.setTextColor(getResources().getColor(R.color.blocked));
                    }

                } else {
                    holder.right_lo.setVisibility(View.VISIBLE);
                    holder.card_right.setVisibility(View.VISIBLE);
                    holder.left_lo.setVisibility(View.GONE);
                    holder.card_view.setVisibility(View.GONE);

                    holder.member_id_second.setText(response.get(position).getMemberId());
                    holder.member_id_second.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.member_id_second.setSelected(true);
                    holder.member_id_second.setSingleLine(true);

                    holder.status_second.setText(response.get(position).getStatus());
                    holder.member_name_second.setText(response.get(position).getMemberName());
                    holder.member_name_second.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.member_name_second.setSelected(true);
                    holder.member_name_second.setSingleLine(true);

                    holder.sponsor_id_second.setText(response.get(position).getSponsorId());
                    holder.sponsor_id_second.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.sponsor_id_second.setSelected(true);
                    holder.sponsor_id_second.setSingleLine(true);

                    holder.sponsor_name_second.setText(response.get(position).getSponsorName());
                    holder.sponsor_name_second.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.sponsor_name_second.setSelected(true);
                    holder.sponsor_name_second.setSingleLine(true);

                    holder.joining_date_second.setText(response.get(position).getJoiningDate().substring(0, 10));
                    holder.joining_date_second.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    holder.joining_date_second.setSelected(true);
                    holder.joining_date_second.setSingleLine(true);

                    holder.bv_second.setText(response.get(position).getBv());
                    holder.package_name_second.setText(response.get(position).getPackageName());
                    holder.package_amt_second.setText(response.get(position).getPackageAmount());

                    String sta = response.get(position).getStatus();
                    if (sta.equalsIgnoreCase("Active")) {
                        holder.status_second.setTextColor(getResources().getColor(R.color.active));
                    } else if (sta.equalsIgnoreCase("InActive")) {
                        holder.status_second.setTextColor(getResources().getColor(R.color.inactive));
                    } else {
                        holder.status_second.setTextColor(getResources().getColor(R.color.blocked));
                    }
                }
            } catch (Exception e) {
                Log.e("", e.toString());
            }
        }

        @Override
        public int getItemCount() {
            return response.size();
        }

        @Override
        public Filter getFilter() {
            if (userFilter == null)
                userFilter = new UserFilter(this, downline_arr);
            return userFilter;
        }

        public class PaidHolder extends RecyclerView.ViewHolder {
            TextView member_id, status, member_name, sponsor_id, sponsor_name, joining_date, bv, package_name, package_amt, leg, member_id_second, status_second, member_name_second,
                    sponsor_id_second, sponsor_name_second, joining_date_second, bv_second, package_name_second, package_amt_second, leg_second;
            CardView card_view, card_right;
            LinearLayout left_lo, right_lo;
//            Space space_two;

            public PaidHolder(View itemView) {
                super(itemView);
                member_id = (TextView) itemView.findViewById(R.id.member_id);
                status = (TextView) itemView.findViewById(R.id.status);
                member_name = (TextView) itemView.findViewById(R.id.member_name);
                sponsor_id = (TextView) itemView.findViewById(R.id.sponsor_id);
                sponsor_name = (TextView) itemView.findViewById(R.id.sponsor_name);
                joining_date = (TextView) itemView.findViewById(R.id.joining_date);
                bv = (TextView) itemView.findViewById(R.id.bv);
                package_name = (TextView) itemView.findViewById(R.id.package_name);
                package_amt = (TextView) itemView.findViewById(R.id.package_amt);
                leg = (TextView) itemView.findViewById(R.id.leg);

                member_id_second = (TextView) itemView.findViewById(R.id.member_id_second);
                status_second = (TextView) itemView.findViewById(R.id.status_second);
                member_name_second = (TextView) itemView.findViewById(R.id.member_name_second);
                sponsor_id_second = (TextView) itemView.findViewById(R.id.sponsor_id_second);
                sponsor_name_second = (TextView) itemView.findViewById(R.id.sponsor_name_second);
                joining_date_second = (TextView) itemView.findViewById(R.id.joining_date_second);
                bv_second = (TextView) itemView.findViewById(R.id.bv_second);
                package_name_second = (TextView) itemView.findViewById(R.id.package_name_second);
                package_amt_second = (TextView) itemView.findViewById(R.id.package_amt_second);
                leg_second = (TextView) itemView.findViewById(R.id.leg_second);

                card_view = (CardView) itemView.findViewById(R.id.card_view);
                card_right = (CardView) itemView.findViewById(R.id.card_right);
                left_lo = (LinearLayout) itemView.findViewById(R.id.left_lo);
                right_lo = (LinearLayout) itemView.findViewById(R.id.right_lo);
            }
        }
    }

}