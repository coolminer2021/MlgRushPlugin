package de.cool_miner2021.mlgrush;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class MlgCommand implements CommandExecutor {
    private Player waitingPlayer = null;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Du musst ein Spieler sein, um diesen Befehl zu verwenden!");
            return true;
        }

        TeamManager teamManager = TeamManager.getInstance();

        if (teamManager.playerTeams.isEmpty()){
            if (waitingPlayer == null) {
                waitingPlayer = player;
                player.sendMessage(MlgRush.prefix+"Warte auf den zweiten Spieler...");

            } else {
                startGame(waitingPlayer, player);
                waitingPlayer = null;
            }
        }else {
            player.sendMessage(MlgRush.prefix+"§cEs läuft bereits eine Runde!");
        }
        return true;
    }

    public static void giveItemsToPlayer(Player p) {
        ItemStack knockbackStick = new ItemStack(Material.BLAZE_ROD);
        knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK,2);

        ItemStack netheritePickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        netheritePickaxe.addEnchantment(Enchantment.DIG_SPEED, 4);

        ItemStack sandstone = new ItemStack(Material.SANDSTONE, 64);

        PlayerInventory inventory = p.getInventory();
        inventory.setItem(0, knockbackStick);
        inventory.setItem(1, netheritePickaxe);
        inventory.setItem(2, sandstone);
        inventory.setItem(3, sandstone);

    }

    public void setupPlayer(Player p){
        switch (TeamManager.getInstance().getPlayerTeam(p)){
            case "Red" -> {
                p.teleport(new Location(p.getWorld(),0.5,-57,-22.5).setDirection(new Vector(0,0,1)));
                p.setPlayerListName("§c"+p.getName());
                p.sendMessage(MlgRush.prefix+"Das Spiel beginnt, du bist §cROT");
            }
            case "Blue" -> {
                p.teleport(new Location(p.getWorld(),0.5,-57,22.5).setDirection(new Vector(0,0,-1)));
                p.setPlayerListName("§9"+p.getName());
                p.sendMessage(MlgRush.prefix+"Das Spiel beginnt, du bist §9BLAU");
            }
        }
        p.setGameMode(GameMode.SURVIVAL);
        giveItemsToPlayer(p);
    }

    private void startGame(Player player1, Player player2) {
        World world = Bukkit.getWorld("world");
        if (world != null) {
            TeamManager teamManager = TeamManager.getInstance();
            teamManager.reset();
            teamManager.assignPlayerToTeam(player1,"Red");
            teamManager.assignPlayerToTeam(player2,"Blue");

            setupPlayer(player1);
            setupPlayer(player2);

            world.setDifficulty(Difficulty.PEACEFUL);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.DO_MOB_SPAWNING,false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
            world.setTime(6000);


        } else {
            player1.sendMessage(MlgRush.prefix+"Fehler beim Starten des Spiels!");
            player2.sendMessage(MlgRush.prefix+"Fehler beim Starten des Spiels!");
        }
    }

}
