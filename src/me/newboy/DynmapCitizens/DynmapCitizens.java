package me.newboy.DynmapCitizens;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

public final class DynmapCitizens extends JavaPlugin
  implements Listener
{
  private static MarkerSet ms;
  private static MarkerAPI mar;
  private static DynmapAPI dyn;

  public void onEnable()
  {
    getLogger().info(getDescription().getVersion() + " has been enabled.");
    getServer().getPluginManager().registerEvents(this, this);
    reloadConfig();
    saveConfig();
    getConfig().options().copyDefaults(true);
    saveConfig();

    if (!getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
      getLogger().warning("Citizens is not enabled,disabling plugin.");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    if (!getServer().getPluginManager().getPlugin("dynmap").isEnabled()) {
      getLogger().warning("Dynmap is not enabled,disabling plugin.");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    dyn = (DynmapAPI)getServer().getPluginManager().getPlugin("dynmap");
    mar = dyn.getMarkerAPI();
    ms = mar.getMarkerSet("citizens2-npc");
    if (ms == null) ms = mar.createMarkerSet("citizens2-npc", "NPCS", null, false);

    ms.setLayerPriority(getConfig().getInt("layer-priority", 10));
    ms.setLabelShow(Boolean.valueOf(getConfig().getBoolean("show-label", false)));
    ms.setDefaultMarkerIcon(mar.getMarkerIcon(getConfig().getString("marker-icon", "offlineuser")));
    int minzoom = getConfig().getInt("min-zoom", 0);
    if (minzoom > 0) ms.setMinZoom(0);
    ms.setHideByDefault(false);

    getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
    {
      public void run() {
        if (DynmapCitizens.dyn == null) DynmapCitizens.dyn = (DynmapAPI)Bukkit.getServer().getPluginManager().getPlugin("dynmap");

        if (DynmapCitizens.mar == null) DynmapCitizens.mar = DynmapCitizens.dyn.getMarkerAPI();

        if (DynmapCitizens.ms == null) {
          DynmapCitizens.ms = DynmapCitizens.mar.getMarkerSet("citizens2-npc");
          if (DynmapCitizens.ms == null) DynmapCitizens.ms = DynmapCitizens.mar.createMarkerSet("citizens2-npc", "NPCS", null, false);
        }

        boolean found = false;
        for (Marker m : DynmapCitizens.ms.getMarkers()) {
          if (m != null) {
            found = false;
            for (NPC npcc : CitizensAPI.getNPCRegistry()) {
              if ((npcc != null) && 
                (npcc.getFullName().equalsIgnoreCase(m.getLabel()))) found = true;
            }

            if (!found) m.deleteMarker();
          }
        }
        label486: for (NPC npc : CitizensAPI.getNPCRegistry())
          if ((npc != null) && (npc.getBukkitEntity() != null) && (npc.getFullName() != null) && (npc.getBukkitEntity().getLocation() != null) && (DynmapCitizens.ms != null) && (DynmapCitizens.mar != null)) {
            found = false;
            Marker m;
            for (??? = DynmapCitizens.ms.getMarkers().iterator(); ???.hasNext(); 
              m.setLocation("citizens2-npc-" + npc.getFullName(), npc.getBukkitEntity().getLocation().getX(), npc.getBukkitEntity().getLocation().getY(), npc.getBukkitEntity().getLocation().getZ()))
            {
              m = (Marker)???.next();
              if ((m == null) || 
                (!npc.getFullName().equalsIgnoreCase(m.getLabel()))) break label486;
              found = true;
              if ((m.getX() == npc.getBukkitEntity().getLocation().getX()) && (m.getY() == npc.getBukkitEntity().getLocation().getY()) && (m.getZ() == npc.getBukkitEntity().getLocation().getZ())) {
                break label486;
              }
            }
            if (!found) DynmapCitizens.ms.createMarker("citizens2-npc-" + npc.getFullName(), npc.getFullName(), npc.getBukkitEntity().getWorld().getName(), npc.getBukkitEntity().getLocation().getX(), npc.getBukkitEntity().getLocation().getY(), npc.getBukkitEntity().getLocation().getZ(), DynmapCitizens.mar.getMarkerIcon("offlineuser"), false);
          }
      }
    }
    , 100L, 100L);
  }

  public void onDisable()
  {
    getLogger().info(getDescription().getVersion() + " has been disabled.");
  }
}

/* Location:           C:\Users\Ryan\Downloads\DynmapCitizens.jar
 * Qualified Name:     me.newboy.DynmapCitizens.DynmapCitizens
 * JD-Core Version:    0.6.2
 */