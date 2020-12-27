package net.frankheijden.insights.addon.griefprevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.frankheijden.insights.entities.Area;
import net.frankheijden.insights.entities.CacheAssistant;
import net.frankheijden.insights.entities.CuboidSelection;
import org.bukkit.Location;

import java.util.Collections;

public class GriefPreventionAssistant extends CacheAssistant {

    public GriefPreventionAssistant() {
        super("GriefPrevention", "GriefPrevention", "claim", "v1.1.0");
    }

    public String getId(Claim claim) {
        return getPluginName() + "@" + claim.getID();
    }

    public Area adapt(Claim claim) {
        if (claim == null) return null;
        Location min = claim.getLesserBoundaryCorner();
        min.setY(0);
        Location max = claim.getGreaterBoundaryCorner();
        max.setY(max.getWorld().getMaxHeight() - 1);
        return Area.from(this, getId(claim), Collections.singletonList(new CuboidSelection(min, max)));
    }

    @Override
    public Area getArea(Location location) {
        return adapt(GriefPrevention.instance.dataStore.getClaimAt(location, false, null));
    }
}
