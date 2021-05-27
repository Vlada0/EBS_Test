package com.example.testebs;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testebs.Extras.ConstantData;
import com.example.testebs.Extras.SingletonQueue;
import com.example.testebs.Model.Product;
import com.example.testebs.Presenter.ProductInfoActivityPresenter;
import com.example.testebs.View.ProductInfoActivityView;
import java.util.Objects;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ProductInfoActivity extends AppCompatActivity implements ProductInfoActivityView {

    private Product product;
    private ProductInfoActivityPresenter productInfoActivityPresenter;
    String productURL;
    Integer productId;
    TextView title, shortDescription, price, salePercent, sale, details;
    ImageView imageProduct;
    String currency = "$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        title = findViewById(R.id.title);
        shortDescription = findViewById(R.id.shortDescription);
        price = findViewById(R.id.price);
        salePercent = findViewById(R.id.sale_percent);
        sale = findViewById(R.id.sale);
        details = findViewById(R.id.details);
        imageProduct = findViewById(R.id.image);

        productInfoActivityPresenter = new ProductInfoActivityPresenter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productId = extras.getInt("productId");
            productURL = ConstantData.productURL + "?id=" + productId;
            getProduct(productURL);
        } else {
            Toast.makeText(ProductInfoActivity.this, "Error while getting product id", Toast.LENGTH_SHORT).show();
        }

    }

    private void getProduct(String productURL) {
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, productURL, response -> {
            if (response.length() >= 1) {
                productInfoActivityPresenter.setData(response);

            }
        }, error -> Log.e("Error", Objects.requireNonNull(error.getMessage())));
        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingletonQueue.getInstance(ProductInfoActivity.this).addToRequestQueue(mJsonObjectRequest);

    }

    @Override
    public void updateData(Product productElement) {
        product = productElement;

        Glide.with(this)
                .load(product.getImage())
                .apply(
                        new RequestOptions()
                                .placeholder(R.drawable.image_not_found)
                                .error(R.drawable.image_not_found)
                                .centerCrop()
                )
                .transition(withCrossFade())
                .into(imageProduct);

        if (product.getTitle() != null) {
            title.setText(product.getTitle());
        }
        if (!TextUtils.isEmpty(product.getShort_description())) {
            String cropDesc = product.getShort_description().substring(0, 1).toUpperCase() + product.getShort_description().substring(1);
            shortDescription.setText(cropDesc);
        }
        if (product.getPrice() != null) {
            price.setText(product.getPrice().toString() + currency);
        }
        if (product.getDetails() != null) {
            details.setText(product.getDetails());
        }
        if (product.getSale_precent() != null) {
            if (product.getSale_precent() == 0) {
                salePercent.setVisibility(View.GONE);
                price.setPaintFlags(price.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                price.setTextColor(Color.parseColor("#198ff2"));
                price.setTextSize(24);
                sale.setVisibility(View.GONE);
            } else {
                salePercent.setVisibility(View.VISIBLE);
                price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                price.setTextColor(Color.parseColor("#c7c7c7"));
                price.setTextSize(13);
                sale.setVisibility(View.VISIBLE);
                sale.setText("Sale " + product.getSale_precent() + "%");
                sale.setTextSize(14);
            }
            int salePrice = (100 - product.getSale_precent()) * product.getPrice() / 100;
            salePercent.setText(salePrice + currency);
        }

    }

    public void backToProductList(View view) {
        finish();
    }
}