package com.techta.databaseapp2;

public class CustomerModel {

    private int id;
    private String name;
    private String purchasedGoods;
    private boolean isActive;
    private boolean isSelected = false;

    public CustomerModel(int id, String name, String purchasedGoods, boolean isActive) {
        this.id = id;
        this.name = name;
        this.purchasedGoods = purchasedGoods;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "CustomerModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", purchasedGoods='" + purchasedGoods + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    public String getPurchasedGoods() {
        return purchasedGoods;
    }

    public void setPurchasedGoods(String purchasedGoods) {
        this.purchasedGoods = purchasedGoods;
    }
}
