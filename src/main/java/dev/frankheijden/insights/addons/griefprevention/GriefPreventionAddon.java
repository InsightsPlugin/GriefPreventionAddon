package dev.frankheijden.insights.addons.griefprevention;

import dev.frankheijden.insights.api.InsightsPlugin;
import dev.frankheijden.insights.api.addons.InsightsAddon;
import dev.frankheijden.insights.api.addons.Region;
import dev.frankheijden.insights.api.addons.SimpleCuboidRegion;
import dev.frankheijden.insights.api.objects.math.Vector3;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimModifiedEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.Optional;

public class GriefPreventionAddon implements InsightsAddon, Listener {

    public String getId(Claim claim) {
        return getPluginName() + "@" + claim.getID();
    }

    public Optional<Region> adapt(Claim claim) {
        if (claim == null) return Optional.empty();
        Location min = claim.getLesserBoundaryCorner();
        Location max = claim.getGreaterBoundaryCorner();
        return Optional.of(new SimpleCuboidRegion(
                min.getWorld(),
                new Vector3(min.getBlockX(), min.getWorld().getMinHeight(), min.getBlockZ()),
                new Vector3(max.getBlockX(), max.getWorld().getMaxHeight() - 1, max.getBlockZ()),
                getPluginName(),
                getId(claim)
        ));
    }

    @Override
    public String getPluginName() {
        return "GriefPrevention";
    }

    @Override
    public String getAreaName() {
        return "claim";
    }

    @Override
    public String getVersion() {
        return "{version}";
    }

    @Override
    public Optional<Region> getRegion(Location location) {
        return adapt(GriefPrevention.instance.dataStore.getClaimAt(location, false, null));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onClaimModified(ClaimModifiedEvent event) {
        deleteClaimCache(event.getFrom());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onClaimDeleted(ClaimDeletedEvent event) {
        deleteClaimCache(event.getClaim());
    }

    private void deleteClaimCache(Claim claim) {
        InsightsPlugin.getInstance().getAddonStorage().remove(getId(claim));
    }
}
