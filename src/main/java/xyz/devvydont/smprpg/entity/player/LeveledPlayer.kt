package xyz.devvydont.smprpg.entity.player

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.FoodProperties
import io.papermc.paper.scoreboard.numbers.NumberFormat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerExpChangeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Team
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.entity.EntityGlobals
import xyz.devvydont.smprpg.entity.MobType
import xyz.devvydont.smprpg.entity.base.LeveledEntity
import xyz.devvydont.smprpg.entity.components.EntityConfiguration
import xyz.devvydont.smprpg.entity.player.settings.ExperienceBarFill
import xyz.devvydont.smprpg.entity.player.settings.ExperienceBarNumber
import xyz.devvydont.smprpg.entity.player.settings.PlayerSettings
import xyz.devvydont.smprpg.events.skills.SkillExperiencePostGainEvent
import xyz.devvydont.smprpg.items.interfaces.IAttributeItem
import xyz.devvydont.smprpg.services.*
import xyz.devvydont.smprpg.skills.SkillInstance
import xyz.devvydont.smprpg.skills.SkillType
import xyz.devvydont.smprpg.util.attributes.AttributeUtil
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils
import xyz.devvydont.smprpg.util.formatting.Symbols
import xyz.devvydont.smprpg.util.persistence.KeyStore
import xyz.devvydont.smprpg.util.persistence.PDCAdapters
import java.util.List
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

class LeveledPlayer(plugin: SMPRPG?, entity: Player) : LeveledEntity<Player>(entity), Listener {
    // Used as a shortcut for skill modification
    val combatSkill: SkillInstance
    val miningSkill: SkillInstance
    val fishingSkill: SkillInstance
    val farmingSkill: SkillInstance
    val woodcuttingSkill: SkillInstance
    val magicSkill: SkillInstance
    val slayerSkill: SkillInstance

    var accessories: AccessoryInventory = AccessoryInventory()
        set(acc) {
            _entity.persistentDataContainer.set(KeyStore.PLAYER_ACCESSORIES, PDCAdapters.ACCESSORY_INVENTORY_ADAPTER,
                acc!!)
        }

    private var _manaRegenerateTask: BukkitTask? = null
    var mana: Double = 0.0
        private set

    /**
     * The player's personal HUD preferences. Mutating the returned instance and then calling
     * [PlayerSettings.save] persists the change.
     * @return The cached settings instance for this player.
     */
    // The player's personal HUD preferences. Cached here so it can be read cheaply, including from the
    // asynchronous action bar task. Loaded from the player's PDC on setup and re-saved when changed via the menu.
    var settings: PlayerSettings = PlayerSettings()
        private set

    // The last values we wrote to the (purely visual) experience bar, so updateExperienceBar() can be called
    // freely on a fast cadence without re-sending identical packets to the client.
    private var _lastExpBarLevel = Int.Companion.MIN_VALUE
    private var _lastExpBarProgress = Float.Companion.NaN

    // The skill this player most recently gained experience in, used by the "last skill" experience bar modes.
    // Null until they gain any skill experience this session, in which case those modes fall back to the average.
    private var _lastGainedSkill: SkillInstance? = null

    // The player's attack-strength charge (0.0-1.0), polled once per tick. Recent Paper builds reset
    // the live attack-strength ticker before the damage event fires, so reading it there yields ~0.
    // Polling lets us recover the charge the player actually had when they swung. We keep the last two
    // ticks so the damage/crit logic can read the value from just before the attack reset the ticker.
    private var _attackChargePollTask: BukkitTask? = null
    private var _currentTickAttackCharge = 1f
    private var _previousTickAttackCharge = 1f

    private var _belowNameHealthTask: BukkitTask? = null

    // Last values we rendered to the below-name display, so we can skip redundant scoreboard writes.
    private var _lastBelowNameHp = Int.Companion.MIN_VALUE
    private var _lastBelowNameMaxHp = Int.Companion.MIN_VALUE
    private var _lastBelowNameAbsorption = Int.Companion.MIN_VALUE

