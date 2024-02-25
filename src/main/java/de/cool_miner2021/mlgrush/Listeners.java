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
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Listeners implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        BreakBlock task = new BreakBlock(e.getBlock());
        task.runTaskLater(MlgRush.getPlugin(MlgRush.class),100);
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();

        if (block.getType() == Material.BLUE_BED || block.getType() == Material.GLASS || block.getType() == Material.RED_BED) {
            if (block.getType() == Material.BLUE_BED || block.getType() == Material.RED_BED) {
                switch (block.getType()){
                    case BLUE_BED -> {
                        if (!TeamManager.getInstance().getPlayerTeam(p).equals("Blue")){
                            TeamManager.getInstance().addTeamPoint("Red");
                            resetPlayers();

                        }
                    }
                    case RED_BED -> {
                        if (!TeamManager.getInstance().getPlayerTeam(p).equals("Red")){
                            TeamManager.getInstance().addTeamPoint("Blue");
                            resetPlayers();

                        }
                    }
                }
                e.setCancelled(true);
            } else {
                block.breakNaturally();
            }
        } else {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        e.setDeathMessage("");
        e.getDrops().clear();
    }

    public void resetPlayers(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SURVIVAL) {
                player.sendMessage(MlgRush.prefix+"§c"+TeamManager.getInstance().getTeamPoints("Red")+"§7:§9"+TeamManager.getInstance().getTeamPoints("Blue"));
                player.setHealth(0);
                player.setPlayerListName(player.getName());
            }
        }
        if(TeamManager.getInstance().getTeamPoints("Blue")==10){
            for (Player player:Bukkit.getOnlinePlayers()){
                player.sendTitle("§9Blau hat gewonnen","gg");
            }
        }
        if(TeamManager.getInstance().getTeamPoints("Red")==10){
            for (Player player:Bukkit.getOnlinePlayers()){
                player.sendTitle("§cRot hat gewonnen","gg");
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        TeamManager teamManager = TeamManager.getInstance();
        if (teamManager.getPlayerTeam(p).equals("Red")){
            e.setRespawnLocation(new Location(p.getWorld(),0.5,-57,-22.5).setDirection(new Vector(0,0,1)));
        } else{
            e.setRespawnLocation(new Location(p.getWorld(),0.5,-57,22.5).setDirection(new Vector(0,0,-1)));
        }
        MlgCommand.giveItemsToPlayer(p);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        double y = player.getLocation().getY();
        if (y < -71) {
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
