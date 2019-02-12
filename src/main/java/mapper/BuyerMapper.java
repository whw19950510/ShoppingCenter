
package mapper;

import bean.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BuyerMapper {
     List<Product> listAllProduct();
     Product selectProductById(int productId);
     void updateSingleProduct(Product curProduct);
     List<Product> selectProductByIdList(@Param("productIdList") List<Integer> productIdList);
}
