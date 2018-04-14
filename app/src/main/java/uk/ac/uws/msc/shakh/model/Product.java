package uk.ac.uws.msc.shakh.model;

/**
 * Created by shakh on 14/04/2018.
 */

public class Product {
    private int mId;
    private String mName;
    private String mSku;
    private int mCategoryId;
    private double mPrice;

    public Product(){

    }

    public Product(int id, String name, String sku, int categoryId, double price){

        mId = id;
        mName = name;
        mSku = sku;
        mCategoryId = categoryId;
        mPrice = price;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        mCategoryId = categoryId;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public String getSku() {
        return mSku;
    }

    public void setSku(String sku) {
        mSku = sku;
    }
}
