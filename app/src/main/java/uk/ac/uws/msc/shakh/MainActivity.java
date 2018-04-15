package uk.ac.uws.msc.shakh;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.github.chen0040.androidmagentoclient.AndroidMagentoClient;

import java.util.List;

import uk.ac.uws.msc.shakh.adapter.ProductRecyclerAdapter;
import uk.ac.uws.msc.shakh.model.TestDataManager;
import uk.ac.uws.msc.shakh.shakhmsc.R;

//import android.support.v7.widget.SearchView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SEARCH_QUERY = "uk.ac.uws.msc.shakh.SEARCH_QUERY";
    public static final String MAGENTO_BASE_URL = "http://192.168.1.91";
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mProductsLayoutManager;
    // private ProductRecyclerAdapter mCoursesLayoutManager;
    private ProductRecyclerAdapter mProductRecyclerAdapter;

    private static String mClientToken = "";
    private static String mAdminToken = "";

    private static AndroidMagentoClient magentoCustomerClient;
    private static AndroidMagentoClient magentoAdminClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toast.makeText(this, "Created new customer token: " + this.getMagentoCustomerClient().getToken()
                , Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Created new admin token: " + MainActivity.getMagentoAdminClient().getToken()
                , Toast.LENGTH_SHORT).show();

        initDisplayContent();
    }

    private void initDisplayContent() {
        mRecyclerItems = (RecyclerView) findViewById(R.id.list_products);
        mProductsLayoutManager = new LinearLayoutManager(this);

        List<com.github.chen0040.magento.models.Product> products
                = MainActivity.getMagentoAdminClient().getProducts().page(1, 20).getItems();

        mProductRecyclerAdapter = new ProductRecyclerAdapter(this, products);

        displayContent();
    }

    private void displayContent() {
        mRecyclerItems.setLayoutManager(mProductsLayoutManager);
        mRecyclerItems.setAdapter(mProductRecyclerAdapter);
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

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.product_search).getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
        }

        /**
         * Handle search event.
         */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                intent.putExtra(SEARCH_QUERY, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

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

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static AndroidMagentoClient getMagentoCustomerClient() {
        if (magentoCustomerClient == null) {
            magentoCustomerClient = new AndroidMagentoClient(MAGENTO_BASE_URL);
            magentoCustomerClient.loginAsClient("shakhmehedi@yahoo.com", "shakhmscpasS1");
        }
        return magentoCustomerClient;
    }

    public static AndroidMagentoClient getMagentoAdminClient() {
        if (magentoAdminClient == null) {
            magentoAdminClient = new AndroidMagentoClient(MAGENTO_BASE_URL);
            magentoAdminClient.loginAsAdmin("admin", "shakhmscpasS1]");
        }
        return magentoAdminClient;
    }
}
