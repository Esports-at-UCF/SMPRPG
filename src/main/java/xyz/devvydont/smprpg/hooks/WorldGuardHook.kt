package xyz.devvydont.smprpg.hooks

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.Flags
import com.sk89q.worldguard.protection.flags.StateFlag
import org.bukkit.Location
import org.bukkit.entity.Player

class WorldGuardHook {

    companion object {
        fun isLocationBreakable(loc: Location, player: Player?): Boolean {
            val container = WorldGuard.getInstance().platform.regionContainer
            val query = container.createQuery()
            val regionSet = query.getApplicableRegions(BukkitAdapter.adapt(loc))
            for (region in regionSet) {
                if (player != null) {
                    if (region.owners.contains(player.uniqueId) || region.members.contains(player.uniqueId)) {
                        continue  // We can break in this region, but what if there is an overlapping region that doesnt allow us?
                    }
                }
                if (region.getFlag(Flags.BLOCK_BREAK) == StateFlag.State.DENY) {
                    return false
                }
            }
            return true
        }

        fun isLocationBreakable(loc: Location): Boolean {
            return isLocationBreakable(loc, null)
        }
    }
}