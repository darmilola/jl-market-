package com.electonicmarket.android.emarket.interfaces;

import com.electonicmarket.android.emarket.Models.ProductCategory;
import com.electonicmarket.android.emarket.Models.ProductModel;

import java.util.List;

public interface InteractorListener {
    void onProductsLoaded(int id, List<ProductModel> products);
    void onCategoriesLoaded(List<ProductCategory> categories);
}
