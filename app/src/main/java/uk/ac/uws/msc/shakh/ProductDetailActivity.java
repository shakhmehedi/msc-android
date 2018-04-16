package uk.ac.uws.msc.shakh;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chen0040.magento.models.Product;
import com.github.chen0040.magento.models.StockItems;

import uk.ac.uws.msc.shakh.shakhmsc.R;
import uk.ac.uws.msc.shakh.util.UtilProduct;

public class ProductDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PRODUCT_POSITION = "uk.ac.uws.msc.shakh.PRODUCT_POSITION";
    public static final String PRODUCT_SKU = "uk.ac.uws.msc.shakh.PRODUCT_SKU";
    private static final int POSITION_NOT_SET = -1;
    private int mProductPosition;
    private boolean mIsValidProduct;
    private Product mProduct;
    private TextView mTextProductName;
    private String mProductSku;
    private TextView mTextProductSku;
    private TextView mTextProductStock;
    private TextView mTextProductPrice;
    private Button mButtonAddToCart;
    private WebView mWebviewProductDescription;
    private StockItems mProductStockItem;
    private ImageView mImageProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayProductDetail();
    }

    private void displayProductDetail() {
        Intent intent = getIntent();
        mProductSku = intent.getStringExtra(PRODUCT_SKU);
        mProductPosition = intent.getIntExtra(PRODUCT_POSITION, POSITION_NOT_SET);
        if (mProductPosition > POSITION_NOT_SET) {
            mIsValidProduct = true;
        }

        mProduct = MainActivity.getMagentoAdminClient().getProducts().getProductBySku(mProductSku);
        mProductStockItem = MainActivity.getMagentoAdminClient().getInventory().getStockItems(mProductSku);

        mImageProduct = (ImageView) findViewById(R.id.image_product_main);
        mTextProductName = (TextView) findViewById(R.id.text_product_name);
        mTextProductSku = (TextView) findViewById(R.id.text_product_sku);
        mTextProductStock = (TextView) findViewById(R.id.text_product_stock);
        mTextProductPrice = (TextView) findViewById(R.id.text_product_price);
        mButtonAddToCart = (Button) findViewById(R.id.button_add_to_cart);
        mWebviewProductDescription = (WebView) findViewById(R.id.webview_product_description);


        UtilProduct.loadImage(mImageProduct, mProduct, UtilProduct.PRODUCT_IMAGE);
        mTextProductName.setText(mProduct.getName());
        mTextProductSku.setText(mProduct.getSku());
        mTextProductStock.setText(Integer.toString(mProductStockItem.getQty()));
        mTextProductPrice.setText(Double.toString(mProduct.getPrice()));

        String description = UtilProduct.getProductExtraAttributeValue(mProduct, "description");
        mWebviewProductDescription.loadData(description, "text/html", null);

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
        getMenuInflater().inflate(R.menu.product_detail, menu);
        
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

            Intent intent = new Intent(ProductDetailActivity.this, ProductListActivity.class);
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
}
