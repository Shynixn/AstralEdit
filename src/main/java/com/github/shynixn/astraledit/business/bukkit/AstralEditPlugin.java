package com.github.shynixn.astraledit.business.bukkit;

import com.github.shynixn.astraledit.api.AstralEdit;
import com.github.shynixn.astraledit.business.bukkit.dependencies.DependencySupport;
import com.github.shynixn.astraledit.business.bukkit.nms.VersionSupport;
import com.github.shynixn.astraledit.lib.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

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
public class AstralEditPlugin extends JavaPlugin {
    public static final String PLUGIN_NAME = "AstralEdit";
    public static final String PREFIX_CONSOLE = ChatColor.LIGHT_PURPLE + "[AstralEdit] ";
    public static final String PREFIX = ChatColor.DARK_RED + "" + ChatColor.BOLD + "[" + ChatColor.RED + "" + ChatColor.BOLD + ChatColor.ITALIC + "AE" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "] " + ChatColor.RED;
    public static final String PREFIX_SUCCESS = PREFIX + ChatColor.GREEN;
    public static final String PREFIX_ERROR = PREFIX + ChatColor.RED;

    /**
     * Enables the plugin
     */
    @Override
    public void onEnable() {
        if (!VersionSupport.isServerVersionSupported(PLUGIN_NAME, PREFIX_CONSOLE) || !DependencySupport.areRequiredDependenciesInstalled(PLUGIN_NAME, PREFIX_CONSOLE)) {
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            try {
                Bukkit.getServer().getConsoleSender().sendMessage(PREFIX_CONSOLE + ChatColor.GREEN + "Loading AstralEdit ...");
                this.saveDefaultConfig();
                ReflectionUtils.invokeMethodByClass(AstralEdit.class, "initialize", new Class[]{Plugin.class}, new Object[]{this});
                Bukkit.getServer().getConsoleSender().sendMessage(PREFIX_CONSOLE + ChatColor.GREEN + "Enabled AstralEdit " + this.getDescription().getVersion() + " by Shynixn");
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to initialize plugin.", e);
            }
        }
    }

    /**
     * Disables the plugin
     */
    @Override
    public void onDisable() {
        try {
            ReflectionUtils.invokeMethodByClass(AstralEdit.class, "shutdown", new Class[0], new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to initialize plugin.", e);
        }
    }
}
