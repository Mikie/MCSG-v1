package me.smiileyface.utils;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.util.ChatPaginator;

import me.smiileyface.mcsg.player.GamePlayer;

public class ChatUtils {

	public static final String AQUA = ChatColor.AQUA.toString();
	public static final String CYAN = ChatColor.DARK_AQUA.toString();
	public static final String BLACK = ChatColor.BLACK.toString();
	public static final String DARK_BLUE = ChatColor.DARK_BLUE.toString();
	public static final String DARK_GREEN = ChatColor.DARK_GREEN.toString();
	public static final String DARK_RED = ChatColor.DARK_RED.toString();
	public static final String PURPLE = ChatColor.DARK_PURPLE.toString();
	public static final String GOLD = ChatColor.GOLD.toString();
	public static final String GRAY = ChatColor.GRAY.toString();
	public static final String DARK_GRAY = ChatColor.DARK_GRAY.toString();
	public static final String BLUE = ChatColor.BLUE.toString();
	public static final String GREEN = ChatColor.GREEN.toString();
	public static final String RED = ChatColor.RED.toString();
	public static final String PINK = ChatColor.LIGHT_PURPLE.toString();
	public static final String YELLOW = ChatColor.YELLOW.toString();
	public static final String WHITE = ChatColor.WHITE.toString();
	public static final String MAGIC = ChatColor.MAGIC.toString();
	public static final String BOLD = ChatColor.BOLD.toString();
	public static final String DATA = CYAN + BOLD;
	public static final String STRIKE = ChatColor.STRIKETHROUGH.toString();
	public static final String UNDERLINE = ChatColor.UNDERLINE.toString();
	public static final String ITALIC = ChatColor.ITALIC.toString();
	public static final String RESET = ChatColor.RESET.toString();

	public static final String SEPARATOR = "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
	public static final String RAQUO = "»";

	public static String colorize(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static String stripColors(String canStrip) {
		for (int i = 0; i < canStrip.length(); i++) {
			if (canStrip.charAt(i) == '&') {
				canStrip = canStrip.replaceFirst("&" + canStrip.charAt(i + 1), "");
			}
		}
		return canStrip;
	}

	public static String modulate(String module, String message) {
		return GOLD + "[" + module + "] " + GREEN + colorize(message);
	}

	public static void modulate(GamePlayer player, String module, String... messages) {
		for (String message : messages) {
			player.getPlayer().sendMessage(ChatUtils.modulate(module, colorize(message)));
		}
	}

	public static String center(String text) {
		return StringUtils.center(text, ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH);
	}
}
