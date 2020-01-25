package com.electonicmarket.android.emarket.interfaces;

import com.electonicmarket.android.emarket.Models.ProductCategory;
import com.electonicmarket.android.emarket.Models.ProductModel;

import java.util.List;

public interface ViewLayer {

    void showProductsInCategory(int categoryid, List<ProductModel> productList, PresenterLayer presenter );
    void showCategories(List<ProductCategory> categoryItemList, PresenterLayer presenter);
}
