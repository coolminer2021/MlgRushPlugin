package de.cool_miner2021.mlgrush;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResetCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        TeamManager.getInstance().reset();
        for (Player player: Bukkit.getOnlinePlayers()) player.setGameMode(GameMode.SPECTATOR);
        commandSender.sendMessage(MlgRush.prefix+"§aErfolgreich zurückgesetzt");
        return false;
    }
}
