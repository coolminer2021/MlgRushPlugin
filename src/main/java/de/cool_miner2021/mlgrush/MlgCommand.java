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

        if (waitingPlayer == null) {
            waitingPlayer = player;
            player.sendMessage("Warte auf den zweiten Spieler...");

        } else {
            startGame(waitingPlayer, player);
            waitingPlayer = null;
        }

        return true;
    }

    public static void giveItemsToPlayer(Player p) {

        ItemStack knockbackStick = new ItemStack(Material.BLAZE_ROD);
        knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK,2);


        ItemStack netheritePickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        netheritePickaxe.addEnchantment(Enchantment.DIG_SPEED, 5);


        ItemStack glass = new ItemStack(Material.GLASS, 64);


        PlayerInventory inventory = p.getInventory();
        inventory.setItem(0, knockbackStick);
        inventory.setItem(1, netheritePickaxe);
        inventory.setItem(2, glass);
        inventory.setItem(3, glass);

    }

    private void startGame(Player player1, Player player2) {
        World world = Bukkit.getWorld("world");
        if (world != null) {
            TeamManager teamManager = TeamManager.getInstance();
            teamManager.reset();
            teamManager.assignPlayerToTeam(player1,"Red");
            teamManager.assignPlayerToTeam(player2,"Blue");


            player1.teleport(new Location(player1.getWorld(),0.5,-57,-22.5).setDirection(new Vector(0,0,1)));
            player2.teleport(new Location(player2.getWorld(),0.5,-57,22.5).setDirection(new Vector(0,0,-1)));
            player1.sendMessage(MlgRush.prefix+"Das Spiel beginnt, du bist §cROT");
            player2.sendMessage(MlgRush.prefix+"Das Spiel beginnt, du bist §9BLAU");

            giveItemsToPlayer(player1);
            giveItemsToPlayer(player2);

            player1.setPlayerListName("§c"+player1.getName());
            player1.setPlayerListName("§9"+player1.getName());

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
