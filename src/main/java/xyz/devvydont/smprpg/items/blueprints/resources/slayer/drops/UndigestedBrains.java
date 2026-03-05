package xyz.devvydont.smprpg.items.blueprints.resources.slayer.drops;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.*;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.services.ChatService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class UndigestedBrains extends CustomItemBlueprint implements IHeaderDescribable, Listener, ITrackedConsumable, ISellable {

    public UndigestedBrains(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public static final NamespacedKey TRACKER_KEY = new NamespacedKey(SMPRPG.getPlugin(), "consumable_undigested_brain");
    public static final NamespacedKey BRAIN_ATTRIBUTE_BONUS = new NamespacedKey(SMPRPG.getPlugin(), "undigested_brains_bonus");
    public static final double INTELLIGENCE_BONUS = 10.0;

    @Override
    public List<Component> getHeader(ItemStack meta) {
        return List.of(
                ComponentUtils.merge(
                    ComponentUtils.create("Consume to be "),
                    ComponentUtils.create("plagued", NamedTextColor.DARK_GRAY),
                    ComponentUtils.create(" by the memories of an")
                ),
                ComponentUtils.create("adventurer unfortunate enough to perish to the "),
                ComponentUtils.merge(
                    ComponentUtils.create("Shambling Abomination", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                    ComponentUtils.create(".")
                ),
                ComponentUtils.EMPTY,
                ComponentUtils.create("(And also gain +10 permanent intelligence up to 5 times.)", NamedTextColor.AQUA)
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

    @Override
    public @NonNull Consumable getConsumableComponent(ItemStack item) {
        return Consumable.consumable()
                .consumeSeconds(5)
                .sound(SoundEventKeys.BLOCK_HONEY_BLOCK_BREAK)
                .build();
    }

    @Override
    public void updateItemData(ItemStack item) {
        super.updateItemData(item);
        item.editMeta(meta -> meta.setMaxStackSize(1));
    }

    @Override
    public int getWorth(ItemStack item) {
        return 100_000 * item.getAmount();
    }

    @Override
    public NamespacedKey getConumableTrackerKey() {
        return TRACKER_KEY;
    }

    @Override
    public int getMaxUses() {
        return 5;
    }

    @EventHandler
    public void __onConsumeBrain(PlayerItemConsumeEvent event) {

        ItemStack consumedItem = event.getItem();
        if (!isItemOfType(consumedItem))
            return;

        var player = event.getPlayer();
        var playerPdc = player.getPersistentDataContainer();
        var numTimesEaten = playerPdc.getOrDefault(getConumableTrackerKey(), PersistentDataType.INTEGER, 0);
        if (numTimesEaten < getMaxUses()) {
            var intComp = ComponentUtils.create("You have gained +10 intelligence!", NamedTextColor.AQUA);
            player.sendMessage(intComp);

            // TODO: Make fun flavor text for these
            //switch (numTimesEaten) {
            //    case 0:
            //    {

            //    }
            //}
            player.playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1f, 2f);
            player.playSound(player, Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1f, 2f);
            numTimesEaten++;
            playerPdc.set(getConumableTrackerKey(), PersistentDataType.INTEGER, numTimesEaten);
            var intInst = AttributeService.getInstance().getOrCreateAttribute(player, AttributeWrapper.INTELLIGENCE);
            intInst.addModifier(new AttributeModifier(BRAIN_ATTRIBUTE_BONUS, INTELLIGENCE_BONUS * numTimesEaten, AttributeModifier.Operation.ADD_NUMBER));
            intInst.save(player, AttributeWrapper.INTELLIGENCE);
        }
        else {
            player.sendMessage(ComponentUtils.error("You have already eaten " + getMaxUses() + " lumps of undigested brains!"));
            player.playSound(player, Sound.ENTITY_ZOMBIE_AMBIENT, 1f, 0.5f);
            event.setCancelled(true);
        }
    }
}
