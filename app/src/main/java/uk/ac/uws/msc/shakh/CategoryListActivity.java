package uk.ac.uws.msc.shakh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.Product;

import java.util.ArrayList;
import java.util.List;

import uk.ac.uws.msc.shakh.adapter.CategoryRecyclerAdapter;
import uk.ac.uws.msc.shakh.adapter.ProductRecyclerAdapter;

public class CategoryListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ACTION_TYPE_VIEW_ALL_CATEGORY = "view_all_category";
    public static final String INTENT_ACTION = "intent_action";
    public static final String ACTION_TYPE_VIEW_CATEGORY = "view_category";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    private Intent mIntent;
    private String mIntentActionType;
    private List<Category> mCategories;
    private TextView mCategoryTitle;
    private Category mCurrentCategory;
    private List<Product> mProductList = new ArrayList<>();
    private ProductRecyclerAdapter mProductRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private Button mButtonShowProducts;
    private Button mButtonShowProductsNewActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mButtonShowProducts = (Button) findViewById(R.id.button_show_products);
        mButtonShowProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadProductRecycler();


            }
        });
        mButtonShowProductsNewActivity = (Button) findViewById(R.id.button_show_products_new_activity);
        mButtonShowProductsNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryListActivity.this, CategoryProductListActivity.class);
                intent.putExtra(CATEGORY_ID, mCurrentCategory.getId());
                intent.putExtra(CATEGORY_NAME, mCurrentCategory.getName());
                startActivity(intent);
            }
        });
        initializeDisplayParam();

        hendleIntent();


    }


    private void initializeDisplayParam() {
        mCategoryTitle = (TextView) findViewById(R.id.text_category_title);

    }


    private void hendleIntent() {
        mIntent = getIntent();

        mIntentActionType = mIntent.getStringExtra(INTENT_ACTION);

        if (mIntentActionType.equals(ACTION_TYPE_VIEW_ALL_CATEGORY)) {
            Category category = MainActivity.getRootCategory();
            mCurrentCategory = category;
            mCategories = category.getChildren_data();
        } else if (mIntentActionType.equals(ACTION_TYPE_VIEW_CATEGORY)) {
            long categoryId = mIntent.getLongExtra(CATEGORY_ID, 0l);
            Category category = MainActivity.getCategoryByIdWithChildren(categoryId);
            mCategories = category.getChildren_data();
            mCurrentCategory = category;
        }

        displayCategory();

        displeyProductList();

    }

    private void displayCategory() {
        initializeDisplayParam();

        CategoryRecyclerAdapter categoryRecyclerAdapter = new CategoryRecyclerAdapter(getApplicationContext(), mCategories);
        RecyclerView categotyRecycler = (RecyclerView) findViewById(R.id.recycler_view_category_list_activity_category_list);
        GridLayoutManager categoryLayoutManager = new GridLayoutManager(this, 2);

        if (mCurrentCategory == null || mCurrentCategory.getChildren_data().size() == 0) {
            mCategoryTitle.setVisibility(View.GONE);
        } else {
            mCategoryTitle.setText("Category: " + mCurrentCategory.getName());
        }


        categotyRecycler.setLayoutManager(categoryLayoutManager);
        categotyRecycler.setAdapter(categoryRecyclerAdapter);


    }

    private void displeyProductList() {


        mProductRecyclerAdapter = new ProductRecyclerAdapter(this, mProductList, "Category");


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_category_list_activity_product_list);
        mRecyclerView.setNestedScrollingEnabled(false);
        ((NestedScrollView) findViewById(R.id.nested_scrol_view)).setNestedScrollingEnabled(false);
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scrol_view);
        nestedScrollView.setSmoothScrollingEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CategoryListActivity.this);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mProductRecyclerAdapter);
        if (mCurrentCategory != null) {
            mProductList = MainActivity.getProductsByCategoryIdSku(mCurrentCategory.getId());
        }
        setProductListTitle();

    }

    private void reloadProductRecycler() {


        mProductRecyclerAdapter.setProducts(mProductList);
        mProductRecyclerAdapter.notifyDataSetChanged();
        setProductListTitle();

    }

    private void setProductListTitle() {
        TextView productTextView = (TextView) findViewById(R.id.text_category_list_activity_product_title);
        if (mProductList != null && mProductList.size() > 0) {
            productTextView.setText(String.format("%d Product(s) found.", mProductList.size()));
        } else {
            productTextView.setText(String.format("No products found in the category %s", mCurrentCategory.getName()));
        }
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
        getMenuInflater().inflate(R.menu.category_list, menu);
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

            Intent intent = new Intent(CategoryListActivity.this, ProductListActivity.class);
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

}
