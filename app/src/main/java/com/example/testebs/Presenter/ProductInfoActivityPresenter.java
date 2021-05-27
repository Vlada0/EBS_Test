package com.example.testebs.Presenter;

import android.util.Log;
import com.example.testebs.Model.Product;
import com.example.testebs.View.ProductInfoActivityView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ProductInfoActivityPresenter {
    private Product product;
    private ProductInfoActivityView productInfoActivityView;

    public ProductInfoActivityPresenter(ProductInfoActivityView productInfoActivityView) {
        this.productInfoActivityView = productInfoActivityView;
        product = new Product();
    }

    public void setData(JSONObject response) {
        try {
            if (response.length() >= 1) {
                product = new Product(response);
                productInfoActivityView.updateData(product);
            }
        } catch (JSONException e) {
            Log.e("JSONException", Objects.requireNonNull(e.getMessage()));
        }
    }
}