    init {
        val skillService: SkillService = SMPRPG.getService(SkillService::class.java)
        // Skill shortcuts
        this.combatSkill = skillService.getNewSkillInstance(entity, SkillType.COMBAT)
        this.miningSkill = skillService.getNewSkillInstance(entity, SkillType.MINING)
        this.fishingSkill = skillService.getNewSkillInstance(entity, SkillType.FISHING)
        this.farmingSkill = skillService.getNewSkillInstance(entity, SkillType.FARMING)
        this.woodcuttingSkill = skillService.getNewSkillInstance(entity, SkillType.WOODCUTTING)
        this.magicSkill = skillService.getNewSkillInstance(entity, SkillType.MAGIC)
        this.slayerSkill = skillService.getNewSkillInstance(entity, SkillType.SLAYER)

        this._config = EntityConfiguration.PLAYER
    }

    override fun setup() {
        mobTypes.add(MobType.HUMANOID)

        super.setup()

        // Load the player's saved HUD preferences before anything that reads them (tasks, exp bar) starts.
        this.settings = _entity.persistentDataContainer
            .getOrDefault(
                KeyStore.PLAYER_SETTINGS!!,
                PDCAdapters.PLAYER_SETTINGS!!,
                PlayerSettings()
            )

        // Load the player's accessory inventory
        this.accessories = _entity.persistentDataContainer.getOrDefault(
            KeyStore.PLAYER_ACCESSORIES,
            PDCAdapters.ACCESSORY_INVENTORY_ADAPTER,
            AccessoryInventory()
        )

        startManaTask()
        startAttackChargePollTask()
        startBelowNameHealthTask()

        // Initialize the (purely visual) experience bar to reflect the player's settings.
        updateExperienceBar()

        // Vanilla food is disabled (see __onHungerChange), so crank up the regen rates to avoid the vanilla
        // hunger system ever interfering with our custom healing.
        _entity.saturatedRegenRate = 160
        _entity.unsaturatedRegenRate = 160
    }

    /**
     * Re-renders the vanilla experience bar to reflect this player's current settings. The bar is purely a
     * visual on this server, so this is the single authority that writes to it (all vanilla experience gains
     * are suppressed in [.__onVanillaExperienceGain]). The number and the bar fill are configured
     * independently, so this resolves each from settings. Redundant writes are skipped, so this is safe to
     * call on a fast cadence.
     */
    fun updateExperienceBar() {
        val level = computeExperienceBarNumber()
        val progress = computeExperienceBarProgress()

        if (level == _lastExpBarLevel && java.lang.Float.compare(progress, _lastExpBarProgress) == 0) return
        _lastExpBarLevel = level
        _lastExpBarProgress = progress

        _entity.setLevel(level)
        _entity.setExp(progress)
    }

    /**
     * Resolves the number to show on the experience bar from this player's settings.
     * @return The level number to display.
     */
    private fun computeExperienceBarNumber(): Int {
        return when (settings.getExperienceBarNumber()) {
            ExperienceBarNumber.MANA_PERCENT -> {
                val max = this.maxMana
                if (max <= 0) 0 else Math.round(Math.clamp(this.mana / max, 0.0, 1.0) * 100).toInt()
            }

            ExperienceBarNumber.MANA -> Math.round(this.mana).toInt()
            ExperienceBarNumber.POWER_RATING -> getLevel()
            ExperienceBarNumber.SKILL_AVERAGE -> this.averageSkillLevel.toInt()
            ExperienceBarNumber.LAST_SKILL -> if (_lastGainedSkill != null) _lastGainedSkill!!.level else this.averageSkillLevel.toInt()
            ExperienceBarNumber.HIDDEN -> 0
        }
    }

    /**
     * Resolves the fill (0.0-1.0) of the experience bar from this player's settings.
     * @return The bar progress to display.
     */
    private fun computeExperienceBarProgress(): Float {
        return when (settings.getExperienceBarFill()) {
            ExperienceBarFill.MANA -> {
                val max = this.maxMana
                if (max <= 0) 0f else Math.clamp(this.mana / max, 0.0, 1.0).toFloat()
            }

            ExperienceBarFill.SKILL_AVERAGE -> skillAverageProgress()
            ExperienceBarFill.LAST_SKILL -> if (_lastGainedSkill != null) skillProgressFraction(_lastGainedSkill!!) else skillAverageProgress()
            ExperienceBarFill.HIDDEN -> 0f
        }
    }

