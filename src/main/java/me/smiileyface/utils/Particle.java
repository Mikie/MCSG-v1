package me.smiileyface.utils;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public enum Particle {

	HUGE_EXPLOSION("hugeexplosion"),
    LARGE_EXPLODE("largeexplode"),
    FIREWORKS_SPARK("fireworksSpark"),
    BUBBLE("bubble"),
    SUSPEND("suspend"),
    DEPTH_SUSPEND("depthSuspend"),
    TOWN_AURA("townaura"),
    CRIT("crit"),
    MAGIC_CRIT("magicCrit"),
    SMOKE("smoke"),
    MOB_SPELL("mobSpell"),
    MOB_SPELL_AMBIENT("mobSpellAmbient"),
    SPELL("spell"),
    INSTANT_SPELL("instantSpell"),
    WITCH_MAGIC("witchMagic"),
    NOTE("note"),
    PORTAL("portal"),
    ENCHANTMENT_TABLE("enchantmenttable"),
    EXPLODE("explode"),
    FLAME("flame"),
    LAVA("lava"),
    FOOTSTEP("footstep"),
    SPLASH("splash"),
    WAKE("wake"),
    LARGE_SMOKE("largesmoke"),
    CLOUD("cloud"),
    RED_DUST("reddust"),
    SNOWBALL_POOF("snowballpoof"),
    DRIP_WATER("dripWater"),
    DRIP_LAVA("dripLava"),
    SNOW_SHOVEL("snowshovel"),
    SLIME("slime"),
    HEART("heart"),
    ANGRY_VILLAGER("angryVillager"),
    HAPPY_VILLAGER("happyVillager");

    private String c;

    Particle(String c) {
        this.c = c;
    }

    /**
     * Get the particle packet via reflection
     *
     * @param name   Name of particle
     * @param x      X Coord
     * @param y      Y Coord
     * @param z      Z Coord
     * @param offX   Offset X
     * @param offY   Offset Y
     * @param offZ   Offset Z
     * @param speed  Speed for particles to play
     * @param amount Amount of particles to play
     * @return Particle packet
     */
    public Object getPacket(String name, float x, float y, float z, float offX, float offY, float offZ, float speed, int amount) {
        try {
            return ReflectionUtils.getNMSClass("PacketPlayOutWorldParticles").getConstructor(new Class[]{String.class, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE}).newInstance(new Object[]{name, x, y, z, offX, offY, offZ, speed, amount});
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Play the effect for a single player at a location
     *
     * @param player   Player to play effect for
     * @param location Location to play effect at
     */
    public void play(Player player, Location location) {
        ReflectionUtils.sendPacket(player, getPacket(this.c, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0F, 0F, 0F, 1F, 10));
    }

    /**
     * Play the effect for a all players at a location
     *
     * @param location Location to play effect at
     */
    public void play(Location location) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player, location);
        }
    }

    /**
     * Play the effect for a single player at a location with an offset
     *
     * @param player   Player to play effect for
     * @param location Location to play effect at
     * @param offSet   Offset for the packet
     */
    public void play(Player player, Location location, float offSet) {
        ReflectionUtils.sendPacket(player, getPacket(this.c, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offSet, offSet, offSet, 1F, 10));
    }

    /**
     * Play the effect for all players at a location with an offset
     *
     * @param location Location to play effect at
     * @param offSet   Offset for the packet
     */
    public void play(Location location, float offSet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player, location, offSet);
        }
    }

    /**
     * Play the effect for a single player at a location with an edited amount of particles
     *
     * @param player   Player to play effect for
     * @param location Location to play effect at
     * @param amount   Amount of particles to play
     */
    public void play(Player player, Location location, int amount) {
        ReflectionUtils.sendPacket(player, getPacket(this.c, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0F, 0F, 0F, 1F, amount));
    }

    /**
     * Play the effect for all players at a location with an edited amount of particles
     *
     * @param location Location to play effect at
     * @param amount   Amount of particles to play
     */
    public void play(Location location, int amount) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player, location, amount);
        }
    }

    /**
     * Play the effect for a player at a location with an offset and an edited amount of particles
     *
     * @param player   Player to play effect for
     * @param location Location to play effect at
     * @param offSet   Offset for the packet
     * @param amount   Amount of particles to play
     */
    public void play(Player player, Location location, float offSet, int amount) {
        ReflectionUtils.sendPacket(player, getPacket(this.c, (float) location.getX(), (float) location.getY(), (float) location.getZ(), offSet, offSet, offSet, 1F, amount));
    }

    /**
     * Play the effect for all players at a location with an offset and an edited amount of particles
     *
     * @param location Location to play effect at
     * @param offSet   Offset for the packet
     * @param amount   Amount of particles to play
     */
    public void play(Location location, float offSet, int amount) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player, location, offSet, amount);
        }
    }
}
