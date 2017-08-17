package me.smiileyface.mcsg.listener;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.Game;
import me.smiileyface.mcsg.game.GameState;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;

public class SpectatorListener implements Listener {
	
	private Game g = Core.get().getGame();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		GamePlayer p = GamePlayer.getPlayer(event.getPlayer().getDisplayName());
		
		if(p.isInSpecChat()) {
			event.setCancelled(true);
			HashMap<Integer, GamePlayer> gPlayers = new HashMap<Integer, GamePlayer>();
			int i = 0;
			for(GamePlayer player : g.getPlayers()) {
				gPlayers.put(i++, player);
			}
			if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
				p.getPlayer().teleport(gPlayers.get(p.getSpecIndex() + 1).getPlayer().getLocation());
				p.setSpecIndex(p.getSpecIndex() + 1);
				p.getPlayer().sendMessage(ChatUtils.modulate("SurvivalGames", "&3You are now spectating &8(&e" + gPlayers.get(p.getSpawnIndex()).getPoints() + "&8)" + gPlayers.get(p.getSpawnIndex()).getRank().getPrefix() + gPlayers.get(p.getSpawnIndex()).getRealName()));
			} else if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				p.getPlayer().teleport(gPlayers.get(p.getSpecIndex() - 1).getPlayer().getLocation());
				p.setSpecIndex(p.getSpecIndex() - 1);
				p.getPlayer().sendMessage(ChatUtils.modulate("SurvivalGames", "&3You are now spectating &8(&e" + gPlayers.get(p.getSpawnIndex()).getPoints() + "&8)" + gPlayers.get(p.getSpawnIndex()).getRank().getPrefix() + gPlayers.get(p.getSpawnIndex()).getRealName()));
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (GamePlayer.getPlayer(event.getPlayer().getDisplayName()).isInSpecChat() || g.getState() == GameState.LOBBY || g.getState() == GameState.STARTING || g.getState() == GameState.ENDING) 
			event.setCancelled(true);
		if(g.getPlayers().contains(GamePlayer.getPlayer(event.getPlayer().getDisplayName()))) {
			if(event.getBlock().getType() != Material.LEAVES || event.getBlock().getType() != Material.LONG_GRASS || event.getBlock().getType() != Material.BROWN_MUSHROOM || event.getBlock().getType() != Material.RED_MUSHROOM)
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		if (GamePlayer.getPlayer(event.getPlayer().getDisplayName()).isInSpecChat() || g.getState() == GameState.LOBBY || g.getState() == GameState.STARTING || g.getState() == GameState.ENDING) 
			event.setCancelled(true);
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (GamePlayer.getPlayer(event.getPlayer().getDisplayName()).isInSpecChat() || g.getState() == GameState.LOBBY || g.getState() == GameState.STARTING || g.getState() == GameState.ENDING)
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (GamePlayer.getPlayer(event.getPlayer().getDisplayName()).isInSpecChat() || g.getState() == GameState.LOBBY || g.getState() == GameState.STARTING || g.getState() == GameState.ENDING) 
			event.setCancelled(true);
	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player) {
			Player p = (Player) event.getTarget();
			if (GamePlayer.getPlayer(p.getDisplayName()).isInSpecChat() || g.getState() == GameState.LOBBY || g.getState() == GameState.STARTING || g.getState() == GameState.ENDING) 
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onExpChangeEvent(PlayerExpChangeEvent event) {
		if (GamePlayer.getPlayer(event.getPlayer().getDisplayName()).isInSpecChat())
			event.setAmount(0);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		Player damager = null;
		if (event.getDamager() instanceof Player) {
			damager = (Player) event.getDamager();
		} else if (event.getDamager() instanceof Projectile) {
			Projectile pro = (Projectile) event.getDamager();
			if (pro.getShooter() instanceof Player) {
				damager = (Player) pro.getShooter();
			}
		}

		if (damager != null) {
			if (GamePlayer.getPlayer(damager.getDisplayName()).isInSpecChat() || g.getState() == GameState.LOBBY || g.getState() == GameState.STARTING || g.getState() == GameState.ENDING) 
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (GamePlayer.getPlayer(p.getDisplayName()).isInSpecChat() || g.getState() == GameState.LOBBY || g.getState() == GameState.STARTING || g.getState() == GameState.ENDING) 
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (GamePlayer.getPlayer(p.getDisplayName()).isInSpecChat() || g.getState() == GameState.LOBBY || g.getState() == GameState.STARTING || g.getState() == GameState.ENDING) 
				event.setCancelled(true);
		}
	}
}
