

package bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// 能直接标记每个商品是否被购买了吗？展示的时候直接选中bought status的商品？
// 还是得每个用户记录下来已经购买过的，在cart里面的商品

// 反正就一个用户，展示的时候也只展示商品当前的状态，就直接statis就好了，
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"userType", "ownedIdMap","shoppingCartIdMap","boughtPriceMap"})
public class User implements Serializable {
    int userId;
    String userName;
    String password;
    UserType userType;
    String shoppingCart;
    String boughtPrice;
    String owned;

    public void setUserTypeName(String value) {
        this.userType = UserType.valuesOf(value);
    }

    public String getUserTypeName() {
        return this.userType == null ? "" : userType.getValue();
    }
    public Map<Integer, Integer> getShoppingCartIdMap() throws IOException {
        return (new ObjectMapper()).readValue(shoppingCart, new TypeReference<Map<Integer, Integer>>(){});
    }

    public void setShoppingCartIdMap(Map<Integer, Integer> shoppingCartIdList)
            throws JsonProcessingException {
        this.shoppingCart = new ObjectMapper().writeValueAsString(shoppingCartIdList);
    }

    public Map<Integer, Double> getBoughtPriceMap() throws IOException {
        return new ObjectMapper().readValue(this.boughtPrice, new TypeReference<Map<Integer,
                Double>>(){});
    }

    public void setBoughtPriceMap(Map<Integer, Double> boughtPriceMap)
            throws JsonProcessingException {
        this.boughtPrice = new ObjectMapper().writeValueAsString(boughtPriceMap);
    }

    public Map<Integer, Integer> getOwnedIdMap() throws IOException {
        return (new ObjectMapper()).readValue(owned, new TypeReference<Map<Integer, Integer>>(){});
    }
    public void setOwnedIdMap(Map<Integer, Integer> ownedIdList)
            throws JsonProcessingException {
        this.owned = new ObjectMapper().writeValueAsString(ownedIdList);
    }


    /**
     * add a single product ID to ID List
     * @param productId
     * @throws IOException
     */
    public void addSingleProductToShoppingCart(int productId, int boughtNumber, double curPrice)
            throws IOException {
        Map<Integer, Integer> productIdMap = getShoppingCartIdMap();
        Map<Integer, Double> boughtPriceMap = getBoughtPriceMap();
        if(productIdMap.containsKey(productId)) {
            productIdMap.put(productId, productIdMap.get(productId) + boughtNumber);
        } else {
            productIdMap.put(productId, boughtNumber);
        }
        boughtPriceMap.put(productId, curPrice);
        setBoughtPriceMap(boughtPriceMap);
        setShoppingCartIdMap(productIdMap);
    }
}
