package de.cool_miner2021.mlgrush;

import org.bukkit.plugin.java.JavaPlugin;

public final class MlgRush extends JavaPlugin {

    public static String prefix = "§l[§b§lMLG §d§lRUSH§r§l] ";

    @Override
    public void onEnable() {
        getCommand("mlg").setExecutor(new MlgCommand());
        getCommand("reset").setExecutor(new ResetCmd());

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        getLogger().info("MLG Rush Plugin aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MLG Rush Plugin deaktiviert!");
    }
}
