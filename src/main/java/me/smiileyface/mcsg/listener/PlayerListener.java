package me.smiileyface.mcsg.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;

import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.game.GameState;
import me.smiileyface.mcsg.game.phase.GamePhase;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;

public class PlayerListener implements Listener {

	private Game g = Core.get().getGame();

	@EventHandler
	public void onInventroyClose(InventoryCloseEvent e) {
		if (e.getInventory().getType() == InventoryType.CHEST) {
			Inventory chest = e.getInventory();
			Player p = (Player) e.getPlayer();
			GamePlayer gp = GamePlayer.getPlayer(p.getDisplayName());
			if (gp != null) {
				p.getLocation().getWorld().playSound(p.getLocation(), Sound.CHEST_CLOSE, 1.0F, 1.0F);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();

		if (p.getKiller() instanceof Player) {
			Player pkiller = p.getKiller();

			if (g.getPlayers().contains(p)) {
				GamePlayer player = GamePlayer.getPlayer(p.getDisplayName());
				e.setDeathMessage(null);

				GamePhase gp = g.getGamePhase();
				
				for (Entity entity : p.getWorld().getEntities()) {
					if (entity instanceof Projectile) {
						Projectile pr = (Projectile) entity;
						if (p.equals(pr.getShooter())) {
							pr.remove();
						}
					}
				}
				
				if(g.getPlayers().contains(pkiller)) {
					gp.killPlayer(player, GamePlayer.getPlayer(pkiller.getDisplayName()), false);
				} else {
					gp.killPlayer(player, null, false);
				}
			}
		} else {
			if(g.getPlayers().contains(p)) {
				GamePlayer player = GamePlayer.getPlayer(p.getDisplayName());
				e.setDeathMessage(null);
				
				GamePhase gp = g.getGamePhase();
				
				gp.killPlayer(player, null, false);
			}
		}
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onHangingDestroy(HangingBreakEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player p = event.getPlayer();
		
		if(g.getPlayers().contains(GamePlayer.getPlayer(p.getDisplayName()))) {
			if(event.getCause() == TeleportCause.END_PORTAL || event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.NETHER_PORTAL) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Location from = e.getFrom();
		Location to = e.getTo();
		
		if(from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
			Player p = e.getPlayer();
			if(g.getPlayers().contains(GamePlayer.getPlayer(p.getDisplayName()))) {
				if(g.getState() == GameState.STARTING) {
					p.teleport(from);
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		GamePlayer player = GamePlayer.createPlayer(e.getPlayer());
		Player p = player.getPlayer();
		if (g.getState() != GameState.LOBBY) {
			g.getSpectators().add(player);
			player.setInSpecChat(true);
			for (Player p2 : Bukkit.getOnlinePlayers()) {
				p2.hidePlayer(p);
			}
			player.clear();
			p.setAllowFlight(true);
			p.setFlying(true);
			p.teleport(Core.get().getGame().getPlayers().get(0).getPlayer().getLocation());
		} else if (g.getState() == GameState.LOBBY) {
			g.getPlayers().add(player);
			e.getPlayer().teleport(Core.get().getGame().getLobbySpawn());
		}
		player.clear();

		e.setJoinMessage(ChatUtils.colorize("&8(&e" + player.getPoints() + "&8)" + player.getRank().getPrefix()
				+ player.getRealName() + " &ehas joined the server."));
		g.checkForStart();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if (g.getSpectators().contains(e.getPlayer())) {
			g.getSpectators().remove(e.getPlayer());
		} else {
			g.getGamePhase().killPlayer(GamePlayer.getPlayer(e.getPlayer().getDisplayName()), null, true);
			g.getPlayers().remove(e.getPlayer());
			if (g.getState() == GameState.IN_GAME || g.getState() == GameState.DEATHMATCH) {
				if (g.getPlayers().size() < 2) {
					GamePlayer winner = g.getPlayers().get(0);

					winner.addWin();
					winner.addGamePlayed();

					Bukkit.broadcastMessage(
							ChatUtils.modulate("SurvivalGames", "Tribute " + winner.getRealName() + " has won!"));

					g.end();
				}
			}
			g.checkCancelStart();
		}
		GamePlayer.removePlayer(e.getPlayer().getDisplayName());
	}
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		e.setCancelled(true);
	}
}
