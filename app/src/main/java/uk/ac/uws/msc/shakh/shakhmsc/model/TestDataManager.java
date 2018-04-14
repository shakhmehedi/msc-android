package uk.ac.uws.msc.shakh.shakhmsc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shakh on 14/04/2018.
 */

public class TestDataManager {
    private static TestDataManager mDataManager = null;

    protected List<Category> mCategories = new ArrayList<>();
    protected List<Product> mProducts = new ArrayList<>();

    private TestDataManager(){};

    public static TestDataManager getInstance() {
        if (mDataManager == null) {
            mDataManager = new TestDataManager();
            mDataManager.init();
        }
        return mDataManager;
    }

    private void init() {
        //Add Categories

        mCategories.add(new Category(1, "Shirt"));
        mCategories.add(new Category(2, "T-Shirt"));
        mCategories.add(new Category(3, "Trouser"));
        mCategories.add(new Category(4, "Watch"));
        mCategories.add(new Category(5, "Jewellery"));

        int pid = 0;
        for (Category category : mCategories
                ) {

            for (int i = 0; i < 10; i++) {
                pid = pid + i;
                Product product = new Product(pid, category.getName() + i, "SKU-" + pid, category.getId(), 10.5 + pid);
                mProducts.add(product);
                category.getProducts().add(product);
            }

        }
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public List<Product> getProducts() {
        return mProducts;
    }
}
