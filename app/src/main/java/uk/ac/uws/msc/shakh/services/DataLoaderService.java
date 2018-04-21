package uk.ac.uws.msc.shakh.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.github.chen0040.magento.models.Category;
import com.github.chen0040.magento.models.Product;
import com.github.chen0040.magento.models.ProductPage;

import java.util.List;

import uk.ac.uws.msc.shakh.MainActivity;

public class DataLoaderService extends IntentService {
    public static final String INTENT_ID = "DataLoaderService";
    public static final String STATUS = "status";
    public static final String STATUS_SUCCESS = "status_success";
    public static final String STATUS_FAILED = "status_failed";
    public static final String MESSAGE = "message";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DataLoaderService() {
        super("DataLoaderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Intent intentBroadcast = new Intent(INTENT_ID);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());


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

            intentBroadcast.putExtra(MESSAGE, "Product data loaded successfully");
        } else {
            intentBroadcast.putExtra(STATUS, STATUS_FAILED);
            intentBroadcast.putExtra(MESSAGE, "Failed to load product data");

        }

        Category category = MainActivity.getMagentoAdminClient().extendedCategories().all();

        if (category == null) {
            intentBroadcast.putExtra(STATUS, STATUS_FAILED);
            intentBroadcast.putExtra(MESSAGE, "Failed to load category data");
        } else {
            MainActivity.setRootCategory(category);
            intentBroadcast.putExtra(STATUS, STATUS_SUCCESS);

            intentBroadcast.putExtra(MESSAGE, "Product data loaded successfully");
        }

        MainActivity.setIsUsingCacheData(false);

        localBroadcastManager.sendBroadcast(intentBroadcast);
    }
}
