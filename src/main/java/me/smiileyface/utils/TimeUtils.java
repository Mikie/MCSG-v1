package me.smiileyface.utils;

import java.text.SimpleDateFormat;

public class TimeUtils {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a - MMM d, yyyy");

    public static String formatTime(int secs) {
        int remainder = secs % 86400;

        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = (remainder / 60) - (hours * 60);
        int seconds = (remainder % 3600) - (minutes * 60);

        if (days > 0) {
            return days + "d" + hours + "h" + minutes + "m" + seconds + "s";
        } else if (hours > 0) {
            return hours + "h" + minutes + "m" + seconds + "s";
        } else if (minutes > 0) {
            return minutes + "m" + seconds + "s";
        } else {
            return String.valueOf(seconds + "s");
        }
    }
    
    public static long getTime(long amount, String unit) {
        try {
            if (unit.equalsIgnoreCase("seconds")) {
                return amount * 1000L;
            } else if (unit.equalsIgnoreCase("minutes")) {
                return amount * 1000L * 60L;
            } else if (unit.equalsIgnoreCase("hours")) {
                return amount * 1000L * 60L * 60L;
            } else if (unit.equalsIgnoreCase("days")) {
                return amount * 1000L * 60L * 60L * 24L;
            } else if (unit.equalsIgnoreCase("months")) {
                return amount * 1000L * 60L * 60L * 24L * 31L;
            } else if (unit.equalsIgnoreCase("years")) {
                return amount * 1000L * 60L * 60L * 24L * 365L;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0L;
    }
    
    public static String simpleFormat(int secs) {
    	int remainder = secs % 86400;

        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = (remainder / 60) - (hours * 60);
        int seconds = (remainder % 3600) - (minutes * 60);

        if (days > 0) {
            return days + ":" + hours + ":" + minutes + ":" + seconds;
        } else if (hours > 0) {
            return hours + ":" + minutes + ":" + seconds;
        } else if (minutes > 0) {
            return minutes + ":" + seconds;
        } else {
            return String.valueOf(seconds);
        }
    }
}
