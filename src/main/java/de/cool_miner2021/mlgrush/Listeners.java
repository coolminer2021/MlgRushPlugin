package de.cool_miner2021.mlgrush;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Listeners implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE){
            BreakBlock task = new BreakBlock(e.getBlock());
            task.runTaskLater(MlgRush.getPlugin(MlgRush.class),200);
        }
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();

        switch (block.getType()){
            case BLUE_BED -> {
                if (!TeamManager.getInstance().getPlayerTeam(p).equals("Blue")){
                    TeamManager.getInstance().addTeamPoint("Red");
                    resetPlayers();
                }
                e.setCancelled(true);
            }
            case RED_BED -> {
                if (!TeamManager.getInstance().getPlayerTeam(p).equals("Red")){
                    TeamManager.getInstance().addTeamPoint("Blue");
                    resetPlayers();
                }
                e.setCancelled(true);
            }
            case SANDSTONE -> e.setCancelled(false);
            default -> e.setCancelled(true);
        }
        e.setDropItems(false);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        e.setDeathMessage("");
        e.getDrops().clear();
    }

    public void resetPlayers(){
        TeamManager teamManager = TeamManager.getInstance();
        int redPoints = teamManager.getTeamPoints("Red");
        int bluePoints = teamManager.getTeamPoints("Blue");

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SURVIVAL) {
                player.sendMessage(MlgRush.prefix+"§c"+TeamManager.getInstance().getTeamPoints("Red")+"§7:§9"+TeamManager.getInstance().getTeamPoints("Blue"));
                player.setHealth(0);
            }
        }

        if (bluePoints == 5 || redPoints == 5) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle("§" + (bluePoints == 5 ? "9Blau" : "cRot") + " hat gewonnen", "§c" + redPoints + "§7:§9" + bluePoints);
                player.setGameMode(GameMode.SPECTATOR);
                player.setPlayerListName(player.getName());
            }
            teamManager.reset();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        e.setJoinMessage("");
        e.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        TeamManager teamManager = TeamManager.getInstance();

        switch (teamManager.getPlayerTeam(p)){
            case "Red" -> e.setRespawnLocation(new Location(p.getWorld(),0.5,-57,-22.5).setDirection(new Vector(0,0,1)));
            case "Blue" -> e.setRespawnLocation(new Location(p.getWorld(),0.5,-57,22.5).setDirection(new Vector(0,0,-1)));
        }

        MlgCommand.giveItemsToPlayer(p);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        double y = player.getLocation().getY();
        if (y < -70) {
            player.setHealth(0);
        }
    }
    private static class BreakBlock extends BukkitRunnable {
        private final Block block;

        public BreakBlock(Block block) {
            this.block = block;
        }

        @Override
        public void run() {
            block.setType(Material.AIR);
        }
    }
}
