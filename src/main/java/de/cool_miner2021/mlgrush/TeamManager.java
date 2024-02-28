package de.cool_miner2021.mlgrush;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {
    private static TeamManager instance;
    public Map<Player, String> playerTeams;
    private Map<String, Integer> teamPoints;

    private TeamManager() {
        playerTeams = new HashMap<>();
        teamPoints = new HashMap<>();
    }

    public static TeamManager getInstance() {
        if (instance == null) {
            instance = new TeamManager();
        }
        return instance;
    }

    public void assignPlayerToTeam(Player player, String teamName) {
        playerTeams.put(player, teamName);
    }

    public String getPlayerTeam(Player player) {
        return playerTeams.get(player);
    }

    public void addTeamPoint(String teamName) {
        int currentPoints = teamPoints.getOrDefault(teamName, 0);
        teamPoints.put(teamName, currentPoints + 1);
    }

    public int getTeamPoints(String teamName) {
        return teamPoints.getOrDefault(teamName, 0);
    }
    public void reset(){
        teamPoints=new HashMap<>();
        playerTeams=new HashMap<>();
    }

}
