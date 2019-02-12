/*
 * SellerController.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package controller;

import bean.Product;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.SellerService;

import java.util.Map;

@Controller
@RequestMapping(path = "/api/sell")
public class SellerController {
    Logger log = LoggerFactory.getLogger(SellerController.class);
    @Autowired
    SellerService sellerService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @RequiresRoles(value = { "seller" }, logical = Logical.OR)
    public void addNewProduct(@RequestBody Product newProduct) {
        log.warn("newProduct {}", newProduct.getAbstractText());
        sellerService.addNewProduct(newProduct);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> displayAllProduct() {
        return sellerService.getProductList();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> displaySingleProduct(@RequestParam Integer productId) {
        Product target = sellerService.getProductDetail(productId);
        if(target == null) {
            throw new IllegalArgumentException("Illegal productId");
        }
        return ImmutableMap.of("product", (Object)target);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseBody
    @RequiresRoles(value = { "seller" }, logical = Logical.OR)
    public void deleteSingleproduct(@RequestParam Integer productId) {
        sellerService.deleteProduct(productId);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    @RequiresRoles(value = { "seller" }, logical = Logical.OR)
    public Map<String, Object> updateProduct(@RequestBody Product curProduct) {
        return sellerService.updateProduct(curProduct);
    }
}
