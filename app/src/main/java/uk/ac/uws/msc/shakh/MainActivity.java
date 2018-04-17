package uk.ac.uws.msc.shakh;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import uk.ac.uws.msc.shakh.adapter.ProductRecyclerAdapter;
import uk.ac.uws.msc.shakh.shakhmsc.R;
import uk.ac.uws.msc.shakh.util.ExtendedAndroidMagentoClient;

//import android.support.v7.widget.SearchView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SEARCH_QUERY = "uk.ac.uws.msc.shakh.SEARCH_QUERY";
    public static final String MAGENTO_BASE_URL = "http://192.168.1.91";
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mProductsLayoutManager;
    private ProductRecyclerAdapter mProductRecyclerAdapter;

    private static String mClientToken = "";
    private static String mAdminToken = "";

    private static ExtendedAndroidMagentoClient magentoCustomerClient;
    private static ExtendedAndroidMagentoClient magentoAdminClient;
    private TabLayout.Tab mTabCategory;
    private TabLayout.Tab mTabAccount;

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


        TabLayout drawerTabLayout = (TabLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_tab_layout);

        mTabCategory = drawerTabLayout.newTab();
        mTabCategory.setText("Category");
        /**
         * To add content to tab, need to use fragment
         * http://android-er.blogspot.co.uk/2012/06/create-actionbar-in-tab-navigation-mode.html
         */
        mTabCategory.setCustomView(R.layout.tab_category_content);
        drawerTabLayout.addTab(mTabCategory);

        mTabAccount = drawerTabLayout.newTab();
        mTabAccount.setText("Account");
        drawerTabLayout.addTab(mTabAccount);

        /**
         * This is important. If policy is not set, network communication fails.
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button btnViewAllProducts = (Button) findViewById(R.id.button_view_all_product);
        btnViewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                intent.putExtra(ProductListActivity.INTENT_ACTION, ProductListActivity.ACTION_TYPE_SEARCH);
                intent.putExtra(ProductListActivity.SEARCH_QUERY, ProductListActivity.RETURN_ALL_PRODUCTS);
                startActivity(intent);
            }
        });

        Button btnViewAllCategory = (Button) findViewById(R.id.button_view_all_category);
        btnViewAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryListActivity.class);
                intent.putExtra(CategoryListActivity.INTENT_ACTION, CategoryListActivity.ACTION_TYPE_VIEW_ALL_CATEGORY);
                startActivity(intent);
            }
        });

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

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static ExtendedAndroidMagentoClient getMagentoCustomerClient() {
        if (magentoCustomerClient == null) {
            magentoCustomerClient = new ExtendedAndroidMagentoClient(MAGENTO_BASE_URL);
            magentoCustomerClient.loginAsClient("shakhmehedi@yahoo.com", "shakhmscpasS1");
        }
        return magentoCustomerClient;
    }

    public static ExtendedAndroidMagentoClient getMagentoAdminClient() {
        if (magentoAdminClient == null) {
            magentoAdminClient = new ExtendedAndroidMagentoClient(MAGENTO_BASE_URL);
            magentoAdminClient.loginAsAdmin("admin", "shakhmscpasS1]");
        }
        return magentoAdminClient;
    }


}
