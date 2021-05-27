package com.example.testebs.Presenter;

import android.util.Log;

import com.example.testebs.Model.Product;
import com.example.testebs.View.ProductsActivityView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ProductsActivityPresenter {

    private ArrayList<Product> products;
    private ProductsActivityView productsActivityView;

    public ProductsActivityPresenter(ProductsActivityView productsActivityView) {
        this.productsActivityView = productsActivityView;
        products = new ArrayList<>();
    }

    public void setData(JSONArray response, int offset) {
        try {
            if (response.length() >= 1) {
                if (offset == 0) {
                    products.clear();
                }

                for (int i = 0; i < response.length(); i++) {
                    JSONObject productsJsonObject = response.getJSONObject(i);
                    Product product = new Product(productsJsonObject);
                    products.add(product);
                }
                productsActivityView.updateData(products);
            }
        } catch (JSONException e) {
            Log.e("JSONException", Objects.requireNonNull(e.getMessage()));
        }
    }
}
