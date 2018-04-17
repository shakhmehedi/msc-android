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

        List<String> skuPages = new ArrayList<>();

        int currentIndex = 0;
        while (currentIndex < categoryProducts.size() - 1) {
            int maxCount = 1500;
            String skus = "";

            while (skus.length() < maxCount && currentIndex < categoryProducts.size() - 1) {
                skus += categoryProducts.get(currentIndex).getSku() + ",";
                currentIndex++;
            }

            skuPages.add(skus);
            skus = "";

        }

        List<Product> products = new ArrayList<>();

        if (skuPages.size() > 0) {
            ExtendedMagentoProductManager productManager = new ExtendedMagentoProductManager(mClient);

            for (String skuString :
                    skuPages) {
                String jsonData = "";
                jsonData = productManager.page("sku", skuString, "in");

                ProductPage productPage = new ProductPage();
                if (jsonData.length() > 0) {
                    productPage = JSON.parseObject(jsonData, ProductPage.class);
                }

                products.addAll(productPage.getItems());
            }

        }

        return products;
    }
}
