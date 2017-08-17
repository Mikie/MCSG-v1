package me.smiileyface.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.smiileyface.mcsg.Core;

public class InventoryMenu implements Listener {

	private String inventoryTitle;
    private int inventoryRows;
    private Inventory inventory;

    public InventoryMenu(String title, int rows) {
        this.inventoryTitle = ChatColor.translateAlternateColorCodes('&', title);
        this.inventoryRows = rows * 9;

        this.inventory = Bukkit.createInventory(null, this.getRows(), this.getTitle());
        
        Core.get().getServer().getPluginManager().registerEvents(this, Core.get());
    }

    /**
     * Get the title of the inventory
     *
     * @return Inventory title
     */
    public String getTitle() {
        return this.inventoryTitle;
    }

    /**
     * Get the rows of the inventory
     *
     * @return Inventory rows
     */
    public int getRows() {
        return inventoryRows;
    }

    /**
     * Get the inventory
     *
     * @return inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Add an item to the inventory
     *
     * @param itemStack ItemStack to add to inventory
     * @return this
     */
    public InventoryMenu addItem(ItemStack itemStack) {
        getInventory().addItem(itemStack);
        return this;
    }

    /**
     * Set an item in the inventory
     *
     * @param slot      ItemStack slot to add to inventory
     * @param itemStack ItemStack to add to inventory
     * @return this
     */
    public InventoryMenu setItem(int slot, ItemStack itemStack) {
        getInventory().setItem(slot, itemStack);
        return this;
    }

    /**
     * Open the inventory for a player
     *
     * @param player Player to open inventory for
     */
    public void open(Player player) {
        player.openInventory(getInventory());
    }
}
