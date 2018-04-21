package uk.ac.uws.msc.shakh.util;

import android.content.Context;
import android.widget.ImageView;

import com.github.chen0040.magento.models.MagentoAttribute;
import com.github.chen0040.magento.models.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import uk.ac.uws.msc.shakh.MainActivity;
import uk.ac.uws.msc.shakh.ProductDetailActivity;

public class UtilProduct {
    public static final String PRODUCT_IMAGE = "image";
    public static final String PRODUCT_IMAGE_SMALL = "small_image";
    public static final String PRODUCT_IMAGE_THUMBNAIL = "thumbnail";

    public static void loadImage(ImageView imageView, Product product, String imageAttrinuteCode) {
        String imageFileName = getProductExtraAttributeValue(product, imageAttrinuteCode);
        String imageUrl = MainActivity.getBaseUrl() + "/pub/media/catalog/product/" + imageFileName;

        if (imageAttrinuteCode.equals(PRODUCT_IMAGE_THUMBNAIL)) {
            Picasso.get()
                    .load(imageUrl)
                    .resize(50, 70)
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(imageUrl)
                    .into(imageView);
        }
    }

    public static String getProductExtraAttributeValue(Product product, String attributeCode) {
        String value = null;

        for (MagentoAttribute attribute :
                product.getCustom_attributes()) {
            if (attributeCode.equals(attribute.getAttribute_code())) {
                value = (String) attribute.getValue();
                return value;
            }
        }

        for (MagentoAttribute attribute :
                product.getExtension_attributes()) {
            if (attributeCode.equals(attribute.getAttribute_code())) {
                value = (String) attribute.getValue();
                return value;
            }

        }
        return value;

    }

//    public static List<uk.ac.uws.msc.shakh.model.Product> search(String query){
//        List<uk.ac.uws.msc.shakh.model.Product> products;
//
//        /**
//         * Search in product sku
//         */
//
//        return List;
//    }
}
