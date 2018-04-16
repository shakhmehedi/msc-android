package uk.ac.uws.msc.shakh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chen0040.magento.models.Product;

import java.util.List;

import uk.ac.uws.msc.shakh.MainActivity;
import uk.ac.uws.msc.shakh.ProductDetailActivity;
import uk.ac.uws.msc.shakh.shakhmsc.R;
import uk.ac.uws.msc.shakh.util.UtilProduct;

/**
 * Created by shakh on 14/04/2018.
 */

public class ProductRecyclerAdapter extends RecyclerView.Adapter <ProductRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private List<com.github.chen0040.magento.models.Product> mProducts;
    private final LayoutInflater mLayoutInflater;
    private String mQuery;

    public ProductRecyclerAdapter(Context context, List<Product> products, String query) {

        mContext = context;
        mProducts = products;

        mLayoutInflater = LayoutInflater.from(context);
        mQuery = query;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_product_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        com.github.chen0040.magento.models.Product product = mProducts.get(position);
        UtilProduct.loadImage(holder.getProductThumb(), product, UtilProduct.PRODUCT_IMAGE_THUMBNAIL);
        holder.getProductName().setText(product.getName());
        holder.getProductPrice().setText(Double.toString(product.getPrice()));
        holder.getProductPriceCurrency().setText("£");
        holder.getProductSku().setText(product.getSku());
        holder.setCurrentPosition(position);
        //ToDo handle image
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public List<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mProductName;
        private final TextView mProductPrice;
        private final TextView mProductPriceCurrency;
        private final TextView mProductSku;
        private final ImageView mProductThumb;
        private int mCurrentPosition;

        public ViewHolder(View itemView) {
            super(itemView);

            mProductName = (TextView) itemView.findViewById(R.id.text_product_name);
            mProductPrice = (TextView) itemView.findViewById(R.id.text_product_price);
            mProductPriceCurrency = (TextView) itemView.findViewById(R.id.text_product_price_currency);
            mProductSku = (TextView) itemView.findViewById(R.id.text_product_sku);
            mProductThumb = (ImageView) itemView.findViewById(R.id.image_view_product_thumb);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra(ProductDetailActivity.PRODUCT_POSITION, mCurrentPosition);
                    String sku = ((TextView) itemView.findViewById(R.id.text_product_sku)).getText().toString();
                    intent.putExtra(ProductDetailActivity.PRODUCT_SKU, sku);
                    if (mQuery.length() > 0) {
                        intent.putExtra(MainActivity.SEARCH_QUERY, mQuery);
                    }
                    mContext.startActivity(intent);
                }
            });
        }

        public TextView getProductName() {
            return mProductName;
        }

        public TextView getProductPrice() {
            return mProductPrice;
        }

        public TextView getProductPriceCurrency() {
            return mProductPriceCurrency;
        }

        public TextView getProductSku() {
            return mProductSku;
        }

        public ImageView getProductThumb() {
            return mProductThumb;
        }

        public int getCurrentPosition() {
            return mCurrentPosition;
        }

        public void setCurrentPosition(int currentPosition) {
            mCurrentPosition = currentPosition;
        }
    }

}
