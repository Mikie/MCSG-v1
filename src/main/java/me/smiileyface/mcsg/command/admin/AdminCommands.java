package me.smiileyface.mcsg.command.admin;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.smiileyface.backend.players.Rank;
import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.Chests;
import me.smiileyface.mcsg.game.GameState;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;

public class AdminCommands implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sg")) {
			if ((sender instanceof Player)) {
				Player p = (Player) sender;
				if (args.length == 0) {
					if (GamePlayer.getPlayer(p.getDisplayName()).getRank().has(p, Rank.ADMIN, true)) {
						p.sendMessage(ChatUtils.colorize(
								"&6SurvivalGames Help \n &6/sg disable &a- Disables the game. Used for when setting maps, or editing something. \n&6/sg enable &a- Enable the game after it's disabled. \n&6/sg setlobbyspawn &a- Set the spawn point of the lobby. \n&6/sg refillchests &a- Restocks the chests. \n&6/sg forcedm &a- Sets the time to the minimum players time. \n&6/sg map create <mapname> &a- Create a map. \n&6/sg map addspawn <mapname> &a- Add a spawn to a specific map. \n&6/sg map addtier2 <mapname> &a- Add a tier2 chest to a specific map. \n&6/sg map adddmspawn <mapname> &a- Add a spawn the deathmatch spawn of a specific map. \n&6/sg usercommands &a- List of the commands meant for users."));
					}
				} else if(args.length == 1) {
					if((args[0].equalsIgnoreCase("setlobbyspawn")) && (GamePlayer.getPlayer(p.getDisplayName()).getRank().has(p, Rank.ADMIN, true))) {
						String locationSerialized = p.getWorld().getName() + "=" + p.getLocation().getX() + "=" + p.getLocation().getY() + "=" + p.getLocation().getZ() + "=" + p.getLocation().getPitch() + "=" + p.getLocation().getYaw();
			            Core.getConfigFile().setPath("lobby-spawn", locationSerialized);
			            p.sendMessage(ChatUtils.modulate("SurvivalGames", "Successfully set lobby spawn."));
					}
					if(args[0].equalsIgnoreCase("usercommands")) {
						p.sendMessage(ChatUtils.colorize("&6SurvivalGames User Commands \n&6/vote &a-Opens the voting GUI \n&6/stats (<player>) &a-Check your/other stats. \n&6/resetstats &a- Reset your stats \n&6/info &a- Shows info about game and server.")); 
					}
					if((args[0].equalsIgnoreCase("disable")) && (GamePlayer.getPlayer(p.getDisplayName()).getRank().has(p, Rank.ADMIN, true))) {
						if(!Core.get().getGame().getState().equals(GameState.DEAD)) {
							Core.get().getGame().setState(GameState.DEAD);
							Bukkit.getScheduler().cancelAllTasks();
							p.sendMessage(ChatUtils.modulate("SurvivalGames", "The game has been disabled."));
						} else {
							p.sendMessage(ChatUtils.modulate("SurvivalGames", "The game is already disabled."));
						}
					}
					if((args[0].equalsIgnoreCase("enable")) && (GamePlayer.getPlayer(p.getDisplayName()).getRank().has(p, Rank.ADMIN, true))) {
						if(!Core.get().getGame().getState().equals(GameState.DEAD)) {
							p.sendMessage(ChatUtils.modulate("SurvivalGames", "The game is already enabled."));
						} else {
							p.sendMessage(ChatUtils.modulate("SurvivalGames", "The server is restarting..."));
							for(Player player : Bukkit.getOnlinePlayers()) {
								player.kickPlayer("Server restarting...");
							}
							
							Bukkit.getScheduler().runTaskLater(Core.get(), new Runnable() {
								
								@Override
								public void run() {
									Bukkit.shutdown();
								}
							}, 20 * 10);
						}
					}
					if((args[0].equalsIgnoreCase("refillchests")) && (GamePlayer.getPlayer(p.getDisplayName()).getRank().has(p, Rank.ADMIN, true))) {
						Chests.c.refill();
						Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "The chests have been refilled!"));
					}
					if((args[0].equalsIgnoreCase("forcedm")) && (GamePlayer.getPlayer(p.getDisplayName()).getRank().has(p, Rank.ADMIN, true))) {
						Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "Deathmatch has been forced by " + p.getName()));
						Bukkit.getScheduler().runTaskTimer(Core.get(), new Runnable() {
							int timer = 30;
							@Override
							public void run() {
								if(timer % 10 == 0 && timer > 10) {
									Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "The final deathmatch will start in " + timer + " seconds."));
								} else if(timer <= 10 && timer > 0) {
									Bukkit.broadcastMessage(ChatUtils.modulate("SurvivalGames", "The final deathmatch will start in " + timer + " seconds."));
								} else if(timer == 0) {
									Core.get().getGame().startDeathmatch();
								}
							}
							
						}, 20L, 20L);
					}
				} else if(args.length == 2) {
					if((args[0].equalsIgnoreCase("reload")) && (GamePlayer.getPlayer(p.getDisplayName()).getRank().has(p, Rank.ADMIN, true))) {
						if(args[1].equalsIgnoreCase("c")) {
							Core.getConfigFile().save();
						}
					}
				} else if((args.length == 3) && (args[0].equalsIgnoreCase("map") && (GamePlayer.getPlayer(p.getDisplayName()).getRank().has(p, Rank.ADMIN, true)))) {
					if((args[1].equalsIgnoreCase("create"))) {
						String mapName = args[2];
						for(int i = 0; i < 999; i++) {
							if(Core.getMaps().contains(String.valueOf(i))) {
								if(Core.getMaps().contains(String.valueOf(i) + "." + mapName)) {
									p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cThis map name already exists."));
									break;
								}
							} else {
								Core.getMaps().createPath(String.valueOf(i), "");
								
								Core.getMaps().createPath(String.valueOf(i) + ".name", mapName);
				                Core.getMaps().createStringList(String.valueOf(i) + ".spawns", new String[0]);
				                Core.getMaps().createStringList(String.valueOf(i) + ".tier2", new String[0]);
				                Core.getMaps().createPath(String.valueOf(i) + ".spec-spawn", "world=0.0=4.0=0.0=0.0=0.0");
				                Core.getMaps().createStringList(String.valueOf(i) + ".break-whitelist", new String[] { "LEAVES", "RED_MUSHROOM", "BROWN_MUSHROOM", "WEB", "TNT", "FIRE" });
				                Core.getMaps().createStringList(String.valueOf(i) + ".place-whitelist", new String[] { "LEAVES", "RED_MUSHROOM", "BROWN_MUSHROOM", "WEB", "TNT", "FIRE" });
				                Core.getMaps().createStringList(String.valueOf(i) + ".deathmatch-spawns", new String[0]);
				                Core.getMaps().createPath(String.valueOf(i) + ".deathmatch-spec-spawn", "world=1=1=1=1=1");
				                p.sendMessage(ChatUtils.modulate("SurvivalGames", "Successfully created arena " + mapName));
				                break;
							}
						}
					}
					if(args[1].equalsIgnoreCase("addspawn")) {
						int selectedMap = getMapByName(args[2]);
						if(selectedMap != 69420) {
							String locationSerialized = p.getWorld().getName() + "=" + p.getLocation().getX() + "=" + p.getLocation().getY() + "=" + p.getLocation().getZ();
							if(Core.getMaps().getList(selectedMap + ".spawns").size() < 24) {
								Core.getMaps().getList(selectedMap + ".spawns").add(locationSerialized);
								Core.getMaps().save();
								p.sendMessage(ChatUtils.modulate("SurvivalGames", "Added spawn #" + Core.getMaps().getList(selectedMap + ".spawns").size() + " to " + Core.getMaps().getString(selectedMap + ".name")));
							} else {
								p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cThe maximum amount of spawns has been reached."));
							}
						} else {
							p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cThe arena " + args[2] + " can not be found."));
						}
					}
					if(args[1].equalsIgnoreCase("addtier2")) {
						Set<Material> tmpSet = null;
						Location chest = p.getTargetBlock(tmpSet, 100).getLocation();
						if(chest.getBlock().getType() == Material.CHEST) {
							int map = getMapByName(args[2]);
							if(getMapByName(args[2]) != 69420) {
								if(!Core.getMaps().contains("" + map)) {
									p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cThis map does not exist!"));
									p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cUse /sg map create <name>"));
								} else {
									Core.getMaps().getList(map + ".tier2").add(chest.getWorld().getName() + "=" + chest.getX() + "=" + chest.getY() + "=" + chest.getZ());
									Core.getMaps().save();
									p.sendMessage(ChatUtils.modulate("SurvivalGames", "Added Tier 2 chest to " + args[2]));
								}
							} else {
								p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cThe map " + args[2] + " can not be found."));
							}
						} else {
							p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cThis is not a chest. Hover your cursor over a chest."));
						}
					}
					if(args[1].equalsIgnoreCase("adddmspawn")) {
						if(getMapByName(args[2]) != 69420) {
							String loc = Core.get().getGame().serializeLocation(p.getLocation());
							Core.getMaps().getList(getMapByName(args[2]) + ".deathmatch-spawns").add(loc);
							Core.getMaps().save();
							p.sendMessage(ChatUtils.modulate("SurvivalGames","Added a deathmatch spawn to the map " + args[2]));
						} else {
							p.sendMessage(ChatUtils.modulate("SurvivalGames","&cThe map " + args[2] + " cannot be found."));
						}
					}
				} else {
					p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cUnrecognized arguments."));
				}
			} else {
				sender.sendMessage(ChatUtils.modulate("SurvivalGames", "&cOnly players can use Survival Games commands."));
			}
		}
		return false;
	}
	
	public static int getMapByName(String name) {
		for (int i = 0; i < 999; i++) {
			if ((Core.getMaps().contains(String.valueOf(i)))
					&& (Core.getMaps().getString(i + ".name").equals(name))) {
				return i;
			}
		}
		return 69420;
	}
}
