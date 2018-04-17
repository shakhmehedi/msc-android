Shakh MSc project: Android Native App
=====================================
# ToDo
* ProductDetailActivity
    * Add next/prev/back button


# Draft
JSON.parseObject(getMagentoAdminClient().extendedProducts().page("sku", "24-MB01,24-MB04","in"), ProductPage.class)

MainActivity.getMagentoAdminClient().getProducts().page(1,20).getItems().get(0)

getMagentoAdminClient().categories().getProductsInCategory(3l)
MainActivity.getMagentoAdminClient().extendedCategories().getProductsWithDetailByCategoryId(2l)