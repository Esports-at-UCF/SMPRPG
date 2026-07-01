package xyz.devvydont.smprpg.entity.player

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class AccessoryInventory: Iterable<ItemStack> {

    var necklace: ItemStack = AIR
    var cloak: ItemStack = AIR
    var belt: ItemStack = AIR
    var gloves: ItemStack = AIR
    var charm: ItemStack = AIR

    constructor(necklace: ItemStack, cloak: ItemStack, belt: ItemStack, gloves: ItemStack, charm: ItemStack) {
        this.necklace = necklace
        this.cloak = cloak
        this.belt = belt
        this.gloves = gloves
        this.charm = charm
    }

    constructor() {
        this.necklace = AIR
        this.cloak = AIR
        this.belt = AIR
        this.gloves = AIR
        this.charm = AIR
    }

    override fun iterator(): Iterator<ItemStack> {
        return listOf(
            this.necklace,
            this.cloak,
            this.belt,
            this.gloves,
            this.charm
        ).iterator()
    }

    companion object {
        val AIR = ItemStack.of(Material.AIR)
    }
}