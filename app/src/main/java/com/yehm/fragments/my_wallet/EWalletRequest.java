package com.yehm.fragments.my_wallet;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.app.PreferencesManager;
import com.yehm.constants.BaseFragment;
import com.yehm.model.EWalletRequestlistItem;
import com.yehm.model.EwalletRequestResponse;
import com.yehm.model.EwalletRequestSubmitResponse;
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

public class EWalletRequest extends BaseFragment {
    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.ewallet_request_list)
    RecyclerView ewalletRequestList;
    @BindView(R.id.goto_top)
    Button gotoTop;
    @BindView(R.id.ewallet_request)
    TextView ewallet_request;
    Unbinder unbinder;
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    List<EWalletRequestlistItem> ewalletRequest_arr;
    LinearLayoutManager linearLayoutManager;
    EWalletRequestAdapter ewallet_request_adapter;
    Dialog transfer_to_other_dialog;
    TextInputEditText et_amount, et_payment_mode, et_transaction_no, et_bank_name, et_bank_acco_no, et_branch;
    TextInputLayout input_layout_amount, input_layout_payment_mode, input_layout_transaction_no, input_layout_bank_name, input_layout_bank_acco_no, input_layout_branch;
    String amount_st = "", payment_mode_st = "", transaction_no_st = "", bank_name_st = "", bank_acco_no_st = "", branch_st = "";
    private UserFilter userFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ewallet_request, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getEwalletRequestList();
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
                ewallet_request_adapter.getFilter().filter(s);
            }
        });

        ewalletRequestList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


    private void getEwalletRequestList() {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("Fk_MemId", PreferencesManager.getInstance(context).getMEMBERID());
            Call<EwalletRequestResponse> eWalletRequestCall = apiServices.getEwalletReqstLst(param);
            eWalletRequestCall.enqueue(new Callback<EwalletRequestResponse>() {
                @Override
                public void onResponse(@NonNull Call<EwalletRequestResponse> call, @NonNull Response<EwalletRequestResponse> response) {
                    hideLoading();
//                    Log.e( ">>>>>", response.toString() );
                    LoggerUtil.logItem(response.body());
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        ewalletRequest_arr = response.body().getEWalletRequestlist();
                        ewalletRequestList.setVisibility(View.VISIBLE);
                        linearLayoutManager = new LinearLayoutManager(context);
                        ewalletRequestList.setLayoutManager(linearLayoutManager);
                        ewalletRequestList.setItemAnimator(new DefaultItemAnimator());
                        ewallet_request_adapter = new EWalletRequestAdapter(context, ewalletRequest_arr);
                        ewalletRequestList.setAdapter(ewallet_request_adapter);
                    } else {
                        searchEt.setVisibility(View.GONE);
                        noRecFound.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EwalletRequestResponse> call, @NonNull Throwable t) {
                    hideLoading();
                }
            });
        } catch (Exception e) {
            Log.e("", "");
            e.printStackTrace();
        }
    }


    @OnClick({R.id.goto_top, R.id.ewallet_request})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.goto_top:
                ewalletRequestList.smoothScrollToPosition(0);
                break;
            case R.id.ewallet_request:
                ewalletRequestDialog();
                break;
        }
    }

    private void ewalletRequestDialog() {
        try {
            hideKeyboard();
            transfer_to_other_dialog = new Dialog(context);
            transfer_to_other_dialog.setCanceledOnTouchOutside(false);
            transfer_to_other_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            transfer_to_other_dialog.setContentView(R.layout.ewallet_request_dialog);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
            transfer_to_other_dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            transfer_to_other_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            ImageView cancel = transfer_to_other_dialog.findViewById(R.id.img_cancel);
            TextView submit = transfer_to_other_dialog.findViewById(R.id.submit);
            TextView reset = transfer_to_other_dialog.findViewById(R.id.reset);

            et_amount = transfer_to_other_dialog.findViewById(R.id.et_amount);
            et_payment_mode = transfer_to_other_dialog.findViewById(R.id.et_payment_mode);
            et_transaction_no = transfer_to_other_dialog.findViewById(R.id.et_transaction_no);
            et_bank_name = transfer_to_other_dialog.findViewById(R.id.et_bank_name);
            et_bank_acco_no = transfer_to_other_dialog.findViewById(R.id.et_bank_acco_no);
            et_branch = transfer_to_other_dialog.findViewById(R.id.et_branch);

            input_layout_amount = transfer_to_other_dialog.findViewById(R.id.input_layout_amount);
            input_layout_payment_mode = transfer_to_other_dialog.findViewById(R.id.input_layout_payment_mode);
            input_layout_bank_name = transfer_to_other_dialog.findViewById(R.id.input_layout_bank_name);
            input_layout_transaction_no = transfer_to_other_dialog.findViewById(R.id.input_layout_transaction_no);
            input_layout_bank_acco_no = transfer_to_other_dialog.findViewById(R.id.input_layout_bank_acco_no);
            input_layout_branch = transfer_to_other_dialog.findViewById(R.id.input_layout_branch);

            cancel.setOnClickListener(v -> transfer_to_other_dialog.dismiss());

            et_payment_mode.setOnClickListener(view -> {
                PopupMenu popuppm = new PopupMenu(context, et_payment_mode);
                popuppm.getMenuInflater().inflate(R.menu.menu_payment_mode, popuppm.getMenu());
                popuppm.setOnMenuItemClickListener(item -> {
                    try {
                        et_payment_mode.setText(item.getTitle());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                });
                popuppm.show();
            });

            submit.setOnClickListener(view -> {
                if (eWalletRequestValidation()) {
                    getEwalletRqst();
                }
            });
            reset.setOnClickListener(view -> {
                et_amount.setText("");
                et_payment_mode.setText("");
                et_transaction_no.setText("");
                et_bank_name.setText("");
                et_bank_acco_no.setText("");
                et_branch.setText("");
            });

            transfer_to_other_dialog.show();
        } catch (Exception e) {
            Log.e("", "");
            e.printStackTrace();
        }

    }

    private void getEwalletRqst() {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("LoginId", PreferencesManager.getInstance(context).getUSERID());
            param.addProperty("RequestedAmount", amount_st);
            param.addProperty("PaymentMode", payment_mode_st);
            param.addProperty("TransactionNo", transaction_no_st);
            param.addProperty("BankName", bank_name_st);
            param.addProperty("AccountNo", bank_acco_no_st);
            param.addProperty("Branch", branch_st);
            param.addProperty("SlipUpload", "");
            Call<EwalletRequestSubmitResponse> ewalletRequestCall = apiServices.getEwalletRequestSubmit(param);
            ewalletRequestCall.enqueue(new Callback<EwalletRequestSubmitResponse>() {
                @Override
                public void onResponse(@NonNull Call<EwalletRequestSubmitResponse> call, @NonNull Response<EwalletRequestSubmitResponse> response) {
                    LoggerUtil.logItem(response.body());
                    hideLoading();
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        transfer_to_other_dialog.dismiss();
                        Toast.makeText(context, "Request Submitted Successfully.", Toast.LENGTH_SHORT).show();
                        getEwalletRequestList();
                    } else {
                        showAlert(response.body().getResponse(), R.color.red, R.drawable.error);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<EwalletRequestSubmitResponse> call, @NonNull Throwable t) {
                    hideLoading();
                }
            });
        } catch (Exception e) {
            Log.e("", "");
            e.printStackTrace();
        }
    }

    private boolean eWalletRequestValidation() {
        try {
            amount_st = et_amount.getText().toString().trim();
            payment_mode_st = et_payment_mode.getText().toString().trim();
            transaction_no_st = et_transaction_no.getText().toString().trim();
            bank_name_st = et_bank_name.getText().toString().trim();
            bank_acco_no_st = et_bank_acco_no.getText().toString().trim();
            branch_st = et_branch.getText().toString().trim();

            if (amount_st.length() == 0) {
                et_amount.setError("Please Enter Amount");
                requestFocus(et_amount);
                return false;
            } else {
                input_layout_amount.setErrorEnabled(false);
            }
            if (payment_mode_st.length() == 0) {
                showAlert("Please select Payment Mode", R.color.red, R.drawable.error);
                return false;
            }
            if (transaction_no_st.length() == 0) {
                et_transaction_no.setError("Please Enter Transaction No.");
                requestFocus(et_transaction_no);
                return false;
            } else {
                input_layout_transaction_no.setErrorEnabled(false);
            }
            if (bank_name_st.length() == 0) {
                et_bank_name.setError("Please Enter Bank Name");
                requestFocus(et_bank_name);
                return false;
            } else {
                input_layout_bank_name.setErrorEnabled(false);
            }
            if (bank_acco_no_st.length() == 0) {
                et_bank_acco_no.setError("Please Enter Bank Account Number");
                requestFocus(et_bank_acco_no);
                return false;
            } else {
                input_layout_bank_acco_no.setErrorEnabled(false);
            }
            if (branch_st.length() == 0) {
                et_branch.setError("Please Enter Bank Branch");
                requestFocus(et_branch);
                return false;
            } else {
                input_layout_branch.setErrorEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private static class UserFilter extends Filter {
        EWalletRequestAdapter adapter;
        List<EWalletRequestlistItem> myDirectResponses;
        List<EWalletRequestlistItem> filteredList;

        private UserFilter(EWalletRequestAdapter adapter, List<EWalletRequestlistItem> originalList) {
            super();
            this.adapter = adapter;
            this.myDirectResponses = new LinkedList<EWalletRequestlistItem>(originalList);
            this.filteredList = new ArrayList<EWalletRequestlistItem>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(myDirectResponses);
            } else {
                final String filterPattern = constraint.toString().trim();
                for (final EWalletRequestlistItem user : myDirectResponses) {
                    if (Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getRequestedDate()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getRequestNo()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getRequestedAmount()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getPaymentStatus()).find()) {
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
            adapter.response.addAll((ArrayList<EWalletRequestlistItem>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    public class EWalletRequestAdapter extends RecyclerView.Adapter<EWalletRequestAdapter.PaidHolder> implements Filterable {
        View view;
        List<EWalletRequestlistItem> response;

        public EWalletRequestAdapter(Context context, List<EWalletRequestlistItem> list) {
            this.response = list;
        }

        @NonNull
        @Override
        public PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.e_wallet_request_adapter, parent, false);
            PaidHolder paidHolder = new PaidHolder(view);
            return paidHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PaidHolder holder, int position) {
            try {
                holder.req_date.setText(response.get(position).getRequestedDate());
                holder.req_date.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                holder.req_date.setSelected(true);
                holder.req_date.setSingleLine(true);

                holder.amount.setText(response.get(position).getRequestedAmount());

                holder.request_no.setText(response.get(position).getRequestNo());
                holder.approval_date.setText(response.get(position).getApprovalDate().substring(0, 10));
                holder.status.setText(response.get(position).getPaymentStatus());
                holder.payment_mode.setText(response.get(position).getPaymentMode());

                String sta = response.get(position).getPaymentStatus();
                if (sta.equalsIgnoreCase("Approved")) {
                    holder.status_lo.setBackground(getResources().getDrawable(R.drawable.walletrequest_approve));
                } else if (sta.equalsIgnoreCase("Pending")) {
                    holder.status_lo.setBackground(getResources().getDrawable(R.drawable.walletrequest_pending));
                } else if (sta.equalsIgnoreCase("Decline")) {
                    holder.status_lo.setBackground(getResources().getDrawable(R.drawable.walletrequest_decline));
                }
            } catch (Exception e) {
                Log.e("", "");
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
                userFilter = new UserFilter(this, ewalletRequest_arr);
            return userFilter;
        }

        public class PaidHolder extends RecyclerView.ViewHolder {
            TextView req_date, amount, request_no, slip, approval_date, status, payment_mode;
            LinearLayout status_lo;

            public PaidHolder(View itemView) {
                super(itemView);
                req_date = itemView.findViewById(R.id.req_date);
                amount = itemView.findViewById(R.id.amount);
                request_no = itemView.findViewById(R.id.request_no);
//                slip = itemView.findViewById( R.id.slip );
                approval_date = itemView.findViewById(R.id.approval_date);
                status = itemView.findViewById(R.id.status);
                payment_mode = itemView.findViewById(R.id.payment_mode);
                status_lo = itemView.findViewById(R.id.status_lo);
            }
        }
    }
}