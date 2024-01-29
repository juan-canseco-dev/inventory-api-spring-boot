package com.jcanseco.inventoryapi.security.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class Resource {
    private int order;
    private String name;
    private List<Action> actions;
}
