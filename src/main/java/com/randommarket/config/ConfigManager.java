package com.randommarket.config;

import com.randommarket.RandomMarketPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConfigManager {

    private final RandomMarketPlugin plugin;
    private FileConfiguration config;
    private String prefix;
    private int updateInterval;
    private double minPrice;
    private double maxPrice;
    private List<ItemConfig> availableItems;

    public ConfigManager(RandomMarketPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();

        prefix = colorize(config.getString("prefix", "&6[&eRandomMarket&6] &r"));
        updateInterval = config.getInt("market.update-interval", 5);
        minPrice = config.getDouble("market.price.min", 10.0);
        maxPrice = config.getDouble("market.price.max", 1000.0);

        availableItems = new ArrayList<>();
        if (config.isList("market.items")) {
            List<?> itemsList = config.getList("market.items");
            if (itemsList != null) {
                for (Object itemObj : itemsList) {
                    if (itemObj instanceof java.util.Map) {
                        @SuppressWarnings("unchecked")
                        java.util.Map<String, Object> itemMap = (java.util.Map<String, Object>) itemObj;
                        try {
                            String materialStr = (String) itemMap.get("material");
                            if (materialStr == null) continue;
                            
                            Material material = Material.valueOf(materialStr);
                            String displayName = colorize((String) itemMap.getOrDefault("display-name", material.name()));
                            int minAmount = ((Number) itemMap.getOrDefault("min-amount", 1)).intValue();
                            int maxAmount = ((Number) itemMap.getOrDefault("max-amount", 1)).intValue();
                            double basePrice = ((Number) itemMap.getOrDefault("base-price", 10.0)).doubleValue();

                            availableItems.add(new ItemConfig(material, displayName, minAmount, maxAmount, basePrice));
                        } catch (IllegalArgumentException e) {
                            plugin.getLogger().warning("Geçersiz material: " + itemMap.get("material"));
                        } catch (Exception e) {
                            plugin.getLogger().warning("Eşya yüklenirken hata: " + e.getMessage());
                        }
                    }
                }
            }
        }

        if (availableItems.isEmpty()) {
            plugin.getLogger().warning("Config'de hiç eşya bulunamadı! Varsayılan eşyalar kullanılıyor.");
            availableItems.add(new ItemConfig(Material.DIAMOND, "&bElmas", 1, 5, 100.0));
            availableItems.add(new ItemConfig(Material.EMERALD, "&aZümrüt", 1, 10, 50.0));
            availableItems.add(new ItemConfig(Material.GOLD_INGOT, "&6Altın Külçe", 5, 20, 30.0));
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public List<ItemConfig> getAvailableItems() {
        return availableItems;
    }

    public ItemConfig getRandomItem(Random random) {
        if (availableItems.isEmpty()) {
            return null;
        }
        return availableItems.get(random.nextInt(availableItems.size()));
    }

    private String colorize(String text) {
        return text.replace("&", "§");
    }

    public static class ItemConfig {
        private final Material material;
        private final String displayName;
        private final int minAmount;
        private final int maxAmount;
        private final double basePrice;

        public ItemConfig(Material material, String displayName, int minAmount, int maxAmount, double basePrice) {
            this.material = material;
            this.displayName = displayName;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.basePrice = basePrice;
        }

        public Material getMaterial() {
            return material;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getMinAmount() {
            return minAmount;
        }

        public int getMaxAmount() {
            return maxAmount;
        }

        public double getBasePrice() {
            return basePrice;
        }
    }
}

