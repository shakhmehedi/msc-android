package uk.ac.uws.msc.shakh;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.github.chen0040.magento.models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.uws.msc.shakh.adapter.ProductRecyclerAdapter;
import uk.ac.uws.msc.shakh.shakhmsc.R;

public class ProductListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String ACTION_TYPE_SEARCH = "action_search";
    public static final String INTENT_ACTION = "intent_action";
    public static final String SEARCH_QUERY = "search_query";
    public static final String SEARCH_ALL_PRODUCTS = "all_products";
    public static final String ACTION_TYPE_CATEGORY = "view_category_product";
    public static final String CATEGORY_ID = "category_id";

    private static List<Product> mProductList = new ArrayList<>();
    private RecyclerView mRecyclerProducts;
    private LinearLayoutManager mProductsLayoutManager;
    private ProductRecyclerAdapter mProductRecyclerAdapter;
    private SearchView mSearchView;
    private Intent mIntent;
    private String mIntentActionType;

    private static String mSearchQuery = "";
    private View mProgressBar;

    private static LruCache mSearchCache;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        inititializeDisplayParams();

        handleIntent();
    }

    private void inititializeDisplayParams() {
        int cacheSize = 10 * 1024 * 1024; // 10MiB
        mSearchCache = new LruCache<String, List<Product>>(cacheSize) {
            protected int sizeOf(String key, List<Product> products) {
                return products.size();
            }
        };
        mProductRecyclerAdapter = new ProductRecyclerAdapter(this);
        mRecyclerProducts = (RecyclerView) findViewById(R.id.list_products);
        mProductsLayoutManager = new LinearLayoutManager(ProductListActivity.this);
        mProductRecyclerAdapter = new ProductRecyclerAdapter(ProductListActivity.this, mProductList, mSearchQuery);
        mProgressBar = findViewById(R.id.progress_bar_loader);


    }

    private void handleIntent() {

        mIntent = getIntent();
        mIntentActionType = mIntent.getStringExtra(INTENT_ACTION);
        if (mIntentActionType.equals(ACTION_TYPE_SEARCH)) {
            mSearchQuery = mIntent.getStringExtra(SEARCH_QUERY);
        } else if (mIntentActionType.equals(ACTION_TYPE_CATEGORY)) {
            long categoryId = mIntent.getLongExtra(CATEGORY_ID, 0l);
            mProductList = MainActivity.getMagentoAdminClient().extendedCategories().getProductsWithDetailByCategoryId(categoryId);
        }

        displayContent();
    }

    private void displayContent() {
        mRecyclerProducts.setLayoutManager(mProductsLayoutManager);
        mRecyclerProducts.setAdapter(mProductRecyclerAdapter);
        setContentTitle(mSearchQuery);
    }

    public static List<Product> getProductList() {
        return mProductList;
    }

    public static void setProductList(List<Product> productList) {
        mProductList = productList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_list, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.product_search).getActionView();
        if (searchManager != null) {
            mSearchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
        }

        if (mIntentActionType.equals(ACTION_TYPE_SEARCH)) {
            mSearchView.setQuery(mSearchQuery, true);

            mSearchView.setFocusable(true);
            mSearchView.setIconified(false);

            mSearchView.requestFocusFromTouch();

        }

        /**
         * Handle search event.
         */
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // UtilView.showProgress(mProgressBar);
                mProgressBar.setVisibility(View.VISIBLE);
                findViewById(R.id.progress_bar_loader).setVisibility(View.INVISIBLE);
                findViewById(R.id.progress_bar_loader).setVisibility(View.VISIBLE);
                //List<Product> products = MainActivity.getMagentoAdminClient().extendedProducts().search(query);
                List<Product> products = getProduct(query, false);

                if (products == null) {
                    mProductList.clear();
                } else {
                    mProductList = products;
                }

                setContentTitle(query);
                mProductRecyclerAdapter.setProducts(mProductList);
                mSearchQuery = query;
                mProductRecyclerAdapter.setQuery(mSearchQuery);
                mProductRecyclerAdapter.notifyDataSetChanged();

                mProgressBar.setVisibility(View.INVISIBLE);

                //UtilView.hidProgress(mProgressBar);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_SHORT).show();
                return false;
            }

        });

        return true;
    }

    public void setContentTitle(String query) {
        String contentTitle = "";
        if (query == null || query.length() == 0 || mProductList.size() == 0) {

            contentTitle = "No result found. LRU:";
        } else {
            contentTitle = "Search Result(" + mProductList.size() + "): " + query;

        }

        if (mIntentActionType.equals(ACTION_TYPE_CATEGORY)) {
            contentTitle = mProductList.size() + " products found";
        }

//        contentTitle += String.format("LRU State: put %s, hit %s, miss %s, max size %s",
//                mSearchCache.putCount(),
//                mSearchCache.hitCount(),
//                mSearchCache.missCount()
//        );

        TextView textView = (TextView) findViewById(R.id.text_content_title);
        textView.setText(contentTitle);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    protected List<Product> getProduct(String query, boolean referesh) {
        if (mSearchCache.get(query) == null) {
            List<Product> products = MainActivity.getMagentoAdminClient().extendedProducts().search(query);
            mSearchCache.put(query, products);

        } else if (referesh == true) {
            List<Product> products = MainActivity.getMagentoAdminClient().extendedProducts().search(query);

            mSearchCache.remove(query);
            mSearchCache.put(query, products);
        }

        return (List<Product>) mSearchCache.get(query);
    }
}
