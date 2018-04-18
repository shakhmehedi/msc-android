package uk.ac.uws.msc.shakh;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.github.chen0040.magento.models.Product;

import java.util.ArrayList;
import java.util.List;

import uk.ac.uws.msc.shakh.adapter.ProductRecyclerAdapter;

public class ProductListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String ACTION_TYPE_SEARCH = "action_search";
    public static final String INTENT_ACTION = "intent_action";
    public static final String SEARCH_QUERY = "search_query";
    public static final String SEARCH_ALL_PRODUCTS = "all_products";

    private static List<Product> mProductList = new ArrayList<>();
    private RecyclerView mRecyclerProducts;
    private LinearLayoutManager mProductsLayoutManager;
    private ProductRecyclerAdapter mProductRecyclerAdapter;
    private SearchView mSearchView;
    private Intent mIntent;
    private String mIntentActionType;

    private static String mSearchQuery = "";


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

        mProductRecyclerAdapter = new ProductRecyclerAdapter(this);
        mRecyclerProducts = (RecyclerView) findViewById(R.id.list_products);
        mProductsLayoutManager = new LinearLayoutManager(ProductListActivity.this);
        mProductRecyclerAdapter = new ProductRecyclerAdapter(ProductListActivity.this, mProductList, mSearchQuery);


    }

    private void handleIntent() {

        mIntent = getIntent();
        mIntentActionType = mIntent.getStringExtra(INTENT_ACTION);
        if (mIntentActionType.equals(ACTION_TYPE_SEARCH)) {
            mSearchQuery = mIntent.getStringExtra(SEARCH_QUERY);
        }

        displayContent();
    }

    private void displayContent() {
        mRecyclerProducts.setLayoutManager(mProductsLayoutManager);
        mRecyclerProducts.setAdapter(mProductRecyclerAdapter);
        setContentTitle(mSearchQuery);
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
                reloadProductRecycler(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                reloadProductRecycler(newText);
                return false;
            }

        });

        return true;
    }

    private void reloadProductRecycler(String query) {
        List<Product> products = MainActivity.searchProduct(query);

        if (products == null || query.equals("")) {
            mProductList.clear();
        } else {
            mProductList = products;
        }

        setContentTitle(query);
        mProductRecyclerAdapter.setProducts(mProductList);
        mSearchQuery = query;
        mProductRecyclerAdapter.setQuery(mSearchQuery);
        mProductRecyclerAdapter.notifyDataSetChanged();
    }

    public void setContentTitle(String query) {
        String contentTitle = "";

        if (query == null || query.length() == 0) {
            contentTitle += "Please start typing in the search field";
        } else if (mProductList.size() == 0) {
            contentTitle = "No product found.";
        } else {
            contentTitle += String.format("Search Result(%d) : %s", mProductList.size(), query);

        }

        TextView textView = (TextView) findViewById(R.id.text_content_title);
        textView.setText(contentTitle);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.app_setting) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
