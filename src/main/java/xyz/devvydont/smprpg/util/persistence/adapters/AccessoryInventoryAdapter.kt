package xyz.devvydont.smprpg.util.persistence.adapters

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.entity.player.AccessoryInventory

val NECKLACE_KEY = NamespacedKey("smprpg", "necklace")
val CLOAK_KEY = NamespacedKey("smprpg", "cloak")
val BELT_KEY = NamespacedKey("smprpg", "belt")
val GLOVES_KEY = NamespacedKey("smprpg", "gloves")
val CHARM_KEY = NamespacedKey("smprpg", "charm")

class AccessoryInventoryAdapater : PersistentDataType<PersistentDataContainer, AccessoryInventory> {

    override fun getPrimitiveType(): Class<PersistentDataContainer> {
        return PersistentDataContainer::class.java
    }

    override fun getComplexType(): Class<AccessoryInventory> {
        return AccessoryInventory::class.java
    }

    override fun toPrimitive(
        complex: AccessoryInventory,
        context: PersistentDataAdapterContext
    ): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        container.set(NECKLACE_KEY, PersistentDataType.BYTE_ARRAY, serializeItem(complex.necklace))
        container.set(CLOAK_KEY, PersistentDataType.BYTE_ARRAY, serializeItem(complex.cloak))
        container.set(BELT_KEY, PersistentDataType.BYTE_ARRAY, serializeItem(complex.belt))
        container.set(GLOVES_KEY, PersistentDataType.BYTE_ARRAY, serializeItem(complex.gloves))
        container.set(CHARM_KEY, PersistentDataType.BYTE_ARRAY, serializeItem(complex.charm))
        return container
    }

    override fun fromPrimitive(
        primitive: PersistentDataContainer,
        context: PersistentDataAdapterContext
    ): AccessoryInventory {
        return AccessoryInventory(
            resolveItemFromBytes(primitive.get(NECKLACE_KEY, PersistentDataType.BYTE_ARRAY)),
            resolveItemFromBytes(primitive.get(CLOAK_KEY, PersistentDataType.BYTE_ARRAY)),
            resolveItemFromBytes(primitive.get(BELT_KEY, PersistentDataType.BYTE_ARRAY)),
            resolveItemFromBytes(primitive.get(GLOVES_KEY, PersistentDataType.BYTE_ARRAY)),
            resolveItemFromBytes(primitive.get(CHARM_KEY, PersistentDataType.BYTE_ARRAY))
        )
    }

    /**
     * We cannot serialize air, but we need to store "empty" items. To do this, we are going to tag an item
     * with something so we can judge after deserialization if it should be converted to air.
     */
    private fun getEmptyItem(): ItemStack {
        val item = ItemStack.of(Material.BARRIER)
        item.editPersistentDataContainer { pdc -> pdc.set(INVALID_KEY, PersistentDataType.BOOLEAN, true) }
        return item
    }

    private fun isEmptyItem(item: ItemStack): Boolean {
        return item.persistentDataContainer.has(INVALID_KEY)
    }

    /**
     * Resolves an item from raw bytes. If this fails, we fall back to AIR.
     * This may be considered dangerous, as if the serialization process changes then we could just lose the item.
     */
    private fun resolveItemFromBytes(data: ByteArray?): ItemStack {
        if (data == null)
            return ItemStack(Material.AIR)

        val item = ItemStack.deserializeBytes(data)
        if (isEmptyItem(item))
            return ItemStack(Material.AIR)

        return item
    }

    /**
     * Serialize an item. This mainly serves as a purpose of injection to make air serialize as our "invalid" item
     * instead of attempting to serialize air as that is not supported.
     */
    private fun serializeItem(item: ItemStack): ByteArray {
        if (item.type == Material.AIR)
            return getEmptyItem().serializeAsBytes()
        return item.serializeAsBytes()
    }
}