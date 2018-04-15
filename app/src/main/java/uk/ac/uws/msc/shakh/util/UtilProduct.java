package uk.ac.uws.msc.shakh.util;

import android.content.Context;
import android.widget.ImageView;

import com.github.chen0040.magento.models.MagentoAttribute;
import com.github.chen0040.magento.models.Product;
import com.squareup.picasso.Picasso;

import uk.ac.uws.msc.shakh.MainActivity;
import uk.ac.uws.msc.shakh.ProductDetailActivity;

public class UtilProduct {
    public static void loadImage(ProductDetailActivity productDetailActivity, Context context, ImageView imageView, Product product) {
//        String baseUrl = MainActivity.getMagentoAdminClient().getMedia().getProductMediaAbsoluteUrls(product.getSku());
        String imageFileName = getProductExtraAttributeValue(product, "image");
        String imageUrl = MainActivity.getMagentoAdminClient().getBaseUri() + "/pub/media/catalog/product/" + imageFileName;

        Picasso
                .get()
                .load(imageUrl)
                .into(imageView);
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
}
