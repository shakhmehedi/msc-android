package uk.ac.uws.msc.shakh.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.github.chen0040.magento.models.Category;

import uk.ac.uws.msc.shakh.CategoryListActivity;
import uk.ac.uws.msc.shakh.ProductListActivity;
import uk.ac.uws.msc.shakh.shakhmsc.R;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Category> mCategories;
    private final LayoutInflater mLayoutInflater;

    public CategoryRecyclerAdapter(Context context, List<Category> categories) {

        mContext = context;
        mCategories = categories;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_category_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = mCategories.get(position);
        holder.setCurrentPosition(position);
        holder.setCurrentCategoryId(category.getId());
        holder.getTextCategoryName().setText(category.getName());

        if (category.getProduct_count() < 1) {
            holder.getButtonViewProduct().setEnabled(false);
        }

        if (category.getChildren_data().size() < 1) {
            holder.getButtonViewCategory().setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextCategoryName;
        private Button mButtonViewProduct;
        private ImageView mImageCategory;
        private Button mButtonViewCategory;
        private int mCurrentPosition;
        private long mCurrentCategoryId;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextCategoryName = (TextView) itemView.findViewById(R.id.text_category_name);
            mImageCategory = (ImageView) itemView.findViewById(R.id.image_category);
            mButtonViewCategory = (Button) itemView.findViewById(R.id.button_view_sub_category);
            mButtonViewProduct = (Button) itemView.findViewById(R.id.button_view_products);

            Category category = mCategories.get(mCurrentPosition);

            mButtonViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category category = mCategories.get(mCurrentPosition);

                    Intent intent = new Intent(mContext, ProductListActivity.class);
                    intent.putExtra(ProductListActivity.INTENT_ACTION, ProductListActivity.ACTION_TYPE_CATEGORY);
                    intent.putExtra(ProductListActivity.CATEGORY_ID, category.getId());
                    mContext.startActivity(intent);


                }
            });

            mButtonViewCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Category category = mCategories.get(mCurrentPosition);

                    Intent intent = new Intent(mContext, CategoryListActivity.class);
                    intent.putExtra(CategoryListActivity.INTENT_ACTION, CategoryListActivity.ACTION_TYPE_VIEW_CATEGORY);
                    intent.putExtra(CategoryListActivity.CATEGORY_ID, category.getId());
                    mContext.startActivity(intent);


                }
            });
        }

        public TextView getTextCategoryName() {
            return mTextCategoryName;
        }

        public Button getButtonViewProduct() {
            return mButtonViewProduct;
        }

        public ImageView getImageCategory() {
            return mImageCategory;
        }

        public Button getButtonViewCategory() {
            return mButtonViewCategory;
        }

        public int getCurrentPosition() {
            return mCurrentPosition;
        }

        public void setCurrentPosition(int currentPosition) {
            mCurrentPosition = currentPosition;
        }

        public long getCurrentCategoryId() {
            return mCurrentCategoryId;
        }

        public void setCurrentCategoryId(long currentCategoryId) {
            mCurrentCategoryId = currentCategoryId;
        }

        public void setTextCategoryName(TextView textCategoryName) {
            mTextCategoryName = textCategoryName;
        }

        public void setButtonViewProduct(Button buttonViewProduct) {
            mButtonViewProduct = buttonViewProduct;
        }

        public void setImageCategory(ImageView imageCategory) {
            mImageCategory = imageCategory;
        }

        public void setButtonViewCategory(Button buttonViewCategory) {
            mButtonViewCategory = buttonViewCategory;
        }
    }
}
