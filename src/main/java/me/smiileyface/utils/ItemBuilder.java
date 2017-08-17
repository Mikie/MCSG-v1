package me.smiileyface.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.smiileyface.mcsg.Core;

public class ItemBuilder {

	private static Map<ItemStack, Callback<Player>> callbacks = new HashMap<>();

    static {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void interact(PlayerInteractEvent event) {
                ItemStack item = event.getItem();

                if (!event.hasItem())
                    return;

                Optional<Map.Entry<ItemStack, Callback<Player>>> optional = callbacks
                        .entrySet()
                        .stream()
                        .filter((is) -> is.getKey().equals(item))
                        .findFirst();

                if (optional.isPresent()) {
                    event.setCancelled(true);
                    optional.get().getValue().call(event.getPlayer());
                }
            }
        }, Core.get());
    }

    private ItemStack building;
    private Callback<Player> callback;

    private ItemBuilder() {
        building = new ItemStack(Material.AIR);
    }

    private ItemBuilder(ItemStack building) {
        this.building = building;

        Optional<Map.Entry<ItemStack, Callback<Player>>> optional = callbacks
                .entrySet()
                .stream()
                .filter((is) -> is.getKey().equals(building))
                .findFirst();

        if (optional.isPresent()) {
            callback = optional.get().getValue();
        }
    }

    public static ItemBuilder create() {
        return new ItemBuilder();
    }

    public static ItemBuilder create(ItemStack stack) {
        return new ItemBuilder(stack);
    }

    public ItemBuilder material(Material mat) {
        building.setType(mat);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = building.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        building.setItemMeta(meta);

        return this;
    }

    public ItemBuilder data(byte data) {
        building.setDurability(data);
        return this;
    }

    public ItemMeta meta() {
        return building.getItemMeta();
    }

    public ItemBuilder lore(String... lore) {
        ItemMeta meta = building.getItemMeta();

        for (int i = 0; i < lore.length; i++) {
            lore[i] = ChatColor.translateAlternateColorCodes('&', lore[i]);
        }

        meta.setLore(Arrays.asList(lore));
        building.setItemMeta(meta);

        return this;
    }

    public ItemBuilder appendLore(String... toAppend) {
        ItemMeta meta = building.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<String>();
        }

        for (String s : toAppend) {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        meta.setLore(lore);
        building.setItemMeta(meta);

        return this;
    }

    public ItemBuilder amount(int amount) {
        building.setAmount(amount);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int lvl) {
        building.addEnchantment(enchantment, lvl);
        return this;
    }

    public ItemBuilder callback(Callback<Player> callback) {
        this.callback = callback;
        return this;
    }

    public Material getType() {
        return building.getType();
    }

    public ItemBuilder building(ItemStack building) {
        this.building = building;
        return this;
    }

    public ItemStack build() {
        if (callback != null)
            callbacks.put(building, callback);

        return building;
    }
}
