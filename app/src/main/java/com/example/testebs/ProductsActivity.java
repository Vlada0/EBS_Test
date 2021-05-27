package com.example.testebs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.testebs.Adapter.ProductsAdapter;
import com.example.testebs.Extras.ConstantData;
import com.example.testebs.Extras.SingletonQueue;
import com.example.testebs.Model.Product;
import com.example.testebs.Presenter.ProductsActivityPresenter;
import com.example.testebs.View.ProductsActivityView;
import java.util.ArrayList;
import java.util.Objects;

public class ProductsActivity extends AppCompatActivity implements ProductsActivityView {
    private ArrayList<Product> products;
    private RecyclerView productsRecyclerView;
    private ProductsActivityPresenter productsActivityPresenter;
    private LinearLayoutManager layoutManager;
    private ProductsAdapter productsAdapter;
    private Integer offset = 0;
    private final Integer limit = 10;
    private boolean isLoading = false;
    String productsURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        productsURL = ConstantData.productsURL + "?offset=" + offset + "&limit=" + limit;
        productsActivityPresenter = new ProductsActivityPresenter(this);

        productsRecyclerView = findViewById(R.id.rec_view);
        products = new ArrayList<>();

        getProducts(productsURL, offset);

        productsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && totalItemCount % limit == 0 && firstVisibleItemPosition >= 0) {
                        isLoading = true;
                        offset += 10;
                        productsURL = ConstantData.productsURL + "?offset=" + offset + "&limit=" + limit;

                        getProducts(productsURL, offset);
                    }
                }
            }
        });
    }

    private void getProducts(String productsURL, Integer offset) {
        JsonArrayRequest mJsonObjectRequest = new JsonArrayRequest(Request.Method.GET, productsURL, response -> {
            if (response.length() >= 1) {
                productsActivityPresenter.setData(response, offset);

            }
        }, error -> Log.e("Error", Objects.requireNonNull(error.getMessage())));
        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingletonQueue.getInstance(ProductsActivity.this).addToRequestQueue(mJsonObjectRequest);

    }

    @Override
    public void updateData(ArrayList<Product> productsList) {
        products = productsList;

        if (offset == 0) {
            productsAdapter = new ProductsAdapter(ProductsActivity.this, products);
            productsRecyclerView.setAdapter(productsAdapter);
            layoutManager = new GridLayoutManager(getApplicationContext(), 2);
            productsRecyclerView.setLayoutManager(layoutManager);
        } else {
            if (productsAdapter != null) {
                productsAdapter.notifyDataSetChanged();
            }
            isLoading = false;
        }
        if (productsAdapter != null) {
            productsAdapter.setOnItemClickListener(position -> {
                Intent intent = new Intent(ProductsActivity.this, ProductInfoActivity.class);
                intent.putExtra("productId", products.get(position).getId());
                startActivity(intent);
            });
        }
    }
}