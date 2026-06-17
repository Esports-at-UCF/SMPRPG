package xyz.devvydont.smprpg.hooks

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.Flags
import com.sk89q.worldguard.protection.flags.StateFlag
import org.bukkit.Location

class WorldGuardHook {

    companion object {
        fun isLocationBreakable(loc: Location): Boolean {
            val container = WorldGuard.getInstance().platform.regionContainer
            val query = container.createQuery()
            val regionSet = query.getApplicableRegions(BukkitAdapter.adapt(loc))
            for (region in regionSet) {
                if (region.getFlag(Flags.BLOCK_BREAK) == StateFlag.State.DENY) {
                    return false
                }
            }
            return true
        }
    }
}