package uk.ac.uws.msc.shakh;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.chen0040.magento.models.Product;

import java.util.List;

import uk.ac.uws.msc.shakh.adapter.ProductRecyclerAdapter;
import uk.ac.uws.msc.shakh.shakhmsc.R;

public class ProductListActivity extends AppCompatActivity {
    private static List<Product> mProductList;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mProductsLayoutManager;
    private ProductRecyclerAdapter mProductRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        String query = intent.getStringExtra(MainActivity.SEARCH_QUERY);

        setProductList(MainActivity.getMagentoAdminClient().extendedProducts().search(query));
        TextView textView = (TextView) findViewById(R.id.text_tmp);
        textView.setText("Search: " + query);
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_products);
        mProductsLayoutManager = new LinearLayoutManager(this);

        mProductRecyclerAdapter = new ProductRecyclerAdapter(this, mProductList, query);
        mRecyclerItems.setLayoutManager(mProductsLayoutManager);
        mRecyclerItems.setAdapter(mProductRecyclerAdapter);
    }

    public static List<Product> getProductList() {
        return mProductList;
    }

    public static void setProductList(List<Product> productList) {
        mProductList = productList;
    }

}
