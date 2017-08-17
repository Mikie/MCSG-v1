package me.smiileyface.mcsg.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.smiileyface.backend.Stats;
import me.smiileyface.mcsg.Core;
import me.smiileyface.utils.ItemUtil;

public class Chests implements Listener {

	public static Chests c = new Chests();
	private List<Location> opened = new ArrayList<Location>();
	private ItemStack[] tier1 = new ItemStack[Core.getChests().getList("tier1").size()];
	private ItemStack[] tier2 = new ItemStack[Core.getChests().getList("tier2").size()];
	
	public void setTiers() {
		for(int i = 0; i < Core.getChests().getList("tier1").size(); i++) {
			if(ItemUtil.unserializeItemStack((String)Core.getChests().getList("tier1").get(i)).getType() == Material.FLINT_AND_STEEL) {
				ItemStack ist = ItemUtil.unserializeItemStack((String)Core.getChests().getList("tier1").get(i));
				ist.setDurability((short)(64 - Core.getConfigFile().getInt("fns-uses")));
				tier1[1] = ist;
			} else {
				tier1[i] = ItemUtil.unserializeItemStack((String)Core.getChests().getList("tier1").get(i));
			}
		}
		for(int i = 0; i < Core.getChests().getList("tier2").size(); i++) {
			if(ItemUtil.unserializeItemStack((String)Core.getChests().getList("tier2").get(i)).getType() == Material.FLINT_AND_STEEL) {
				ItemStack ist = ItemUtil.unserializeItemStack((String)Core.getChests().getList("tier2").get(i));
				ist.setDurability((short)(64 - Core.getConfigFile().getInt("fns-uses")));
				tier1[1] = ist;
			} else {
				tier1[i] = ItemUtil.unserializeItemStack((String)Core.getChests().getList("tier2").get(i));
			}
		}
	}
	
	public void fillChest(Chest c) {
		c.getInventory().clear();
		
		Location l = c.getLocation();
		String loc = l.getWorld().getName() + "=" + l.getX() + "=" + l.getY() + "=" + l.getZ();
		
		Random r = new Random();
		
		int max = Core.getChests().getInt("max-items");
		
		int a = r.nextInt(max);
		if(a == 0)
			a = 1;
		for(int i = 0; i < a; i++) {
			if(!Core.getMaps().getList(Core.get().getGame().getCurrentMap() + ".tier2").contains(loc)) {
				ItemStack it = tier1[r.nextInt(tier1.length)];
				int slot = r.nextInt(c.getInventory().getSize());
				if(!c.getInventory().contains(it)) {
					c.getInventory().setItem(slot, it);
				} else {
					c.getInventory().addItem(new ItemStack[] { it });
				}
			} else {
				ItemStack it = tier2[r.nextInt(tier2.length)];
				if(Core.getChests().getBoolean("rarer-tier2") == true) {
					if(!c.getInventory().contains(it)) {
						int slot = r.nextInt(c.getInventory().getSize());
						c.getInventory().setItem(slot, it);
					} else {
						boolean k = true;
						while (k) {
							it = tier2[r.nextInt(tier2.length)];
							if(!c.getInventory().contains(it)) {
								k = false;
							}
						}
					}
				} else {
					int slot = r.nextInt(c.getInventory().getSize());
					c.getInventory().setItem(slot, it);
				}
			}
		}
		opened.add(c.getLocation());
	}
	
	public void fillDoubleChest(DoubleChest c) {
		c.getInventory().clear();
		
		Location l = c.getLocation();
		String loc = l.getWorld().getName() + "=" + l.getX() + "=" + l.getY() + "=" + l.getZ();
		
		Random r = new Random();
		
		int max = Core.getChests().getInt("max-items-doublechest");
		
		int a = r.nextInt(max);
		if(a == 0)
			a = 1;
		for(int i = 0; i < a; i++) {
			if(!Core.getMaps().getList(Core.get().getGame().getCurrentMap() + ".tier2").contains(loc)) {
				ItemStack it = tier1[r.nextInt(tier1.length)];
				int slot = r.nextInt(c.getInventory().getSize());
				if(!c.getInventory().contains(it)) {
					c.getInventory().setItem(slot, it);
				} else {
					c.getInventory().addItem(new ItemStack[] { it });
				}
			} else {
				ItemStack it = tier2[r.nextInt(tier2.length)];
				if(Core.getChests().getBoolean("rarer-tier2") == true) {
					if(!c.getInventory().contains(it)) {
						int slot = r.nextInt(c.getInventory().getSize());
						c.getInventory().setItem(slot, it);
					} else {
						boolean k = true;
						while (k) {
							it = tier2[r.nextInt(tier2.length)];
							if(!c.getInventory().contains(it)) {
								k = false;
							}
						}
					}
				} else {
					int slot = r.nextInt(c.getInventory().getSize());
					c.getInventory().setItem(slot, it);
				}
			}
		}
		opened.add(c.getLocation());
	}
	
	public void refill() {
		emptyChests();
	}
	
	public void emptyChests() {
		if(opened.size() != 0) {
			opened.removeAll(opened);
		}
	}
	
	@EventHandler
	public void onOpen(final PlayerInteractEvent e) {
		if((e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock().getType() == Material.CHEST)) {
			if(Core.get().getGame().getState() == GameState.IN_GAME || Core.get().getGame().getState() == GameState.DEATHMATCH) {
				Chest c = (Chest)e.getClickedBlock().getState();
				if(!opened.contains(c.getLocation())) {
					fillChest(c);
					Stats.getChestsOpened().setChestsOpened(e.getPlayer().getUniqueId(), Stats.getChestsOpened().getChestsOpened(e.getPlayer()) + 1);
				}
			}
		}
	}
}