    /**
     * The fractional progress (0.0-1.0) toward the player's next average skill level. Creeps up as skill
     * experience is earned.
     * @return The average progress fraction.
     */
    private fun skillAverageProgress(): Float {
        val average = this.averageSkillLevel
        return (average - floor(average)).toFloat()
    }

    /**
     * The fractional progress (0.0-1.0) through a single skill's current level.
     * @param skill The skill to measure.
     * @return The progress fraction, or full when the skill is maxed.
     */
    private fun skillProgressFraction(skill: SkillInstance): Float {
        val into = skill.experienceProgress
        val band = into + skill.experienceForNextLevel
        if (band <= 0) return 1f // Max level (or no further progression) — render the bar as full.

        return Math.clamp(into.toDouble() / band, 0.0, 1.0).toFloat()
    }

    fun regenerateMana() {
        val max = this.maxMana
        this.mana += this.maxMana / 100
        this.mana = min(max(0.0, this.mana), max)

        // Refresh the experience bar on this periodic tick. It de-dupes redundant writes internally, so this
        // cheaply keeps mana-driven, power-rating (gear changes), and skill displays current without spamming
        // packets when nothing has actually changed.
        updateExperienceBar()
    }

    val lastMeleeAttackCharge: Float
        /**
         * The player's attack-strength charge (0.0-1.0) from just before their most recent attack reset
         * the attack-strength ticker. Use this instead of the live [Player.getAttackCooldown]
         * during damage events, where recent Paper builds have already reset the ticker to ~0.
         * Returns the higher of the last two polled values so the pre-attack charge survives regardless
         * of whether the per-tick poll ran before or after the attack within the tick.
         */
        get() = max(_currentTickAttackCharge, _previousTickAttackCharge)

    private fun pollAttackCharge() {
        _previousTickAttackCharge = _currentTickAttackCharge
        _currentTickAttackCharge = this.player.getAttackCooldown()
    }

    val maxMana: Double
        get() {
            val mana = SMPRPG.getService(AttributeService::class.java)
                .getOrCreateAttribute(_entity, AttributeWrapper.INTELLIGENCE)
            return mana.getValue()
        }

    fun useMana(cost: Int) {
        if (this.player.getGameMode().isInvulnerable()) return

        this.mana -= cost.toDouble()
        this.mana = max(0.0, this.mana)
    }

    fun gainMana(gained: Int) {
        if (this.player.getGameMode().isInvulnerable()) return

        this.mana += gained.toDouble()
        this.mana = min(this.mana, this.maxMana)
    }

    val difficulty: ProfileDifficulty
        get() = SMPRPG.getService(DifficultyService::class.java).getDifficulty(this.player)

    val skills: MutableCollection<SkillInstance>
        get() = mutableListOf(
            this.combatSkill,
            this.miningSkill,
            this.fishingSkill,
            this.farmingSkill,
            this.woodcuttingSkill,
            this.magicSkill,
            this.slayerSkill
        )

    val averageSkillLevel: Double
        get() {
            var sum = 0.0
            for (skill in this.skills) sum += skill.level.toDouble()
            return sum / this.skills.size
        }

    override fun getInvincibilityTicks(): Int {
        return 20
    }

    override fun getClassKey(): String {
        return "player"
    }

    override fun getDefaultEntityType(): EntityType {
        return EntityType.PLAYER
    }

    override fun getEntityName(): String {
        return _entity.getName()
    }

