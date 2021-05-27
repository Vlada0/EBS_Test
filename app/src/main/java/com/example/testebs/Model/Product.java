package com.example.testebs.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
    private Integer id;
    private String title;
    private String short_description;
    private String image;
    private Integer price;
    private Integer sale_precent;
    private String details;

    public Product() {
    }

    public Product(JSONObject productJsonObject) throws JSONException {
        if (productJsonObject.has("id")) {
            if (!(productJsonObject.isNull("id"))) {
                this.id = productJsonObject.getInt("id");
            }
        }
        if (productJsonObject.has("title")) {
            if (!(productJsonObject.isNull("title"))) {
                this.title = productJsonObject.getString("title");
            }
        }
        if (productJsonObject.has("short_description")) {
            if (!(productJsonObject.isNull("short_description"))) {
                this.short_description = productJsonObject.getString("short_description");
            }
        }
        if (productJsonObject.has("image")) {
            if (!(productJsonObject.isNull("image"))) {
                this.image = productJsonObject.getString("image");
            }
        }
        if (productJsonObject.has("price")) {
            if (!(productJsonObject.isNull("price"))) {
                this.price = productJsonObject.getInt("price");
            }
        }
        if (productJsonObject.has("sale_precent")) {
            if (!(productJsonObject.isNull("sale_precent"))) {
                this.sale_precent = productJsonObject.getInt("sale_precent");
            }
        }
        if (productJsonObject.has("details")) {
            if (!(productJsonObject.isNull("details"))) {
                this.details = productJsonObject.getString("details");
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSale_precent() {
        return sale_precent;
    }

    public void setSale_precent(Integer sale_precent) {
        this.sale_precent = sale_precent;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
