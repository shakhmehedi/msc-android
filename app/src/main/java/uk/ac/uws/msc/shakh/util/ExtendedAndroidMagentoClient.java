package uk.ac.uws.msc.shakh.util;

import com.github.chen0040.androidmagentoclient.AndroidMagentoClient;
import com.github.chen0040.magento.services.HttpComponent;


public class ExtendedAndroidMagentoClient extends AndroidMagentoClient {
    private final ExtendedMagentoProductManager mExtendedProductManager;

    public ExtendedAndroidMagentoClient(String baseUri) {
        super(baseUri);

        //Set custom ExtendedMagentoProductManager
        mExtendedProductManager = new ExtendedMagentoProductManager(this);

    }

    public ExtendedAndroidMagentoClient(String baseUri, HttpComponent httpComponent) {
        super(baseUri, httpComponent);

        //Set custom MagentoProductManager
        mExtendedProductManager = new ExtendedMagentoProductManager(this);

    }

    public ExtendedMagentoProductManager extendedProducts() {
        return mExtendedProductManager;


    }
}
