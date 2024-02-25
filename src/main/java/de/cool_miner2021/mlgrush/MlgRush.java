package de.cool_miner2021.mlgrush;

import org.bukkit.plugin.java.JavaPlugin;

public final class MlgRush extends JavaPlugin {

    public static String prefix = "§l[§b§lMLG §d§lRUSH§r§l] ";

    @Override
    public void onEnable() {

        getLogger().info("MLG Rush Plugin aktiviert!");

        getCommand("mlg").setExecutor(new MlgCommand());

        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MLG Rush Plugin deaktiviert!");
    }
}
