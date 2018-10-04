package com.github.shynixn.astraledit.bukkit.logic.business.service;

import com.github.shynixn.astraledit.api.bukkit.business.entity.service.UpdateService;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class UpdateServiceImpl implements UpdateService {

    private static final String BASE_URL = "https://api.spigotmc.org/legacy/update.php?resource=";
    private final AstralEditPlugin plugin;

    public UpdateServiceImpl(AstralEditPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<Boolean> checkForUpdates() {
        try {
            boolean isUpToDate = plugin.getDescription().getVersion().equals(getLatestVersion());
            String prefix = AstralEditPlugin.PREFIX_CONSOLE;
            String pluginName = AstralEditPlugin.PLUGIN_NAME;

            if (!isUpToDate) {
                if (plugin.getDescription().getVersion().endsWith("SNAPSHOT")) {
                    Bukkit.getServer().getConsoleSender()
                          .sendMessage(prefix + ChatColor.YELLOW + "================================================"
                                        + prefix + ChatColor.YELLOW + "You are using a snapshot of " + pluginName
                                        + prefix + ChatColor.YELLOW + "Please check regularly if there is a new version"
                                        + prefix + ChatColor.YELLOW + "================================================");
                } else {
                    Bukkit.getServer().getConsoleSender()
                          .sendMessage(prefix + ChatColor.YELLOW + "================================================"
                                        + prefix + ChatColor.YELLOW +  pluginName + " is outdated"
                                        + prefix + ChatColor.YELLOW + "Please download the latest version from spigotmc.org"
                                        + prefix + ChatColor.YELLOW + "=====================================================x");
                }
            }

            return CompletableFuture.completedFuture(true);
        } catch (IOException e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    private String getLatestVersion() throws IOException {
        InputStream inputStream = new URL(BASE_URL + AstralEditPlugin.SPIGOT_RESOURCEID).openStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = bufferedReader.readLine();

        inputStream.close();
        inputStreamReader.close();
        bufferedReader.close();

        return line;
    }
}
