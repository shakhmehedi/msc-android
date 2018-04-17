package uk.ac.uws.msc.shakh.util;

import com.alibaba.fastjson.JSON;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.CategoryProduct;
import com.github.chen0040.magento.models.Product;
import com.github.chen0040.magento.models.ProductPage;
import com.github.chen0040.magento.services.MagentoCategoryManager;

import java.util.ArrayList;
import java.util.List;

public class ExtendedMagentoCategoryManager extends MagentoCategoryManager {


    private MagentoClient mClient;

    public ExtendedMagentoCategoryManager(MagentoClient client) {
        super(client);
        mClient = client;
    }

    public List<Product> getProductsWithDetailByCategoryId(long categryId) {
        List<CategoryProduct> categoryProducts = getProductsInCategory(categryId);
        String skus = "";

        for (CategoryProduct categoryProduct :
                categoryProducts) {
            if (skus.equals("")) {
                skus += categoryProduct.getSku();
            } else {
                skus += categoryProduct.getSku() + ",";
            }
        }

        List<Product> products = new ArrayList<>();

        if (skus.length() > 0) {
            ExtendedMagentoProductManager productManager = new ExtendedMagentoProductManager(mClient);
            String jsonData = productManager.page("sku", skus, "in");

            ProductPage productPage = new ProductPage();
            if (!jsonData.isEmpty()) {
                productPage = JSON.parseObject(jsonData, ProductPage.class);
            }

            products = productPage.getItems();
        }

        return products;
    }
}
