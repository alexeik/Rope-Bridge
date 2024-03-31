package com.mrtrollnugnug.ropebridge.lib;

import com.mrtrollnugnug.ropebridge.block.RopeBridgeBlock;
import com.mrtrollnugnug.ropebridge.block.RopeLadderBlock;
import com.mrtrollnugnug.ropebridge.handler.ContentHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraftforge.registries.IForgeRegistry;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class ModUtils {

	private ModUtils() {
	}

	public static final Map<Block, Pair<Block, Block>> map = new HashMap<>();

	/**
	 * Sends a message to a command sender. Can be used for easier message
	 * sending.
	 * @param sender
	 * The thing to send the message to. This should probably be a
	 * player.
	 * @param message
	 * The message to send. This can be a normal message, however
	 * translation keys are HIGHLY encouraged!
	 */
	public static void tellPlayer(Player sender, String message, Object... params) {
		sender.sendSystemMessage(Component.literal(message));
	}

/*	public static <T extends IForgeRegistryEntry<T>> void register(T obj, String name, IForgeRegistry<T> registry) {
		register(obj, Constants.MOD_ID, name, registry);
	}*/

/*	public static <T extends IForgeRegistryEntry<T>> void register(T obj, String modid, String name, IForgeRegistry<T> registry) {
		register(obj, new ResourceLocation(modid, name), registry);
	}*/

/*	public static <T extends IForgeRegistryEntry<T>> void register(T obj, ResourceLocation location, IForgeRegistry<T> registry) {
		registry.register(obj.setRegistryName(location));
	}*/

	public static void initMap() {
		map.put(Blocks.OAK_SLAB, Pair.of(ContentHandler.oak_bridge.get(), ContentHandler.oak_rope_ladder.get()));
		map.put(Blocks.BIRCH_SLAB, Pair.of(ContentHandler.birch_bridge.get(), ContentHandler.birch_rope_ladder.get()));
		map.put(Blocks.JUNGLE_SLAB, Pair.of(ContentHandler.jungle_bridge.get(), ContentHandler.jungle_rope_ladder.get()));
		map.put(Blocks.SPRUCE_SLAB, Pair.of(ContentHandler.spruce_bridge.get(), ContentHandler.spruce_rope_ladder.get()));
		map.put(Blocks.ACACIA_SLAB, Pair.of(ContentHandler.acacia_bridge.get(), ContentHandler.acacia_rope_ladder.get()));
		map.put(Blocks.DARK_OAK_SLAB, Pair.of(ContentHandler.dark_oak_bridge.get(), ContentHandler.dark_oak_rope_ladder.get()));

		((RopeLadderBlock) ContentHandler.oak_rope_ladder.get()).setSlab(Blocks.OAK_SLAB);
		((RopeLadderBlock) ContentHandler.birch_rope_ladder.get()).setSlab(Blocks.BIRCH_SLAB);
		((RopeLadderBlock) ContentHandler.jungle_rope_ladder.get()).setSlab(Blocks.JUNGLE_SLAB);
		((RopeLadderBlock) ContentHandler.spruce_rope_ladder.get()).setSlab(Blocks.SPRUCE_SLAB);
		((RopeLadderBlock) ContentHandler.acacia_rope_ladder.get()).setSlab(Blocks.ACACIA_SLAB);
		((RopeLadderBlock) ContentHandler.dark_oak_rope_ladder.get()).setSlab(Blocks.DARK_OAK_SLAB);

		((RopeBridgeBlock) ContentHandler.oak_bridge.get()).setSlab(Blocks.OAK_SLAB);
		((RopeBridgeBlock) ContentHandler.birch_bridge.get()).setSlab(Blocks.BIRCH_SLAB);
		((RopeBridgeBlock) ContentHandler.jungle_bridge.get()).setSlab(Blocks.JUNGLE_SLAB);
		((RopeBridgeBlock) ContentHandler.spruce_bridge.get()).setSlab(Blocks.SPRUCE_SLAB);
		((RopeBridgeBlock) ContentHandler.acacia_bridge.get()).setSlab(Blocks.ACACIA_SLAB);
		((RopeBridgeBlock) ContentHandler.dark_oak_bridge.get()).setSlab(Blocks.DARK_OAK_SLAB);
	}


}
