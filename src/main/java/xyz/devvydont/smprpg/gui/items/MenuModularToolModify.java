package xyz.devvydont.smprpg.gui.items;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.player.LeveledPlayer;
import xyz.devvydont.smprpg.gui.InterfaceUtil;
import xyz.devvydont.smprpg.gui.base.MenuBase;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IFueledEquipment;
import xyz.devvydont.smprpg.items.tools.ItemDrill;
import xyz.devvydont.smprpg.reforge.ReforgeBase;
import xyz.devvydont.smprpg.reforge.ReforgeType;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.ArrayList;
import java.util.List;

public class MenuModularToolModify extends MenuBase {

    public static final int ROWS = 5;

    public static final int TOOL_SLOT = 22;
    public static final int BEGIN_PART_SLOTS = 30;

    public MenuModularToolModify(@NotNull Player player) {
        super(player, ROWS);
    }

    public int getReforgeCost(ItemRarity rarity) {
        return switch (rarity) {
            case COMMON -> 250;
            case UNCOMMON -> 500;
            case RARE -> 1000;
            case EPIC -> 2500;
            case LEGENDARY -> 5000;
            case MYTHIC -> 10000;
            case DIVINE -> 25000;
            default -> 50000;
        };
    }

    /**
     * Shortcut method to get the balance of the player who owns this inventory.
     * @return The balance of the player
     */
    public int getBalance() {
        return SMPRPG.getService(EconomyService.class).getMoney(player);
    }

