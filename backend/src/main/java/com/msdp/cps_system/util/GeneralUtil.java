package com.msdp.cps_system.util;

public class GeneralUtil {
    public static Double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    public static String formatPercentage(int value) {
        return (value >= 0 ? "+" : "") + value + "%";
    }
}
