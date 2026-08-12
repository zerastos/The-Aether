package com.aetherteam.aether.event.listeners.abilities;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.capability.player.AetherPlayer;
import com.aetherteam.aether.event.hooks.AbilityHooks;
import com.aetherteam.aether.item.AetherItems;
import com.aetherteam.aether.item.combat.abilities.armor.GravititeArmor;
import com.aetherteam.aether.item.combat.abilities.armor.NeptuneArmor;
import com.aetherteam.aether.item.combat.abilities.armor.PhoenixArmor;
import com.aetherteam.aether.item.combat.abilities.armor.ValkyrieArmor;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Aether.MODID)
public class ArmorAbilityListener {
    /**
     * @see ValkyrieArmor#handleFlight(LivingEntity)
     * @see NeptuneArmor#boostWaterSwimming(LivingEntity)
     * @see PhoenixArmor#boostLavaSwimming(LivingEntity)
     * @see PhoenixArmor#damageArmor(LivingEntity)
     */
    @SubscribeEvent
    public static void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        if (event.isCanceled() || event.phase != TickEvent.Phase.START)  return;
        Player player = event.player;
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);

        if (helmet.is(AetherItems.VALKYRIE_HELMET.get())) {
            ValkyrieArmor.handleFlight(player);
        }

        AetherPlayer.get(player).ifPresent(aetherPlayer -> {
            // These must still run once after removing the set so the ramp can reset.

            if (helmet.is(AetherItems.NEPTUNE_HELMET.get()) || aetherPlayer.getNeptuneSubmergeLength() > 0.0) {
                NeptuneArmor.boostWaterSwimming(player);
            }

            if (helmet.is(AetherItems.PHOENIX_HELMET.get()) || aetherPlayer.getPhoenixSubmergeLength() > 0.0) {
                PhoenixArmor.boostLavaSwimming(player);
            }

            // Damage/conversion applies to individual Phoenix pieces, not just a complete set, so it must not be gated solely by the helmet.
            if (player.isInWaterRainOrBubble() || aetherPlayer.getObsidianConversionTime() > 0) {
                PhoenixArmor.damageArmor(player);
            }
        });
    }

    /**
     * @see GravititeArmor#boostedJump(LivingEntity)
     */
    @SubscribeEvent
    public static void onEntityJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity livingEntity = event.getEntity();
        GravititeArmor.boostedJump(livingEntity);
    }

    /**
     * @see AbilityHooks.ArmorHooks#fallCancellation(LivingEntity)
     */
    @SubscribeEvent
    public static void onEntityFall(LivingFallEvent event) {
        if (event.isCanceled()) return;
        LivingEntity livingEntity = event.getEntity();
        event.setCanceled(AbilityHooks.ArmorHooks.fallCancellation(livingEntity));
    }

    /**
     * @see PhoenixArmor#extinguishUser(LivingEntity, DamageSource)
     */
    @SubscribeEvent
    public static void onEntityAttack(LivingAttackEvent event) {
        if (event.isCanceled()) return;

        DamageSource damageSource = event.getSource();
        if (!damageSource.is(DamageTypeTags.IS_FIRE)) return;

        LivingEntity livingEntity = event.getEntity();
        event.setCanceled(PhoenixArmor.extinguishUser(livingEntity, damageSource));
    }
}
