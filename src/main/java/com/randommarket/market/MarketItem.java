package com.randommarket.market;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MarketItem {
    private final Material material;
    private final int amount;
    private double price;
    private final String displayName;

    public MarketItem(Material material, int amount, double price, String displayName) {
        this.material = material;
        this.amount = amount;
        this.price = price;
        this.displayName = displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack toItemStack() {
        return new ItemStack(material, amount);
    }
}

