

package controller;

import bean.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import exception.DisplayInformationException;
import exception.ObjectNotFoundException;
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
import service.BuyerService;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/show")
public class ShoppingController {
    @Autowired
    BuyerService buyerService;

    Logger logger = LoggerFactory.getLogger(ShoppingController.class);

    /**
     * show all product avaliable
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, Object> showAllProduct() {
        return buyerService.displayAllProduct();
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Object> showProductDetail(@RequestParam final Integer productId) {
        return buyerService.showProductDetail(productId);
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    @RequiresRoles(value = { "buyer" }, logical = Logical.OR)
    public void addToShoppingCart(@RequestBody final Map<String, Object> requestMap) throws
            IOException {
        final Product curProduct = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(
                requestMap.get("product")), Product.class);
        final Integer boughtNumber = Integer.parseInt(String.valueOf(requestMap.get("number")));
        try {
            buyerService.addToShoppingCart(curProduct, boughtNumber);
        } catch (final ObjectNotFoundException e) {
//            response.setStatus(406);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/shoppingcart", method = RequestMethod.GET)
    @RequiresRoles(value = { "buyer" }, logical = Logical.OR)
    public Map<String, Object> showShoppingCart() throws IOException {
        try {
            return buyerService.displayShoppingCart();
        } catch (final ObjectNotFoundException e) {
            return ImmutableMap.of("shoppingcart", (Object) "empty");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/owned", method = RequestMethod.GET)
    @RequiresRoles(value = { "buyer" }, logical = Logical.OR)
    public Map<String, Object> showOwnedProduct() throws IOException {
        try {
            return buyerService.displayOwnedProduct();
        } catch (final ObjectNotFoundException e) {
            return ImmutableMap.of("owned", (Object) "empty");
        }
    }


    @ResponseBody
    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    @RequiresRoles(value = { "buyer" }, logical = Logical.OR)
    public Map<String, Object> finishTransaction(@RequestBody final Map<String, Integer> shoppingList)
            throws IOException {
        try {
            return buyerService.finishTransaction(shoppingList);
        } catch (final DisplayInformationException e) {
            return ImmutableMap.of("status", (Object) "failed");
        } catch (final ObjectNotFoundException e) {
            return ImmutableMap.of("status", (Object) "failed");
        }
    }
}
