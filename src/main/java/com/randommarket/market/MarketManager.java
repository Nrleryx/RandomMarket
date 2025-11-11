package com.randommarket.market;

import com.randommarket.RandomMarketPlugin;
import com.randommarket.config.ConfigManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarketManager {

    private final RandomMarketPlugin plugin;
    private final ConfigManager configManager;
    private final List<MarketItem> marketItems; // 3 slot için
    private final Random random;
    private BukkitTask priceUpdateTask;

    public MarketManager(RandomMarketPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.marketItems = new ArrayList<>();
        this.random = new Random();
        initializeMarketItems();
    }

    private void initializeMarketItems() {
        // 3 slot için rastgele eşyalar seç
        updateMarketItems();
    }

    public void startPriceUpdateTask() {
        int intervalMinutes = configManager.getUpdateInterval();
        long intervalTicks = intervalMinutes * 60 * 20; // dakika * saniye * tick
        
        priceUpdateTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateMarketItems();
            }
        }.runTaskTimer(plugin, intervalTicks, intervalTicks);
    }

    public void stopPriceUpdateTask() {
        if (priceUpdateTask != null) {
            priceUpdateTask.cancel();
        }
    }

    private void updateMarketItems() {
        marketItems.clear();
        
        // 3 slot için rastgele eşyalar seç
        for (int i = 0; i < 3; i++) {
            ConfigManager.ItemConfig itemConfig = configManager.getRandomItem(random);
            if (itemConfig != null) {
                // Rastgele miktar seç
                int amount = itemConfig.getMinAmount() + 
                    random.nextInt(itemConfig.getMaxAmount() - itemConfig.getMinAmount() + 1);
                
                // Rastgele fiyat hesapla (base price'un %50-150'i arası)
                double basePrice = itemConfig.getBasePrice();
                double minPrice = basePrice * 0.5;
                double maxPrice = basePrice * 1.5;
                double price = minPrice + (maxPrice - minPrice) * random.nextDouble();
                price = Math.round(price * 100.0) / 100.0;
                
                // Config'deki min-max fiyat aralığına göre sınırla
                if (price < configManager.getMinPrice()) {
                    price = configManager.getMinPrice();
                }
                if (price > configManager.getMaxPrice()) {
                    price = configManager.getMaxPrice();
                }
                
                marketItems.add(new MarketItem(
                    itemConfig.getMaterial(),
                    amount,
                    price,
                    itemConfig.getDisplayName()
                ));
            }
        }
        
        plugin.getLogger().info(configManager.getPrefix() + "§aMarket eşyaları güncellendi!");
    }

    public List<MarketItem> getMarketItems() {
        return marketItems;
    }
}

