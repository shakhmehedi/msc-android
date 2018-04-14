package uk.ac.uws.msc.shakh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.ac.uws.msc.shakh.model.Product;
import uk.ac.uws.msc.shakh.shakhmsc.R;

/**
 * Created by shakh on 14/04/2018.
 */

public class ProductRecyclerAdapter extends RecyclerView.Adapter <ProductRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Product> mProducts;
    private final LayoutInflater mLayoutInflater;

    public ProductRecyclerAdapter(Context context, List<Product> products){

        mContext = context;
        mProducts = products;

        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_product_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = mProducts.get(position);
        holder.getProductName().setText(product.getName());
        holder.getProductPrice().setText(Double.toString(product.getPrice()));
        holder.getProductPriceCurrency().setText("£");
        //ToDo handle image
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView mProductName;
        private final TextView mProductPrice;
        private final TextView mProductPriceCurrency;
        private final ImageView mProductThumb;

        public ViewHolder(View itemView) {
            super(itemView);

            mProductName = (TextView) itemView.findViewById(R.id.text_product_name);
            mProductPrice = (TextView) itemView.findViewById(R.id.text_product_price);
            mProductPriceCurrency = (TextView) itemView.findViewById(R.id.text_product_price_currency);
            mProductThumb = (ImageView) itemView.findViewById(R.id.image_view_product_thumb);


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

        public ImageView getProductThumb() {
            return mProductThumb;
        }
    }

}
