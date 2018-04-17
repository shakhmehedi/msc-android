package uk.ac.uws.msc.shakh.util;

import android.widget.ImageView;

import com.github.chen0040.magento.models.Product;
import com.squareup.picasso.Picasso;

import uk.ac.uws.msc.shakh.MainActivity;

import com.github.chen0040.magento.models.Category;

public class UtilCategory {
//    public static void loadImage(ImageView imageView, Product product, String imageAttrinuteCode) {
//        String imageFileName = getProductExtraAttributeValue(product, imageAttrinuteCode);
//        String imageUrl = MainActivity.getMagentoAdminClient().getBaseUri() + "/pub/media/catalog/product/" + imageFileName;
//
//        if (imageAttrinuteCode.equals(PRODUCT_IMAGE_THUMBNAIL)) {
//            Picasso.get()
//                    .load(imageUrl)
//                    .resize(50, 70)
//                    .into(imageView);
//        } else {
//            Picasso.get()
//                    .load(imageUrl)
//                    .into(imageView);
//        }
//    }

    public static void loadImage(ImageView imageView, Category category, String imageAttributeCode) {
        String url = MainActivity.getMagentoAdminClient().getBaseUri();
    }
}