    override fun getLevel(): Int {
        // If we haven't fully initialized yet we cannot get a good calculation yet

        if (this.combatSkill == null) return 0

        // Using average level of gear and skills on this player determine how strong they are
        var factor = 0
        var total = 0.0

        // First skills
        for (skill in this.skills) total += skill.level.toDouble()
        factor += this.skills.size

        // Now gear
        val p = this.player
        val gear = arrayOf<ItemStack?>(
            p.getInventory().getHelmet(), p.getInventory().getChestplate(),
            p.getInventory().getLeggings(), p.getInventory().getBoots(), p.getInventory().getItemInMainHand(),
            p.getInventory().getItemInOffHand()
        )

        for (item in gear) {
            if (item == null || item.getType() == Material.AIR) continue

            val blueprint = SMPRPG.getService(ItemService::class.java).getBlueprint(item)
            if (blueprint !is IAttributeItem) continue

            total += (blueprint.getPowerRating() + AttributeUtil.getPowerBonus(item.getItemMeta())).toDouble()
            factor += 1
        }

        // The factor cannot be any less than all the skills we have + 4 pieces of armor (exclude holding nothing in our hands)
        factor = max(factor, this.skills.size + 4)
        return (total / factor).toInt()
    }

    val healthScale: Int
        /**
         * Calculates the number of half-hearts to render for the player, based on max HP.
         * Display rules:
         * - 100 HP = 1 full row = 20 half-hearts
         * - 1000 HP = 2 full rows = 40 half-hearts
         * - 2500 HP = 3 full rows = 60 half-hearts (cap)
         * The scale is calculated in tiers:
         * - Below 100 HP: 1 half-heart per 5 HP
         * - Between 100–1000 HP: 1 half-heart per 45 HP above 100 (starts from 20)
         * - Above 1000 HP: 1 half-heart per 500 HP above 1000 (starts from 40)
         * Always rounded up to the nearest even number (Minecraft only displays full hearts).
         * @return The health scale (number of half-hearts) to display to the client (min 2, max 60).
         */
        get() {
            val hp = getMaxHp().toFloat()
            var scale: Int

            if (hp < 100) {
                scale = Math.round(hp / 5f) // 20 at 100 HP
            } else if (hp < 1000) {
                scale = 20 + Math.round((hp - 100) / 45f) // 40 at 1000 HP
            } else {
                scale = 40 + Math.round((hp - 1000) / 500f) // Half heart every 500HP
            }

            // Round down to nearest even number to avoid half-hearts
            if (scale % 2 != 0) scale--

            return Math.clamp(scale.toLong(), 2, 60)
        }

    /**
     * The current value of HP this player's half of heart is HP wise
     * This amount of HP is used a lot for damage such as fall damage, burning, and regeneration values
     * @return The amount of HP this player's half heart is currently worth.
     */
    override fun getHalfHeartValue(): Double {
        return getMaxHp() / this.healthScale
    }

    override fun updateAttributes() {
        // Update max health to 100 while maintaining their current HP

        val percent = getHealthPercentage()
        updateBaseAttribute(AttributeWrapper.HEALTH, this._config.getBaseHealth().toDouble())

        if (percent > .01) setHealthPercentage(percent)

        // Set misc default base attributes that players should have
        updateBaseAttribute(AttributeWrapper.STRENGTH, this._config.getBaseDamage().toDouble())
        updateBaseAttribute(
            AttributeWrapper.REGENERATION,
            (if (this.difficulty == ProfileDifficulty.HARD) 50 else 100).toDouble()
        )
        updateBaseAttribute(
            AttributeWrapper.INTELLIGENCE,
            (if (this.difficulty == ProfileDifficulty.HARD) 50 else 100).toDouble()
        )
        updateBaseAttribute(AttributeWrapper.LUCK, 100.0)
        updateBaseAttribute(AttributeWrapper.DEFENSE, 0.0)
        updateBaseAttribute(AttributeWrapper.CRITICAL_CHANCE, 0.0)
        updateBaseAttribute(
            AttributeWrapper.CRITICAL_DAMAGE,
            (if (this.difficulty == ProfileDifficulty.HARD) 25 else 50).toDouble()
        )
        updateBaseAttribute(AttributeWrapper.SWEEPING, .05)

        updateBaseAttribute(AttributeWrapper.MINING_FORTUNE, 0.0)
        updateBaseAttribute(AttributeWrapper.FARMING_FORTUNE, 0.0)
        updateBaseAttribute(AttributeWrapper.WOODCUTTING_FORTUNE, 0.0)

        updateBaseAttribute(AttributeWrapper.FISHING_RATING, 0.0)
        updateBaseAttribute(AttributeWrapper.FISHING_CREATURE_CHANCE, 0.0)
        updateBaseAttribute(AttributeWrapper.FISHING_TREASURE_CHANCE, 0.0)

        updateBaseAttribute(AttributeWrapper.MINING_SPEED, 0.0)
        updateBaseAttribute(AttributeWrapper.LEGACY_MINING_SPEED, -100.0)
        updateBaseAttribute(AttributeWrapper.AIRBORNE_MINING, 0.2)


        // Make sure we aren't overloading their UI with hearts
        this.player.setHealthScale(this.healthScale.toDouble())
        this.player.setHealthScaled(true)

        // Make them only start with a fraction of their mana, to prevent abusing mana restoration from re-logging.
        this.mana = this.maxMana / 5
    }

