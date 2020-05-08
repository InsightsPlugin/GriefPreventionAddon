package net.frankheijden.insights.addon.griefprevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.frankheijden.insights.entities.CacheAssistant;
import net.frankheijden.insights.entities.Selection;
import org.bukkit.Location;

public class GriefPreventionAssistant extends CacheAssistant {

    public GriefPreventionAssistant() {
        super("GriefPrevention", "claim");
    }

    public Selection adapt(Claim claim) {
        if (claim == null) return null;
        Location min = claim.getLesserBoundaryCorner();
        min.setY(0);
        Location max = claim.getGreaterBoundaryCorner();
        max.setY(max.getWorld().getMaxHeight() - 1);
        return new Selection(min, max);
    }

    @Override
    public Selection getSelection(Location location) {
        return adapt(GriefPrevention.instance.dataStore.getClaimAt(location, false, null));
    }
}
