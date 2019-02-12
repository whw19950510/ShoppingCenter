/*
 * SellerService.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package service;

import bean.Product;
import com.google.common.collect.ImmutableMap;
import enums.Status;
import mapper.SellerMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;


@Service
public class SellerService {
    @Autowired
    SellerMapper sellerMapper;

    @Autowired
    BuyerService buyerService;

    public Map<String, Object> getProductList() {
        return buyerService.displayAllProduct();
    }

    public Product getProductDetail(int productId) {
        Product target = sellerMapper.selectProductById(productId);
        if(null == target) {
            throw new NoSuchElementException("No Such Product");
        }
        return target;
    }

    public void deleteProduct(int productId) {
        sellerMapper.deleteProductById(productId);
    }

    public Product addNewProduct(Product newProduct) {
        newProduct.setCreated(new Date(System.currentTimeMillis()));
        newProduct.setNumber(7);
        newProduct.setStatus(Status.ONSELL);
        int productId = sellerMapper.insertNewProduct(newProduct);
        return newProduct;
    }

    public Map<String, Object> updateProduct(Product curProduct) {
        if(curProduct.getProductId() == null) {
            throw new IllegalArgumentException("No product Id");
        }
        sellerMapper.updateExistProduct(curProduct);
        Product target = getProductDetail(curProduct.getProductId());
        return ImmutableMap.of("product", (Object)target);
    }
}