    private val nametagTeam: Team
        get() {
            val scoreboard = Bukkit.getScoreboardManager().getMainScoreboard()
            val player = this.player
            val teamKey = player.getUniqueId().toString()
            var team = scoreboard.getTeam(teamKey)
            if (team == null) team = scoreboard.registerNewTeam(teamKey)
            team.addPlayer(player)
            return team
        }

    override fun updateNametag() {
        val team = this.nametagTeam
        val chatInformation = SMPRPG.getService(ChatService::class.java).getPlayerInfo(
            this.player
        )
        val newPrefix: Component = ComponentUtils.powerLevelPrefix(getLevel()).append(ComponentUtils.SPACE)
        team.prefix(newPrefix)
        if (!chatInformation.prefix.isEmpty()) team.suffix(
            Component.text(
                " " + chatInformation.prefix.trimEnd(),
                NamedTextColor.WHITE
            )
        )
        else team.suffix(null)
        team.color(NamedTextColor.nearestTo(this.difficulty.Color))
    }

    override fun cleanup() {
        super.cleanup()
        killManaTask()
        killAttackChargePollTask()
        killBelowNameHealthTask()
        clearBelowNameHealthScore()
    }

    override fun setLevel(level: Int) {
        // Does nothing
    }

    override fun resetLevel() {
        // Does nothing
    }

    override fun dimNametag() {
        // Does nothing
    }

    override fun brightenNametag() {
        // does nothing
    }

    override fun getDefaultConfiguration(): EntityConfiguration? {
        val difficulty = this.difficulty
        return EntityConfiguration.builder()
            .withLevel(0)
            .withHealth((if (difficulty == ProfileDifficulty.HARD) 50 else 100).toLong())
            .withDamage((if (difficulty == ProfileDifficulty.HARD) 2 else 5).toLong())
            .build()
    }

    val player: Player
        get() = _entity

    override fun hasVanillaDrops(): Boolean {
        return true
    }

    private fun killManaTask() {
        if (_manaRegenerateTask != null) _manaRegenerateTask!!.cancel()
        _manaRegenerateTask = null
    }

    private fun startManaTask() {
        killManaTask()
        _manaRegenerateTask = Bukkit.getScheduler()
            .runTaskTimer(plugin, Runnable { this.regenerateMana() }, 0, MANA_REGENERATE_FREQUENCY.toLong())
    }

    private fun killAttackChargePollTask() {
        if (_attackChargePollTask != null) _attackChargePollTask!!.cancel()
        _attackChargePollTask = null
    }

    private fun startAttackChargePollTask() {
        killAttackChargePollTask()
        _attackChargePollTask = Bukkit.getScheduler().runTaskTimer(plugin, Runnable { this.pollAttackCharge() }, 0, 1)
    }

    private fun killBelowNameHealthTask() {
        if (_belowNameHealthTask != null) _belowNameHealthTask!!.cancel()
        _belowNameHealthTask = null
    }

    private fun startBelowNameHealthTask() {
        killBelowNameHealthTask()
        // Force the next tick to render, in case the backing objective was recreated under us.
        _lastBelowNameHp = Int.Companion.MIN_VALUE
        _lastBelowNameMaxHp = Int.Companion.MIN_VALUE
        _lastBelowNameAbsorption = Int.Companion.MIN_VALUE
        _belowNameHealthTask = Bukkit.getScheduler().runTaskTimer(
            plugin, Runnable { this.updateBelowNameHealthDisplay() }, 0, BELOW_NAME_UPDATE_FREQUENCY.toLong()
        )
    }

