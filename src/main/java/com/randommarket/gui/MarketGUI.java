package com.randommarket.gui;

import com.randommarket.RandomMarketPlugin;
import com.randommarket.market.MarketItem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MarketGUI implements Listener {

    private static final String GUI_TITLE = "§6§lRandom Market";
    private static final int GUI_SIZE = 27;

    public static void openGUI(Player player) {
        RandomMarketPlugin plugin = RandomMarketPlugin.getInstance();
        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, GUI_TITLE);

        // Boş slotları cam ile doldur
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < GUI_SIZE; i++) {
            gui.setItem(i, glass);
        }

        // Market eşyalarını yerleştir (10, 13, 16 slotlarına)
        List<MarketItem> items = plugin.getMarketManager().getMarketItems();
        int[] slots = {10, 13, 16};

        for (int i = 0; i < items.size() && i < slots.length; i++) {
            MarketItem marketItem = items.get(i);
            ItemStack item = marketItem.toItemStack();
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(marketItem.getDisplayName());
                List<String> lore = new ArrayList<>();
                lore.add("§7─────────────────");
                lore.add("§eFiyat: §6" + marketItem.getPrice() + " §7" + plugin.getEconomy().currencyNamePlural());
                lore.add("§7─────────────────");
                lore.add("§aSol tıkla satın al!");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            gui.setItem(slots[i], item);
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(GUI_TITLE)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        // Cam panelleri kontrol et
        if (clickedItem.getType() == Material.GRAY_STAINED_GLASS_PANE) {
            return;
        }

        RandomMarketPlugin plugin = RandomMarketPlugin.getInstance();
        Economy economy = plugin.getEconomy();

        // Tıklanan eşyayı bul
        Material clickedMaterial = clickedItem.getType();
        MarketItem marketItem = null;

        for (MarketItem item : plugin.getMarketManager().getMarketItems()) {
            if (item.getMaterial() == clickedMaterial) {
                marketItem = item;
                break;
            }
        }

        if (marketItem == null) {
            return;
        }

        // Para kontrolü
        double price = marketItem.getPrice();
        String prefix = plugin.getConfigManager().getPrefix();
        
        if (!economy.has(player, price)) {
            player.sendMessage(prefix + "§cYeterli paranız yok! Gerekli: §6" + price + " §7" + economy.currencyNamePlural());
            player.closeInventory();
            return;
        }

        // Para çek
        economy.withdrawPlayer(player, price);

        // Eşyayı ver
        ItemStack itemToGive = marketItem.toItemStack();
        player.getInventory().addItem(itemToGive);

        player.sendMessage(prefix + "§a" + marketItem.getDisplayName() + " §7satın aldınız! Fiyat: §6" + price + " §7" + economy.currencyNamePlural());
        player.closeInventory();
    }
}

