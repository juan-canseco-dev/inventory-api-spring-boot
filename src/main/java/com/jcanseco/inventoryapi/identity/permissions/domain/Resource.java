package com.jcanseco.inventoryapi.identity.permissions.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;





@Data
@AllArgsConstructor
public class Resource {
    private int order;
    private String name;
    private List<Action> actions;
}





