package uk.ac.uws.msc.shakh.shakhmsc.model;

import java.util.List;

/**
 * Created by shakh on 14/04/2018.
 */

public class Category {
    private int mId;
    private String mName;

    private List<Product> mProducts;

    public Category(){

    }

    public Category(int id, String name){

        mId = id;
        mName = name;
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

    public List<Product> getProducts() {
        return mProducts;
    }
}
