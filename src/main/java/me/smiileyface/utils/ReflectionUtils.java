package me.smiileyface.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class ReflectionUtils {

	private static String VERSION;
    static {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String mcVersion = name.substring(name.lastIndexOf('.') + 1);
        VERSION = mcVersion + ".";
    }

    /**
     * Get a class in NMS
     *
     * @param tar Name of class
     * @return Class in NMS
     */
    public static Class<?> getNMSClass(String tar) {
        String className = "net.minecraft.server." + VERSION + tar;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return c;
    }

    /**
     * Get a class in craft bukkit via reflection
     *
     * @param tar Name of class
     * @return Class in NMS
     */
    public static Class<?> getCraftClass(String tar) {
        String className = "org.bukkit.craftbukkit." + VERSION + tar;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return c;
    }

    /**
     * Get a method from a class
     *
     * @param clazz Class to get method from
     * @param method Method name to return
     * @return Method from class with name
     */
    public static Method getMethod(Class<?> clazz, String method) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }

        return null;
    }

    /**
     * Get a method with explicit parameters
     *
     * @param clazz Class to get method from
     * @param method Method name to return
     * @param params Parameters for method
     * @return Method from class
     */
    public static Method getMethod(Class<?> clazz, String method, Class[] params) {
        try {
            return clazz.getMethod(method, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get a declared method from a class
     *
     * @param clazz Class to get method from
     * @param method Method name to return
     * @return Declared method from class with name
     */
    public static Method getDeclaredMethod(Class<?> clazz, String method) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }

        return null;
    }

    /**
     * Get a declared method with explicit parameters
     *
     * @param clazz Class to get method from
     * @param method Method name to return
     * @param params Parameters for method
     * @return Declared method from class
     */
    public static Method getDeclaredMethod(Class<?> clazz, String method, Class[] params) {
        try {
            return clazz.getDeclaredMethod(method, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get a declared field from the class
     *
     * @param clazz Class to get method from
     * @param field_name Field name to return
     * @return Field from class with name
     */
    public static Field getDeclaredField(Class<?> clazz, String field_name) {
        try {
            return clazz.getDeclaredField(field_name);
        } catch (SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get a field from the class
     *
     * @param clazz Class to get method from
     * @param field_name Field name to return
     * @return Field from class with name
     */
    public static Field getField(Class<?> clazz, String field_name) {
        try {
            return clazz.getField(field_name);
        } catch (SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Set the value of a field in a class
     *
     * @param instance Instance of object to set
     * @param fieldName Field name to set
     * @param value Value to set on field
     */
    public static void setValue(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
            field.setAccessible(!field.isAccessible());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a packet via reflection
     *
     * @param player Player to send packet to
     * @param packet Packet to send
     */
    public static void sendPacket(Player player, Object packet)  {
        try {
            Object nmsPlayer = getHandle(player);
            Field con_field = getField(nmsPlayer.getClass(), "playerConnection");
            Object con = con_field.get(nmsPlayer);
            Method packet_method = getMethod(con.getClass(), "sendPacket");
            packet_method.invoke(con, packet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the NMS handle of an entity
     *
     * @param entity Entity to get handle from
     * @return Entity's NMS handle
     */
    public static Object getHandle(Object entity) {
        Object nms_entity = null;
        Method entity_getHandle = getMethod(entity.getClass() , "getHandle");
        try {
            nms_entity = entity_getHandle.invoke(entity);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return nms_entity;
    }
}