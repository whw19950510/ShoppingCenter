
package mapper;


import bean.Product;

public interface SellerMapper {
    int insertNewProduct(Product product);
    Product selectProductById(int productId);
    void deleteProductById(int productId);
    int updateExistProduct(Product curProduct);
}
