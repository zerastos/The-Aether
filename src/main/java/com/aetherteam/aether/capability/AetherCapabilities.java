package com.aetherteam.aether.capability;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.capability.accessory.MobAccessory;
import com.aetherteam.aether.capability.accessory.MobAccessoryCapability;
import com.aetherteam.aether.capability.arrow.PhoenixArrow;
import com.aetherteam.aether.capability.arrow.PhoenixArrowCapability;
import com.aetherteam.aether.capability.item.DroppedItem;
import com.aetherteam.aether.capability.item.DroppedItemCapability;
import com.aetherteam.aether.capability.lightning.LightningTracker;
import com.aetherteam.aether.capability.lightning.LightningTrackerCapability;
import com.aetherteam.aether.capability.player.AetherPlayer;
import com.aetherteam.aether.capability.player.AetherPlayerCapability;
import com.aetherteam.aether.capability.time.AetherTime;
import com.aetherteam.aether.capability.time.AetherTimeCapability;
import com.aetherteam.aether.data.resources.registries.AetherDimensions;
import com.aetherteam.nitrogen.capability.CapabilityProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = Aether.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AetherCapabilities {
	public static final Capability<AetherPlayer> AETHER_PLAYER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() { });
	public static final Capability<MobAccessory> MOB_ACCESSORY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() { });
	public static final Capability<PhoenixArrow> PHOENIX_ARROW_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() { });
	public static final Capability<LightningTracker> LIGHTNING_TRACKER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() { });
	public static final Capability<DroppedItem> DROPPED_ITEM_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() { });
	public static final Capability<AetherTime> AETHER_TIME_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() { });

	private static final ResourceLocation RESOURCE_CAPABILITY_PLAYER = new ResourceLocation(Aether.MODID, "aether_player");
	private static final ResourceLocation RESOURCE_CAPABILITY_MOB_ACCESSORY = new ResourceLocation(Aether.MODID, "mob_accessory");
	private static final ResourceLocation RESOURCE_CAPABILITY_PHOENIX_ARROW = new ResourceLocation(Aether.MODID, "phoenix_arrow");
	private static final ResourceLocation RESOURCE_CAPABILITY_LIGHTNING_BOLT = new ResourceLocation(Aether.MODID, "lightning_tracker");
	private static final ResourceLocation RESOURCE_CAPABILITY_DROPPED_ITEM = new ResourceLocation(Aether.MODID, "dropped_item");

	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event) {
		event.register(AetherPlayer.class);
		event.register(MobAccessory.class);
		event.register(PhoenixArrow.class);
		event.register(LightningTracker.class);
		event.register(DroppedItem.class);
		event.register(AetherTime.class);
	}
	
	@EventBusSubscriber(modid = Aether.MODID)
	public static class Registration {
		@SubscribeEvent
		public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof LivingEntity livingEntity) {
				if (livingEntity instanceof Player player) {
					event.addCapability(RESOURCE_CAPABILITY_PLAYER, new CapabilityProvider(AetherCapabilities.AETHER_PLAYER_CAPABILITY, new AetherPlayerCapability(player)));
				} else if (livingEntity instanceof Mob mob) {
					event.addCapability(RESOURCE_CAPABILITY_MOB_ACCESSORY, new CapabilityProvider(AetherCapabilities.MOB_ACCESSORY_CAPABILITY, new MobAccessoryCapability(mob)));
				}
			} else if (event.getObject() instanceof AbstractArrow abstractArrow) {
				event.addCapability(RESOURCE_CAPABILITY_PHOENIX_ARROW, new CapabilityProvider(AetherCapabilities.PHOENIX_ARROW_CAPABILITY, new PhoenixArrowCapability(abstractArrow)));
			} else if (event.getObject() instanceof LightningBolt lightningBolt) {
				event.addCapability(RESOURCE_CAPABILITY_LIGHTNING_BOLT, new CapabilityProvider(AetherCapabilities.LIGHTNING_TRACKER_CAPABILITY, new LightningTrackerCapability(lightningBolt)));
			} else if (event.getObject() instanceof ItemEntity itemEntity) {
				event.addCapability(RESOURCE_CAPABILITY_DROPPED_ITEM, new CapabilityProvider(AetherCapabilities.DROPPED_ITEM_CAPABILITY, new DroppedItemCapability(itemEntity)));
			}
		}

		@SubscribeEvent
		public static void attachWorldCapabilities(AttachCapabilitiesEvent<Level> event) {
			if (event.getObject().dimensionType().effectsLocation().equals(AetherDimensions.AETHER_DIMENSION_TYPE.location())) {
				event.addCapability(new ResourceLocation(Aether.MODID, "aether_time"), new CapabilityProvider(AetherCapabilities.AETHER_TIME_CAPABILITY, new AetherTimeCapability(event.getObject())));
			}
		}
	}
}
