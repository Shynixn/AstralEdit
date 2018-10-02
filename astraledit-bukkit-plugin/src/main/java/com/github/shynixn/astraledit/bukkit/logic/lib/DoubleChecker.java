package com.github.shynixn.astraledit.bukkit.logic.lib;

public class DoubleChecker {
    /**
     * Checks if the string can be parsed to double
     *
     * @param value value
     * @return success
     */
    public boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (final NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if multiple string values can be parsed to doubles
     * @param values
     * @return
     */
    public boolean areDoubles(String... values){
        for (String string :
                values) {
            if (isDouble(string) == false){
                return false;
            }
        }

        return true;
    }
}
