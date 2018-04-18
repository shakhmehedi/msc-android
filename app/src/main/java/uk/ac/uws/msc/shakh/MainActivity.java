package uk.ac.uws.msc.shakh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;

import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.CategoryProduct;
import com.github.chen0040.magento.models.Product;
import com.github.chen0040.magento.models.ProductPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.uws.msc.shakh.adapter.CategoryRecyclerAdapter;
import uk.ac.uws.msc.shakh.adapter.ProductRecyclerAdapter;
import uk.ac.uws.msc.shakh.util.ExtendedAndroidMagentoClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SEARCH_QUERY = "uk.ac.uws.msc.shakh.SEARCH_QUERY";
    public static final String MAGENTO_BASE_URL = "http://192.168.1.91";

    private long mCategoryIDNewProducts = 7l;
    private long mCategoryIDBestSellers = 41l;
    private static List<Product> mProductList = new ArrayList<>();
    public static Map<String, Product> mProductListBySky = new HashMap<String, Product>();

    private static ExtendedAndroidMagentoClient magentoCustomerClient;
    private static ExtendedAndroidMagentoClient magentoAdminClient;
    private CategoryRecyclerAdapter mCategoryRecyclerAdapter;
    private RecyclerView mCategotyRecycler;
    private GridLayoutManager mCategoryLayoutManager;
    private List<Category> mCategories;
    private static LruCache<Long, List<Product>> mCategoryProductCache = new LruCache<>(10 * 1024 * 1024);
    private static LruCache<String, List<Product>> mSearchCache = new LruCache<>(10 * 1024 * 1024);
    private static String mBaseUrl;
    private static String mAdminUsername;
    private static String mAdminPassword;
    private static int mMaxProductToLod;
    private static long mNewCategoryId;
    private static long mBestsellerCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /**
         * Plural sight  NavigationView:
         * https://app.pluralsight.com/player?course=android-enhancing-application-experience&author=jim-wilson&name=android-enhancing-application-experience-m5&clip=6
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        loadProducts();

        /**
         * This is important. If policy is not set, network communication fails.
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initializeSettings(this, R.xml.pref_general);

        loadProducts();
        displayCategoryList();
        displayProductListForCategory(mNewCategoryId, R.id.recycler_view_new_collection);
        displayProductListForCategory(mBestsellerCategoryId, R.id.recycler_view_bestseller);

    }

    public static void initializeSettings(Context context, int rewourceId) {
        PreferenceManager.setDefaultValues(context, rewourceId, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mBaseUrl = preferences.getString("sore_base_url", "");
        mAdminUsername = preferences.getString("admin_username", "");
        mAdminPassword = preferences.getString("admin_password", "");
        mMaxProductToLod = Integer.parseInt(preferences.getString("max_product_to_load", "2000"));
        mNewCategoryId = Long.parseLong(preferences.getString("new_prouct_category_id", "7"));
        mBestsellerCategoryId = Long.parseLong(preferences.getString("bestseller_category_id", "41"));

    }


    private void displayProductListForCategory(long categoryId, int recycleViewId) {
        List<Product> products = MainActivity.getProductsByCategoryIdSku(categoryId);

        ProductRecyclerAdapter productRecyclerAdapter = new ProductRecyclerAdapter(this, products, "");
        RecyclerView recyclerView = (RecyclerView) findViewById(recycleViewId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(productRecyclerAdapter);
    }

    private void displayCategoryList() {

        Category category = MainActivity.getMagentoAdminClient().categories().all();
        mCategories = category.getChildren_data();
        mCategoryRecyclerAdapter = new CategoryRecyclerAdapter(getApplicationContext(), mCategories);
        mCategotyRecycler = (RecyclerView) findViewById(R.id.recycler_view_main_activity_category_list);
        mCategoryLayoutManager = new GridLayoutManager(this, 2);

        mCategotyRecycler.setLayoutManager(mCategoryLayoutManager);
        mCategotyRecycler.setAdapter(mCategoryRecyclerAdapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
            intent.putExtra(ProductListActivity.INTENT_ACTION, ProductListActivity.ACTION_TYPE_SEARCH);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.app_setting) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static ExtendedAndroidMagentoClient getMagentoCustomerClient() {
        if (magentoCustomerClient == null) {
            magentoCustomerClient = new ExtendedAndroidMagentoClient(mBaseUrl);
            magentoCustomerClient.loginAsClient("shakhmehedi@yahoo.com", "shakhmscpasS1");
        }
        return magentoCustomerClient;
    }

    public static ExtendedAndroidMagentoClient getMagentoAdminClient() {
        if (magentoAdminClient == null) {
            magentoAdminClient = new ExtendedAndroidMagentoClient(mBaseUrl);
            magentoAdminClient.loginAsAdmin(mAdminUsername, mAdminPassword);
        }
        return magentoAdminClient;
    }

    public static List<Product> searchProduct(String query) {
        loadProducts();

        if (query.equals(ProductListActivity.SEARCH_ALL_PRODUCTS)) {
            return mProductList;
        }

        List<Product> result = new ArrayList<>();

        /**
         * Search product
         *
         * ToDo Implement search order, search in ollowing order
         * sku,
         * name,
         * short_description
         * description
         */
        for (Product product :
                mProductList) {
            String searchString = String.format("%s | %s", product.getSku(), product.getName());
            if (searchString.toLowerCase().contains(query.toLowerCase())) {
                result.add(product);
            }

        }

        return result;
    }


    public static List<Product> getProductsByCategoryIdSku(long categoryId) {
        loadProducts();


        if (mCategoryProductCache.get(categoryId) == null) {
            List<CategoryProduct> categoryProducts = MainActivity.getMagentoAdminClient()
                    .extendedCategories().getProductsInCategory(categoryId);

            List<Product> products = new ArrayList<>();

            for (CategoryProduct categoryProduct :
                    categoryProducts) {
                Product product = mProductListBySky.get(categoryProduct.getSku());
                if (product != null) {
                    products.add(product);
                }
            }

            mCategoryProductCache.put(categoryId, products);
        }


        return mCategoryProductCache.get(categoryId);
    }

    public static void loadProducts() {
        if (mProductList.size() == 0) {
            List<Product> products;
            ProductPage productPage = MainActivity.getMagentoAdminClient().extendedProducts()
                    .page(1, mMaxProductToLod);

            products = productPage.getItems();
            if (products.size() > 0) {
                mProductList = products;

                for (Product product :
                        mProductList) {
                    mProductListBySky.put(product.getSku(), product);
                }
            }


        }
    }

}