    /**
     * Generates the button to be displayed in the anvil click slot. Updates based on the state of the interface.
     *
     * @return an ItemStack to be used as an item display.
     */
    public ItemStack generateToolButton() {

        ItemStack input = getItem(TOOL_SLOT);
        ItemStack part = InterfaceUtil.getNamedItem(Material.BLACK_STAINED_GLASS_PANE, ComponentUtils.create("Tool Slot", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        markItemNoRender(part);
        List<Component> lore = new ArrayList<>();

        // Has nothing been input yet?
        if (input == null || input.getType().equals(Material.AIR)) {
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("Input a tool to swap out parts!", NamedTextColor.WHITE));
            part.editMeta(meta -> {
                meta.lore(ComponentUtils.cleanItalics(lore));
            });
            return part;
        }

        var blueprint = SMPRPG.getService(ItemService.class).getBlueprint(input);

        if (!(blueprint instanceof ItemDrill)) {  // TODO: Extend to generalized tools (Chainsaws, Jackhammers, etc.)
            return input;
        }
        return part;
    }

    public void initializeParts() {
        ItemStack input = getItem(TOOL_SLOT);

        // Item is either not defined, or is empty
        if (input == null || input.getType().equals(Material.AIR)) {
            return;
        }

        var blueprint = SMPRPG.getService(ItemService.class).getBlueprint(input);

        // Only execute this logic if its a drill.
        if (!(blueprint instanceof ItemDrill)) {  // TODO: Extend to generalized tools (Chainsaws, Jackhammers, etc.)
            // Valid item
            var containerData = input.getData(DataComponentTypes.CONTAINER);
            if (containerData != null || !containerData.contents().isEmpty()) {
                var contents = containerData.contents();
                int i = BEGIN_PART_SLOTS;
                for (var containerItem : contents) {
                    this.setSlot(i, containerItem);
                    i++;
                }
            }
        }
        else {
            ItemStack error = InterfaceUtil.getNamedItem(Material.BARRIER, ComponentUtils.create("Invalid Item!", NamedTextColor.RED));
            List<Component> lore = new ArrayList<>();
            lore.add(ComponentUtils.EMPTY);
            lore.add(ComponentUtils.create("Input a tool to swap out parts!", NamedTextColor.WHITE));
            error.editMeta(meta -> {
                meta.lore(ComponentUtils.cleanItalics(lore));
            });
            for (int i = BEGIN_PART_SLOTS; i < BEGIN_PART_SLOTS + 2; i++) {
                this.setSlot(i, error);
            }
        }
    }

    /**
     * Randomly rolls a reforge.
     *
     * @param classification The classification of the item that is being reforged.
     * @param exclude The reforge type to exclude when rolling a reforge. Can be null to consider all available reforges.
     * @return An randomly selected instance of a registered ReforgeBase singleton that is compatible with the classification.
     */
    public @NotNull ReforgeBase getRandomReforge(ItemClassification classification, @Nullable ReforgeType exclude) {

        // Construct a list of reforges to choose from by looping through all reforges and analyzing its compatibility.
        List<ReforgeBase> choices = new ArrayList<>();
        for (ReforgeType type : ReforgeType.values()) {

            // Do we want to exclude this reforge?
            if (type.equals(exclude))
                continue;

            // Is this reforge allowed to be rolled in a reforge station?
            if (!type.isRollable())
                continue;

            // Is this reforge allowed for this item type?
            if (!type.isAllowed(classification))
                continue;

            // Valid!
            choices.add(SMPRPG.getService(ItemService.class).getReforge(type));
        }

        // If we found no valid reforges, default to the error reforge type. Error reforge type should be handled by caller
        if (choices.isEmpty())
            return SMPRPG.getService(ItemService.class).getReforge(ReforgeType.ERROR);

        // Return a random choice
        return choices.get((int) (Math.random()*choices.size()));
    }

    /**
     * Called every time we click the reforge button regardless of the state of the GUI.
     */
    public void reforge() {

        // Check if we have an item in the input
        ItemStack item = getItem(TOOL_SLOT);
        if (item == null) {
            //playInvalidAnimation();
            return;
        }

        // Check if this item is able to store attributes. Reforges can't add attributes to attributeless items!
        SMPItemBlueprint blueprint = SMPRPG.getService(ItemService.class).getBlueprint(item);
        if (!(blueprint instanceof IAttributeItem attributeable)) {
            //playInvalidAnimation();
            return;
        }

        // Analyze the current reforge on the gear and determine if we can even roll another reforge without erroring
        ReforgeType currentReforgeType = blueprint.getReforgeType(item);
        ReforgeBase newReforge = getRandomReforge(blueprint.getItemClassification(), currentReforgeType);
        boolean success = !newReforge.getType().equals(ReforgeType.ERROR);

        // Determine if we can afford this reforge
        int cost = getReforgeCost(blueprint.getRarity(item));
        if (getBalance() < cost)
            success = false;

        // Apply reforge and take their money if we had no issues
        if (success) {
            newReforge.apply(item);
            SMPRPG.getService(EconomyService.class).spendMoney(player, cost);
            LeveledPlayer player = SMPRPG.getService(EntityService.class).getPlayerInstance(this.player);
            player.getMagicSkill().addExperience((blueprint.getRarity(item).ordinal()+1) * attributeable.getPowerRating() / 10);
        }

        Location soundOrigin = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2));
        player.getWorld().playSound(soundOrigin, success ? Sound.BLOCK_ANVIL_USE : Sound.ENTITY_VILLAGER_NO, .5f, .75f);
        blueprint.updateItemData(item);
    }

    /**
     * Renders the GUI.
     */
    public void render() {
        this.setBorderFull();
        this.clearSlot(TOOL_SLOT);
        for (int i=BEGIN_PART_SLOTS;i < BEGIN_PART_SLOTS + 2; i++)
            this.clearSlot(i);
        this.setButton(TOOL_SLOT, generateToolButton(), event -> {
            if (event.getAction().equals(InventoryAction.PLACE_ALL))
                initializeParts();
            else if (event.getAction().equals(InventoryAction.PICKUP_ALL))
                generateToolButton();
        });
        //this.setButton(BEGIN_PART_SLOTS, generatePartButton());
    }

    @Override
    protected void handleInventoryOpened(InventoryOpenEvent event) {
        super.handleInventoryOpened(event);
        this.render();
        event.titleOverride(ComponentUtils.merge(
                ComponentUtils.create("Tool Modification", NamedTextColor.BLACK)
        ));

    }

    @Override
    public void handleInventoryClicked(InventoryClickEvent event) {
        super.handleInventoryClicked(event);

        // Treat click events as a whitelist style
        event.setCancelled(true);

        if (event.getClickedInventory() == null)
            return;

        // Update the anvil button on the next tick to react to the state of the GUI
        Bukkit.getScheduler().runTaskLater(SMPRPG.getInstance(), () -> setSlot(TOOL_SLOT, generateToolButton()), 0L);

        // If we are clicking in the player inventory allow it to happen. We need to allow them to manage items.
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            event.setCancelled(false);
            return;
        }

        // If we are clicking in the input slot allow it to happen. The user owns this slot.
        if (event.getClickedInventory().equals(inventory) && event.getSlot() == TOOL_SLOT) {
            event.setCancelled(false);
        }
    }

    /**
     * When the inventory closes, make sure the item in the input slot is not lost.
     *
     * @param event The inventory close event.
     */
    @Override
    public void handleInventoryClosed(InventoryCloseEvent event) {
        super.handleInventoryClosed(event);
        giveItemToPlayer(TOOL_SLOT, true);
    }
}