    private val belowNameHealthObjective: Objective?
        /**
         * Returns the objective backing the below-name HP display, or `null` if it isn't set up yet.
         */
        get() {
            val scoreboard = Bukkit.getScoreboardManager().getMainScoreboard()
            return scoreboard.getObjective(EntityService.BELOW_NAME_HEALTH_OBJECTIVE)
        }

    /**
     * Refreshes this player's styled HP readout in the below-name scoreboard slot.
     *
     *
     * The below-name slot is global on the server, but on our Minecraft version (26.1.x) the client
     * draws it under every entity that has a custom name. To keep it player-only we render it through a
     * DUMMY objective (see [EntityService]) and write a score here for players exclusively; entities
     * without a score render nothing. We replace the raw score number with a fully styled component via
     * [NumberFormat.fixed], which is what lets us color HP and append a bar.
     *
     *
     * Skips the scoreboard write when the rendered values are unchanged so we aren't spamming packets.
     */
    private fun updateBelowNameHealthDisplay() {
        val hp = clampDisplayHealth(getTotalHp())
        val maxHp = ceil(getMaxHp()).toInt()
        val absorption = ceil(getAbsorptionHealth()).toInt()

        if (hp == _lastBelowNameHp && maxHp == _lastBelowNameMaxHp && absorption == _lastBelowNameAbsorption) return
        _lastBelowNameHp = hp
        _lastBelowNameMaxHp = maxHp
        _lastBelowNameAbsorption = absorption

        val objective = this.belowNameHealthObjective
        if (objective == null) return

        val score = objective.getScore(this.player.getName())
        score.setScore(hp) // The value is hidden behind the fixed format below; we set it for sanity only.
        score.numberFormat(NumberFormat.fixed(buildBelowNameHealthComponent()))
    }

    /**
     * Removes this player's below-name HP score, e.g. when they log out, so it doesn't linger.
     */
    private fun clearBelowNameHealthScore() {
        val objective = this.belowNameHealthObjective
        if (objective != null) objective.scoreboard?.resetScores(this.player.getName())
        _lastBelowNameHp = Int.Companion.MIN_VALUE
        _lastBelowNameMaxHp = Int.Companion.MIN_VALUE
        _lastBelowNameAbsorption = Int.Companion.MIN_VALUE
    }

    /**
     * Builds the styled below-name HP component, e.g. `1,847/2,000 ❤ +250 ▰▰▰▰▰▰▰▰▱▱`. The current
     * HP is colored by how much is missing (reusing [EntityGlobals.getChatColorFromHealth]),
     * absorption is shown in gold, and the bar fills by health percentage.
     */
    private fun buildBelowNameHealthComponent(): Component {
        val totalHp = getTotalHp()
        val maxHp = getMaxHp()
        val hp = clampDisplayHealth(totalHp)
        val maxHpInt = ceil(maxHp).toInt()
        val hpColor = EntityGlobals.getChatColorFromHealth(totalHp, maxHp)

        val readout = ComponentUtils.merge(
            ComponentUtils.create(
                MinecraftStringUtils.formatNumber(hp.toLong()),
                hpColor
            ),  //                ComponentUtils.create("/"),  // Uncomment for max HP number
            //                ComponentUtils.create(MinecraftStringUtils.formatNumber(maxHpInt), NamedTextColor.GREEN),  // Uncomment for max HP number
            ComponentUtils.create(" " + Symbols.HEART, NamedTextColor.DARK_RED)
        )

        val absorption = getAbsorptionHealth()
        return ComponentUtils.merge(
            readout,
            ComponentUtils.create(" "),
            buildHealthBar(totalHp, maxHp, absorption, hpColor)
        )
    }

