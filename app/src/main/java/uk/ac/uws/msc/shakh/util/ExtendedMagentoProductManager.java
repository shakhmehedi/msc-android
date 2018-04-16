package uk.ac.uws.msc.shakh.util;

import com.alibaba.fastjson.JSON;
import com.github.chen0040.magento.MagentoClient;
import com.github.chen0040.magento.models.Product;
import com.github.chen0040.magento.models.ProductPage;
import com.github.chen0040.magento.services.MagentoProductManager;

import java.util.List;

public class ExtendedMagentoProductManager extends MagentoProductManager {
    private static final String relativePath4Products = "rest/V1/products";

    public ExtendedMagentoProductManager(MagentoClient client) {
        super(client);
    }

    @Override
    public String page(String name, String value, String condition_type) {
        String uri = baseUri() + "/" + relativePath4Products
                + "?searchCriteria[filter_groups][0][filters][0][field]=" + name
                + "&searchCriteria[filter_groups][0][filters][0][value]=" + value
                + "&searchCriteria[filter_groups][0][filters][0][condition_type]=" + condition_type;
        return getSecured(uri);
    }

    public List<Product> search(String query) {
        query = "%" + query + "%";
        String jsonData = page("name", query.toString(), MagentoCollection.CONDITION_TYPE_LIKE);

        return JSON.parseObject(jsonData, ProductPage.class).getItems();
    }
}
