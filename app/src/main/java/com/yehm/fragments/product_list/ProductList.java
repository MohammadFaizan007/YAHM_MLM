package com.yehm.fragments.product_list;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.yehm.R;
import com.yehm.constants.BaseFragment;
import com.yehm.constants.ItemOffsetDecoration;
import com.yehm.model.ProductlistItem;
import com.yehm.model.ResponseProductList;
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

public class ProductList extends BaseFragment {
    @BindView(R.id.noRecFound)
    RelativeLayout noRecFound;
    Unbinder unbinder;
    View view;
    RecyclerView productList;
    List<ProductlistItem> product_list;
    EditText search_et;
    ProductList_adapter product_list_adapter;
    private UserFilter userFilter;
    private Button goto_top;

    public ProductList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_list, container, false);
        productList = view.findViewById(R.id.productList);
        search_et = view.findViewById(R.id.search_et);
        goto_top = view.findViewById(R.id.goto_top);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreatedStuff(View view, @Nullable Bundle savedInstanceState) {
        if (NetworkUtils.getConnectivityStatus(context) != 0) {
            getProductLsit();
        } else {
            createInfoDialog(context, "Alert", getString(R.string.alert_internet));
        }
        productList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            productList.smoothScrollToPosition(0);
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
                product_list_adapter.getFilter().filter(s);
            }
        });
    }

    private void getProductLsit() {
        try {
            showLoading();
            JsonObject param = new JsonObject();
            param.addProperty("PK_ProductId", "");
            Call<ResponseProductList> incomeCall = apiServices.getProductList(param);
            incomeCall.enqueue(new Callback<ResponseProductList>() {
                @Override
                public void onResponse(@NonNull Call<ResponseProductList> call, @NonNull Response<ResponseProductList> response) {
                    hideLoading();
                    LoggerUtil.logItem(response.body());
                    if (response.body().getResponse().equalsIgnoreCase("Success")) {
                        product_list = response.body().getProductlist();
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
                        productList.addItemDecoration(itemDecoration);
                        productList.setHasFixedSize(true);
                        productList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        productList.setItemAnimator(new DefaultItemAnimator());
                        product_list_adapter = new ProductList_adapter(context, product_list);
                        productList.setAdapter(product_list_adapter);
                    } else {
                        search_et.setVisibility(View.GONE);
                        noRecFound.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseProductList> call, @NonNull Throwable t) {
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
        ProductList_adapter adapter;
        List<ProductlistItem> myDirectResponses;
        List<ProductlistItem> filteredList;

        private UserFilter(ProductList_adapter adapter, List<ProductlistItem> originalList) {
            super();
            this.adapter = adapter;
            this.myDirectResponses = new LinkedList<ProductlistItem>(originalList);
            this.filteredList = new ArrayList<ProductlistItem>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(myDirectResponses);
            } else {
                final String filterPattern = constraint.toString().trim();
                for (final ProductlistItem user : myDirectResponses) {
                    if (Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getMrp()).find() ||
                            Pattern.compile(Pattern.quote(filterPattern), Pattern.CASE_INSENSITIVE).matcher(user.getBv()).find()) {
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
            adapter.response.addAll((ArrayList<ProductlistItem>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    public class ProductList_adapter extends RecyclerView.Adapter<ProductList_adapter.PaidHolder> implements Filterable {
        View view;
        List<ProductlistItem> response;

        ProductList_adapter(Context context, List<ProductlistItem> list) {
            this.response = list;
        }

        @NonNull
        @Override
        public PaidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_adapter, parent, false);
            return new PaidHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PaidHolder holder, int position) {
            try {
                String string_mrp = "", string_bv = "";
                int spacePos = (response.get(position).getMrp()).indexOf(".");
                int spacePosBv = (response.get(position).getBv()).indexOf(".");
                if (spacePos > 0) {
                    string_mrp = (response.get(position).getMrp()).substring(0, spacePos);
                    holder.productMrp.setText("MRP - " + string_mrp);
                }
                if (spacePosBv > 0) {
                    string_bv = (response.get(position).getBv()).substring(0, spacePosBv);
                    holder.productBv.setText("BV " + string_bv);
                }

                Glide.with(context).load(response.get(position).getImage())
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.ic_launcher_foreground)
                                .error(R.mipmap.ic_launcher_foreground))
                        .into(holder.productImage);

                holder.product_lo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
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
            if (userFilter == null)
                userFilter = new UserFilter(this, product_list);
            return userFilter;
        }

        public class PaidHolder extends RecyclerView.ViewHolder {
            TextView productMrp, productBv;
            ImageView productImage;
            LinearLayout product_lo;

            public PaidHolder(View itemView) {
                super(itemView);
                productMrp = itemView.findViewById(R.id.productMrp);
                productBv = itemView.findViewById(R.id.productBv);
                productImage = itemView.findViewById(R.id.productImage);
                product_lo = itemView.findViewById(R.id.product_lo);
            }
        }
    }

}


