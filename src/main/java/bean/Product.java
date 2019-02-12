/*
 * Product.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"status"})
public class Product {
    private Integer productId;
    private String caption;
    private String picture;
    private String abstractText;
    private Double price;
    private Date boughtTime;
    private Integer number;         // seller sell's total number
    private Status status;
    private String description;
    private Date created;
    private Date lastModified;
    public void setStatusName(String statusName) {
        this.status = Status.valuesOf(statusName);
    }
    public String getStatusName() {
        return this.status == null ? Status.ONSELL.getValue() : this.status.getValue();
    }
}
