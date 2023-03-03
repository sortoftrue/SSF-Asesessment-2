package com.james.SSF.Assessment2.Models;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class Cart {
    
    private String currentChoice;

    @NotNull(message="Required!")
    @Min(value =1, message="You must add at least 1 item")
    private Integer currentQty;

    private Map<String,Integer> itemList = new HashMap<>();
    
    public String getCurrentChoice() {
        return currentChoice;
    }
    public void setCurrentChoice(String currentChoice) {
        this.currentChoice = currentChoice;
    }
    public Integer getCurrentQty() {
        return currentQty;
    }
    public void setCurrentQty(Integer currentQty) {
        this.currentQty = currentQty;
    }
    public Map<String, Integer> getItemList() {
        return itemList;
    }
    public void setItemList(Map<String, Integer> itemList) {
        this.itemList = itemList;
    }

    public void addItem(String item, Integer quantity){
        System.out.println("existing"+itemList.get(item));
        System.out.println("to add"+quantity);
        if(itemList.containsKey(item)){
            itemList.put(item,itemList.get(item)+quantity);
        } else {
            itemList.put(item,quantity);
        }

        System.out.println("existing"+itemList.get(item));
    }
    

}
