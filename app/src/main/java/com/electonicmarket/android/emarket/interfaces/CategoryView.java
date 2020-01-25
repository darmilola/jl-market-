package com.electonicmarket.android.emarket.interfaces;

import com.electonicmarket.android.emarket.Models.ProductModel;

import java.util.List;

public interface CategoryView {
    void setCategoryName(String categoryName);
    void setProductList(List<ProductModel> productList);
}
