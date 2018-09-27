package com.github.shynixn.astraledit.bukkit.logic.business.dependencies;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2017 Shynixn
 * <p>
 * Do not remove this header!
 * <p>
 * Version 1.0
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2017
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public enum DependencySupport {
    WORLDEDIT("WorldEdit", "x", true);

    private final String pluginName;
    private final String version;
    private final boolean required;

    /**
     * Initializes a new dependencySupport
     *
     * @param pluginName pluginName
     * @param version    version
     * @param required   required
     */
    DependencySupport(String pluginName, String version, boolean required) {
        this.pluginName = pluginName;
        this.version = version;
        this.required = required;
    }

    /**
     * Returns if the dependency is required
     *
     * @return required
     */
    public boolean isRequired() {
        return this.required;
    }

    /**
     * Returns the plugin
     *
     * @param <T> plugin
     * @return plugin
     */
    public <T extends Plugin> T getPlugin() {
        return (T) Bukkit.getPluginManager().getPlugin(this.pluginName);
    }

    /**
     * Returns the name of the plugin
     *
     * @return name
     */
    public String getPluginName() {
        return this.pluginName;
    }

    /**
     * Returns the required version of the plugin
     *
     * @return version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns if the plugin is installed
     *
     * @return installed
     */
    public boolean isInstalled() {
        return this.getPlugin() != null;
    }

    /**
     * Returns if the required dependencies are not installed on the server
     *
     * @param pluginName pluginName
     * @param prefix     prefix
     * @return isSupported
     */
    public static boolean areRequiredDependenciesInstalled(String pluginName, String prefix) {
        final List<DependencySupport> supports = getRequiredNotInstalledDependencies();
        if (!supports.isEmpty()) {
            Bukkit.getServer().getConsoleSender().sendMessage(prefix + ChatColor.RED + "================================================");
            Bukkit.getServer().getConsoleSender().sendMessage(prefix + ChatColor.RED + pluginName + " failed to load dependencies");
            Bukkit.getServer().getConsoleSender().sendMessage(prefix + ChatColor.RED + "Missing dependencies: " + toDataString(supports));
            Bukkit.getServer().getConsoleSender().sendMessage(prefix + ChatColor.RED + "Plugin gets now disabled!");
            Bukkit.getServer().getConsoleSender().sendMessage(prefix + ChatColor.RED + "================================================");
            return false;
        }
        return true;
    }

    /**
     * Returns a list of required and not installed dependencies
     *
     * @return list
     */
    private static List<DependencySupport> getRequiredNotInstalledDependencies() {
        final List<DependencySupport> data = new ArrayList<>();
        for (final DependencySupport dependencySupport : DependencySupport.values()) {
            if (dependencySupport.required && !dependencySupport.isInstalled()) {
                data.add(dependencySupport);
            }
        }
        return data;
    }

    /**
     * Converts the list to a text
     *
     * @param dependencySupports list
     * @return text
     */
    private static String toDataString(List<DependencySupport> dependencySupports) {
        final StringBuilder builder = new StringBuilder();
        for (final DependencySupport support : dependencySupports) {
            if (builder.length() == 0) {
                builder.append('[').append(support.getPluginName()).append(']');
            } else {
                builder.append(", [").append(support.getPluginName()).append(']');
            }
        }
        return builder.toString();
    }
}