    /**
     * Builds a fixed-width health bar whose filled portion reflects the current health percentage. The
     * filled segments take the same color as the HP number; empty segments are dark gray. A living player
     * always shows at least one filled segment, and the bar only reads as full when health is actually at
     * (or above) max.
     */
    private fun buildHealthBar(hp: Double, maxHp: Double, absorption: Double, filledColor: TextColor?): Component {
        val percent = if (maxHp <= 0) 0.0 else Math.clamp(hp / maxHp, 0.0, 1.0)
        var filled = Math.round(percent * HEALTH_BAR_SEGMENTS).toInt()
        if (filled <= 0 && hp > 0) filled = 1
        if (filled >= HEALTH_BAR_SEGMENTS && percent < 1.0) filled = HEALTH_BAR_SEGMENTS - 1

        val absorptionBar = if (absorption > 0) ComponentUtils.create(
            String.format(
                "+%d", (ceil(
                    this.player.getAbsorptionAmount() / 2.0
                )).toInt()
            ), NamedTextColor.AQUA
        ) else ComponentUtils.EMPTY

        return ComponentUtils.merge(
            ComponentUtils.create(Symbols.HEALTH_BAR_FILLED.repeat(filled), filledColor),
            ComponentUtils.create(
                Symbols.HEALTH_BAR_EMPTY.repeat(HEALTH_BAR_SEGMENTS - filled),
                NamedTextColor.DARK_GRAY
            ),
            absorptionBar
        )
    }

    /**
     * Rounds health for display: never shows 0 while the player is technically alive, and never shows a
     * fractional heart's worth of HP as 0.
     */
    private fun clampDisplayHealth(health: Double): Int {
        if (health <= 0) return 0
        if (health < 1) return 1
        return ceil(health).toInt()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private fun __onEntityAddToWorld(event: EntityAddToWorldEvent) {
        if (event.getEntity() == _entity) updateNametag()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private fun __onJoin(event: PlayerJoinEvent) {
        if (event.getPlayer() != this.player) return

        updateNametag()
        updateExperienceBar()
    }

    /**
     * Keeps the experience bar in sync the instant the player earns skill experience, so progress is
     * visualized in real time rather than only on the next periodic refresh.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    private fun __onSkillExperienceGained(event: SkillExperiencePostGainEvent) {
        if (event.player != this.player) return

        _lastGainedSkill = event.skill
        updateExperienceBar()
    }

    /**
     * The experience bar is purely a visual that we drive ourselves, so we suppress all vanilla experience
     * gains (orbs, thrown bottles, smelting, etc.) to keep the bar under our exclusive control.
     */
    @EventHandler
    private fun __onVanillaExperienceGain(event: PlayerExpChangeEvent) {
        if (event.getPlayer() != this.player) return

        event.setAmount(0)
    }

    /**
     * Prevents hunger from depleting, essentially disabling vanilla food.
     */
    @EventHandler
    private fun __onHungerChange(event: FoodLevelChangeEvent) {
        event.setCancelled(true) // Cancel Vanilla event

        if (event.getEntity() === this.player) {
            val item = event.getItem()
            if (item != null) {
                val foodComp = item.getData(DataComponentTypes.FOOD)
                if (foodComp != null) {
                    val foodRestoreAmt = foodComp.nutrition().toFloat()
                    val maxHp = this.player.getAttribute(Attribute.MAX_HEALTH)
                    if (maxHp != null)  // Sanity, should never be the case though.
                        this.player.heal((foodRestoreAmt / 100.0f) * maxHp.getValue())

                    if (foodComp.saturation() > 0) {
                        val max = this.maxMana
                        this.mana += foodComp.saturation().toDouble()
                        this.mana = min(max(0.0, this.mana), max)
                    }
                }
            }

            event.getEntity().setFoodLevel(20) // Force food level to be maxed out at all times.
            event.getEntity().setSaturation(20f)
        }
    }

    fun refillMana() {
        this.mana = this.maxMana
    }

    companion object {
        var MANA_REGENERATE_FREQUENCY: Int = 10

        // How often (in ticks) we refresh the below-name HP display, and how many segments its bar has.
        private const val BELOW_NAME_UPDATE_FREQUENCY = 2
        private const val HEALTH_BAR_SEGMENTS = 10
    }
}
