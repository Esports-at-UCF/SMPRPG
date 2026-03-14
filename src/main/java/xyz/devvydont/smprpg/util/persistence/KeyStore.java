package xyz.devvydont.smprpg.util.persistence;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemType;
import xyz.devvydont.smprpg.SMPRPG;

/**
 * Stores various common {@link org.bukkit.NamespacedKey} globals to make code less spaghetti.
 * There is no general pattern to what keys can be stored here, if it feels right to be global then put it here.
 * Keep in mind that changing key namespaces and values will break some things that used the prior key.
 */
public class KeyStore {

    /**
     * The namespace to use when generating keys. You should never change this or else you will break the plugin.
     */
    private static final String NAMESPACE = "smprpg";

    /**
     * Creates a new namespaced key using the given value. This key will ensure that data accessed/stored will be
     * consistent across plugin restarts, assuming the namespace (or key values) never change.
     * @param value The value of the key.
     * @return A {@link NamespacedKey} instance.
     */
    private static NamespacedKey key(String value) {
        return new NamespacedKey(NAMESPACE, value);
    }

    /**
     * Used for storing a "fishing tracker" PDC. Tracks the amount of times certain things were caught.
     */
    public static final NamespacedKey FISHING_GALLERY = key("fishing_gallery");

    /**
     * Used for storing armor sets and quick swapping between them.
     */
    public static final NamespacedKey PLAYER_WARDROBE = key("wardrobe");

    /**
     * Used for tracking which wardrobe upgrade slots a player has unlocked.
     */
    public static final NamespacedKey WARDROBE_UPGRADES = key("wardrobe_upgrades");

    /**
     * Used for item rarity adjustments in item blueprints.
     */
    public static final NamespacedKey ITEM_RARITY_OVERRIDE = key("item_rarity_override");

    /**
     * Used for nerfing fishing attributes when dual wielding fishing rods.
     */
    public static final NamespacedKey FISHING_ATTRIBUTE_DUAL_WIELD_NERF = key("rod_dual_wield_nerf");

    /**
     * Used on slayer spawn entity PDCs to flag them for slayer quests
     */
    public static final NamespacedKey SLAYER_SPAWN_TYPE = key("slayer_spawn_type");

    // Easy access use to RegistryAccess, for code readability
    private static final RegistryAccess access = RegistryAccess.registryAccess();

    // Tag Keys
    public static final TagKey<ItemType> ENCHANTABLE_TOME = TagKey.create(RegistryKey.ITEM, Key.key(NAMESPACE, "enchantable/tome"));
    public static final TagKey<ItemType> ENCHANTABLE_APTITUDE = TagKey.create(RegistryKey.ITEM, Key.key(NAMESPACE, "enchantable/aptitude"));

    // Structure Keys
    public static final Structure CASTLE_DWELLING = access.getRegistry(RegistryKey.STRUCTURE).get(Key.key(NAMESPACE, "castle_dwelling"));

    // Sound Keys
    public static final NamespacedKey AUDIO_BREADBOARD_EAT = new NamespacedKey("audio", "food.breadboard.eat");

    public static final NamespacedKey AUDIO_RARE_DROP = new NamespacedKey("audio", "events.drops.rare");
    public static final NamespacedKey AUDIO_EPIC_DROP = new NamespacedKey("audio", "events.drops.epic");
    public static final NamespacedKey AUDIO_LEGENDARY_DROP = new NamespacedKey("audio", "events.drops.legendary");

}
