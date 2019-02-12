
package service;

import bean.Product;
import bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import enums.Status;
import exception.DisplayInformationException;
import exception.ObjectNotFoundException;
import mapper.BuyerMapper;
import mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.SystemInfoUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

// 购买的price始终是加到购物车的price...
@Service
public class BuyerService {
    @Autowired
    BuyerMapper buyerMapper;
    @Autowired
    SellerService sellerService;

    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    Logger logger = LoggerFactory.getLogger(BuyerService.class);


    public Map<String, Object> displayAllProduct() {
        List<Product> allProduct = buyerMapper.listAllProduct();
        return ImmutableMap.of("products", (Object)allProduct);
    }

    public Map<String, Object> showProductDetail(Integer productId) {
        Product target = buyerMapper.selectProductById(productId);
        if(target == null) {
            throw new NoSuchElementException("No such product");
        }
        return ImmutableMap.of("product", (Object) target);
    }

    public Map<String, Object> displayShoppingCart() throws IOException, ObjectNotFoundException {
        User curUser = userService.getCurrentUser();
        Map<Integer, Integer> shoppingCart = curUser.getShoppingCartIdMap();
        List<Integer> shoppingCartList = new ArrayList<Integer>();
        for(Integer cur : shoppingCart.keySet()) {
            shoppingCartList.add(cur);
        }
        List<Product> cartProduct = buyerMapper.selectProductByIdList(shoppingCartList);
        return ImmutableMap.of("shoppingCart", (Object)cartProduct, "number", (Object)
                shoppingCart.values());
    }

    public Map<String, Object> displayOwnedProduct() throws IOException, ObjectNotFoundException {
        User curUser = userService.getCurrentUser();
        Map<Integer, Integer> owned = curUser.getOwnedIdMap();
        Map<Integer, Double> boughtPriceMap = curUser.getBoughtPriceMap();
        List<Integer> ownedIdList = new ArrayList<Integer>();
        for(Integer cur : owned.keySet()) {
            ownedIdList.add(cur);
        }
        if(!owned.isEmpty()) {
            List<Product> ownedProduct = buyerMapper.selectProductByIdList(ownedIdList);
            double sumPrice = 0;
            for(Integer cur : owned.keySet()) {
                if(boughtPriceMap.containsKey(cur)) {
                    sumPrice += boughtPriceMap.get(cur) * owned.get(cur);
                }
            }
            return ImmutableMap.of("ownedProduct",(Object)ownedProduct, "totalPrice", sumPrice);
        }
        return ImmutableMap.of("ownedProduct", (Object)"", "totalPrice", 0);
    }

    /**
     * Product added to ShoppingCart
     * @param curProduct, 需要有Id, price, ...
     * @return
     * @throws IOException
     */
    public Map<String, Object> addToShoppingCart(Product curProduct, int boughtNumber) throws
            IOException, ObjectNotFoundException {
        User curUser = userService.getCurrentUser();
        curUser.addSingleProductToShoppingCart(curProduct.getProductId(), boughtNumber,
                curProduct.getPrice());
        // add to shoppingCart List
        userMapper.updateSingleUser(curUser);
        return ImmutableMap.of("added", (Object)curProduct);
    }

    /**
     * finish transaction from the shoppingCart to OwnedList
     * @param productNumList
     * @return
     * @throws IOException
     */

    public Map<String, Object> finishTransaction(Map<String, Integer> productNumList)
            throws IOException, ObjectNotFoundException, DisplayInformationException {
        User curUser = userService.getCurrentUser();
        Map<Integer, Integer> shoppingCartId = curUser.getShoppingCartIdMap();
        Map<Integer, Double> boughtPrice = curUser.getBoughtPriceMap();

        List<Integer> shoppingCartIdList = new ArrayList<Integer>();
        Map<Integer, Integer> ownedIdMap = curUser.getOwnedIdMap();

        for(Integer cur : shoppingCartId.keySet()) {
            shoppingCartIdList.add(cur);
        }
        if(shoppingCartIdList.isEmpty()) {
            throw new DisplayInformationException("ShoppingCart is empty");
        }
        List<Product> shoppingCart = buyerMapper.selectProductByIdList(shoppingCartIdList);


        // 更新当前的商品最终结算数量
        for(Product cur : shoppingCart) {
            if(productNumList.containsKey(cur.getCaption()) &&
                    productNumList.get(cur.getCaption()) != 0) {
                cur.setStatus(Status.SOLD);
                cur.setBoughtTime(new Date(System.currentTimeMillis()));
                cur.setNumber(productNumList.get(cur.getCaption()));
                if(!ownedIdMap.containsKey(cur.getProductId())) {
                    ownedIdMap.put(cur.getProductId(), productNumList.get(cur.getCaption()));
                } else {
                    ownedIdMap.put(cur.getProductId(), productNumList.get(cur.getCaption()) +
                            ownedIdMap.get(cur.getProductId()));
                }
                buyerMapper.updateSingleProduct(cur);
            } else if(productNumList.containsKey(cur.getCaption()) &&
                    productNumList.get(cur.getCaption()) == 0) {
                boughtPrice.remove(cur.getProductId());
            }
        }

        curUser.setOwnedIdMap(ownedIdMap);
        curUser.setShoppingCart("{}");
        curUser.setBoughtPriceMap(boughtPrice);
        userMapper.updateSingleUser(curUser);
        return ImmutableMap.of("user", curUser, "product", shoppingCart);
    }
}
