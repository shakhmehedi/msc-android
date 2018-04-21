package uk.ac.uws.msc.shakh.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.Product;
import com.github.chen0040.magento.models.ProductPage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import uk.ac.uws.msc.shakh.MainActivity;

public class DataLoaderService extends IntentService {
    public static final String INTENT_ID = "DataLoaderService";
    public static final String STATUS = "status";
    public static final String STATUS_SUCCESS = "status_success";
    public static final String STATUS_FAILED = "status_failed";
    public static final String MESSAGE = "message";
    public static final String DATA_MODE_SAMPLE = "data_mode_sample";
    public static final String DATA_MODE_LIVE = "data_mode_live";
    public static final String DATA_MODE = "data_mode";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DataLoaderService() {
        super("DataLoaderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String dataMode = intent.getStringExtra(DATA_MODE);
        if (dataMode.equals(DATA_MODE_LIVE)) {

            loadLiveData();
        } else if (dataMode.equals(DATA_MODE_SAMPLE)) {
            loadSampleData();
        }

    }

    private void loadLiveData() {
        Intent intentBroadcast = new Intent(INTENT_ID);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        String msg = "";

        List<Product> products;
        ProductPage productPage = MainActivity.getMagentoAdminClient().extendedProducts()
                .page(1, MainActivity.getMaxProductToLoad());

        products = productPage.getItems();
        if (products.size() > 0) {
            MainActivity.getProductList().clear();

            MainActivity.getProductList().addAll(products);

            MainActivity.getProductListBySku().clear();
            for (Product product :
                    products) {
                MainActivity.getProductListBySku().put(product.getSku(), product);
            }

            MainActivity.setIsProductDataLoaded(true);

            intentBroadcast.putExtra(STATUS, STATUS_SUCCESS);

            msg += "Product data loaded successfully.";
        } else {
            intentBroadcast.putExtra(STATUS, STATUS_FAILED);
            msg += " Failed to load product data.";

        }

        Category category = MainActivity.getMagentoAdminClient().extendedCategories().all();

        if (category == null) {
            intentBroadcast.putExtra(STATUS, STATUS_FAILED);
            msg += " Failed to load category data.";
        } else {
            MainActivity.setRootCategory(category);
            intentBroadcast.putExtra(STATUS, STATUS_SUCCESS);

            msg += " Product data loaded successfully.";
        }
        intentBroadcast.putExtra(MESSAGE, msg);

        MainActivity.setIsUsingCacheData(false);

        localBroadcastManager.sendBroadcast(intentBroadcast);
    }

    public void loadSampleData() {


        Intent intentBroadcast = new Intent(INTENT_ID);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        String msg = "";
        String jsonStringProduct;
        String jsonStringCategory;
        try {
            jsonStringProduct = readFromAssets(getApplicationContext(), "products.json");

            jsonStringProduct = JSON.parseObject(jsonStringProduct, String.class);
            ProductPage productPage = JSON.parseObject(jsonStringProduct, ProductPage.class);
            List<Product> products = productPage.getItems();
            if (products.size() > 0) {
                MainActivity.getProductList().clear();

                MainActivity.getProductList().addAll(products);

                MainActivity.getProductListBySku().clear();
                for (Product product :
                        products) {
                    MainActivity.getProductListBySku().put(product.getSku(), product);
                }

                MainActivity.setIsProductDataLoaded(true);

                intentBroadcast.putExtra(STATUS, STATUS_SUCCESS);

                msg += "Product data loaded successfully.";
            } else {
                intentBroadcast.putExtra(STATUS, STATUS_FAILED);

                msg += " Failed to load product data.";
            }


            jsonStringCategory = readFromAssets(getApplicationContext(), "categories.json");

            jsonStringCategory = JSON.parseObject(jsonStringCategory, String.class);
            Category category = JSON.parseObject(jsonStringCategory, Category.class);

            if (category == null) {
                intentBroadcast.putExtra(STATUS, STATUS_FAILED);

                msg += " Failed to load category data.";
            } else {
                MainActivity.setRootCategory(category);
                intentBroadcast.putExtra(STATUS, STATUS_SUCCESS);

                msg += "Product data loaded successfully";
            }

            msg += "Using sample data. " + msg;
            intentBroadcast.putExtra(MESSAGE, msg);

            MainActivity.setIsUsingCacheData(true);

            localBroadcastManager.sendBroadcast(intentBroadcast);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Taken from
     * https://stackoverflow.com/questions/5771366/reading-a-simple-text-file
     *
     * @param context
     * @param filename
     * @return
     * @throws IOException
     */
    public String readFromAssets(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine); // process line
            mLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }
}
