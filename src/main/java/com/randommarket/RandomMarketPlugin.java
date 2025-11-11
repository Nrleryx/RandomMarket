package com.randommarket;

import com.randommarket.commands.RandomMarketCommand;
import com.randommarket.config.ConfigManager;
import com.randommarket.gui.MarketGUI;
import com.randommarket.market.MarketManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RandomMarketPlugin extends JavaPlugin {

    private static RandomMarketPlugin instance;
    private Economy economy;
    private ConfigManager configManager;
    private MarketManager marketManager;

    @Override
    public void onEnable() {
        instance = this;

        // Config yükle
        configManager = new ConfigManager(this);

        // Vault kontrolü
        if (!setupEconomy()) {
            getLogger().warning(configManager.getPrefix() + "Vault veya ekonomi eklentisi bulunamadı, ekonomi devre dışı.");
            // Devre dışı bırakmak yerine çalışmaya devam etsin
        }

        // Market Manager'ı başlat
        marketManager = new MarketManager(this);
        marketManager.startPriceUpdateTask();

        // Komutları kaydet
        if (getCommand("randommarket") != null) {
            getCommand("randommarket").setExecutor(new RandomMarketCommand(this));
        }

        // Event listener'ları kaydet
        getServer().getPluginManager().registerEvents(new MarketGUI(), this);

        getLogger().info(configManager.getPrefix() + "Plugin başarıyla yüklendi!");
    }

    @Override
    public void onDisable() {
        if (marketManager != null) {
            marketManager.stopPriceUpdateTask();
        }
        getLogger().info("RandomMarket plugin kapatıldı!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning("Vault bulunamadı, ancak plugin çalışmaya devam ediyor (ekonomi devre dışı).");
            return true; // Vault yoksa bile plugin kapanmasın
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().warning("Ekonomi sağlayıcısı bulunamadı, ekonomi işlemleri devre dışı.");
            return true; // Ekonomi plugin yoksa da kapanmasın
        }

        economy = rsp.getProvider();
        getLogger().info("Vault ekonomisi başarıyla bağlandı: " + economy.getName());
        return true;
    }

    public static RandomMarketPlugin getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return economy;
    }

    public MarketManager getMarketManager() {
        return marketManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}git init
