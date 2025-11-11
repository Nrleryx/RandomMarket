package com.randommarket.commands;

import com.randommarket.RandomMarketPlugin;
import com.randommarket.gui.MarketGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RandomMarketCommand implements CommandExecutor {

    private final RandomMarketPlugin plugin;

    public RandomMarketCommand(RandomMarketPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = plugin.getConfigManager().getPrefix();
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "§cBu komut sadece oyuncular tarafından kullanılabilir!");
            return true;
        }

        Player player = (Player) sender;
        MarketGUI.openGUI(player);
        return true;
    }
}

