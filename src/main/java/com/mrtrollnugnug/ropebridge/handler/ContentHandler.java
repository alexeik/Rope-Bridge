package com.mrtrollnugnug.ropebridge.handler;

import com.mrtrollnugnug.ropebridge.RopeBridge;
import com.mrtrollnugnug.ropebridge.block.RopeBridgeBlock;
import com.mrtrollnugnug.ropebridge.block.RopeLadderBlock;
import com.mrtrollnugnug.ropebridge.item.ItemBridgeBuilder;
import com.mrtrollnugnug.ropebridge.item.ItemLadderBuilder;
import com.mrtrollnugnug.ropebridge.lib.Constants;
import com.mrtrollnugnug.ropebridge.lib.ModUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;


public class ContentHandler {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
		BLOCKS.register(eventBus);
	}

	public static final RegistryObject<Item> bridge_builder = ITEMS.register("bridge_builder", () -> new ItemBridgeBuilder(new Item.Properties().tab(RopeBridge.RopeBridgeTab)));
	public static final RegistryObject<Item> ladder_builder = ITEMS.register("ladder_builder", () -> new ItemLadderBuilder(new Item.Properties().tab(RopeBridge.RopeBridgeTab)));
	public static final RegistryObject<Item> rope = ITEMS.register("rope", () -> new Item(new Item.Properties().tab(RopeBridge.RopeBridgeTab)));
	public static final RegistryObject<Item> bridge_builder_hook = ITEMS.register("bridge_builder_hook", () -> new Item(new Item.Properties().tab(RopeBridge.RopeBridgeTab)));
	public static final RegistryObject<Item> bridge_builder_barrel = ITEMS.register("bridge_builder_barrel", () -> new Item(new Item.Properties().tab(RopeBridge.RopeBridgeTab)));
	public static final RegistryObject<Item> bridge_builder_handle = ITEMS.register("bridge_builder_handle", () -> new Item(new Item.Properties().tab(RopeBridge.RopeBridgeTab)));
	public static final RegistryObject<Item> ladder_hook = ITEMS.register("ladder_hook", () -> new Item(new Item.Properties().tab(RopeBridge.RopeBridgeTab)));

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);

	private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
		return ContentHandler.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}

	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		//registerBlockItem(name, toReturn, tab);
		return toReturn;
	}

//	public static final Block.Properties bridge = Block.Properties.copy(Blocks.OAK_PLANKS).noOcclusion();
//	public static final Block.Properties ladder = Block.Properties.copy(Blocks.LADDER);

	public static final RegistryObject<RopeLadderBlock> oak_rope_ladder = registerBlock("oak_rope_ladder", () -> new RopeLadderBlock(Block.Properties.copy(Blocks.LADDER)), RopeBridge.RopeBridgeTab);
	public static final RegistryObject<RopeLadderBlock> birch_rope_ladder = registerBlock("birch_rope_ladder", () -> new RopeLadderBlock(Block.Properties.copy(Blocks.LADDER)), RopeBridge.RopeBridgeTab);
	public static final RegistryObject<RopeLadderBlock> jungle_rope_ladder = registerBlock("jungle_rope_ladder", () -> new RopeLadderBlock(Block.Properties.copy(Blocks.LADDER)), RopeBridge.RopeBridgeTab);
	public static final RegistryObject<RopeLadderBlock> spruce_rope_ladder = registerBlock("spruce_rope_ladder", () -> new RopeLadderBlock(Block.Properties.copy(Blocks.LADDER)), RopeBridge.RopeBridgeTab);
	public static final RegistryObject<RopeLadderBlock> acacia_rope_ladder = registerBlock("acacia_rope_ladder", () -> new RopeLadderBlock(Block.Properties.copy(Blocks.LADDER)), RopeBridge.RopeBridgeTab);
	public static final RegistryObject<RopeLadderBlock> dark_oak_rope_ladder = registerBlock("dark_oak_rope_ladder", () -> new RopeLadderBlock(Block.Properties.copy(Blocks.LADDER)), RopeBridge.RopeBridgeTab);

	public static final RegistryObject<RopeBridgeBlock> oak_bridge = registerBlock("oak_bridge", () -> new RopeBridgeBlock( Block.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()), RopeBridge.RopeBridgeTab);
	public static final RegistryObject<RopeBridgeBlock> birch_bridge = registerBlock("birch_bridge", () -> new RopeBridgeBlock(Block.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()), RopeBridge.RopeBridgeTab);
	public static final RegistryObject<RopeBridgeBlock> jungle_bridge = registerBlock("jungle_bridge", () -> new RopeBridgeBlock(Block.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()), RopeBridge.RopeBridgeTab);
	public static final RegistryObject<RopeBridgeBlock> spruce_bridge = registerBlock("spruce_bridge", () -> new RopeBridgeBlock(Block.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()), RopeBridge.RopeBridgeTab);
	public static final RegistryObject<RopeBridgeBlock> acacia_bridge = registerBlock("acacia_bridge", () -> new RopeBridgeBlock(Block.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()), RopeBridge.RopeBridgeTab);
	public static final RegistryObject<RopeBridgeBlock> dark_oak_bridge = registerBlock("dark_oak_bridge", () -> new RopeBridgeBlock(Block.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()), RopeBridge.RopeBridgeTab);

//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID+ ":"+ "acacia_rope_ladder")
//	public static final Block acacia_rope_ladder = null;

//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID+ ":"+ "dark_oak_rope_ladder")
//	public static final Block dark_oak_rope_ladder = null;

//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID+ ":"+ "oak_bridge")
//	public static final Block oak_bridge = null;
//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID+ ":"+ "birch_bridge")
//	public static final Block birch_bridge = null;
//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID+ ":"+ "jungle_bridge")
//	public static final Block jungle_bridge = null;
//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID+ ":"+ "spruce_bridge")
//	public static final Block spruce_bridge = null;
//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID+ ":"+ "acacia_bridge")
//	public static final Block acacia_bridge = null;
//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID+ ":"+ "dark_oak_bridge")
//	public static final Block dark_oak_bridge = null;

	// Items
	//@ObjectHolder(registryName = "minecraft:item", value = Constants.MOD_ID + ":" + "bridge_builder")
	//public static final Item bridge_builder = null;
	/*@ObjectHolder(registryName = "minecraft:item", value = Constants.MOD_ID+ ":" + "ladder_builder")
	public static final Item ladder_builder = null;
	@ObjectHolder(registryName = "minecraft:item", value = Constants.MOD_ID+ ":" + "rope")
	public static final Item rope = null;
	@ObjectHolder(registryName = "minecraft:item", value = Constants.MOD_ID+ ":" + "bridge_builder_hook")
	public static final Item bridge_builder_hook = null;
	@ObjectHolder(registryName = "minecraft:item", value = Constants.MOD_ID+ ":" + "bridge_builder_barrel")
	public static final Item bridge_builder_barrel = null;
	@ObjectHolder(registryName = "minecraft:item", value = Constants.MOD_ID+ ":" + "bridge_builder_handle")
	public static final Item bridge_builder_handle = null;
	@ObjectHolder(registryName = "minecraft:item", value = Constants.MOD_ID+ ":" + "ladder_hook")
	public static final Item ladder_hook = null;*/


	// Blocks //этот objectholder указание для того, чтобы заполнить его static final Block переменные
//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID + ":"+ "oak_rope_ladder")
//	public static final Block oak_rope_ladder = null;

//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID + "birch_rope_ladder")
//	public static final Block birch_rope_ladder = null;

//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID+ ":"+ "jungle_rope_ladder")
//	public static final Block jungle_rope_ladder = null;

//	@ObjectHolder(registryName = "minecraft:block", value = Constants.MOD_ID+ ":"+ "spruce_rope_ladder")
//	public static final Block spruce_rope_ladder = null;



/*
	@SubscribeEvent
	public static void initBlocks(final RegisterEvent event) {
		Block.Properties bridge = Block.Properties.copy(Blocks.OAK_PLANKS).noOcclusion();
		Block.Properties ladder = Block.Properties.copy(Blocks.LADDER);
		event.register(ForgeRegistries.Keys.BLOCKS,
			helper -> {
				helper.register(new ResourceLocation(Constants.MOD_ID, "oak_bridge"), new RopeBridgeBlock(bridge));
				helper.register(new ResourceLocation(Constants.MOD_ID, "birch_bridge"), new RopeBridgeBlock(bridge));
				helper.register(new ResourceLocation(Constants.MOD_ID, "jungle_bridge"), new RopeBridgeBlock(bridge));
				helper.register(new ResourceLocation(Constants.MOD_ID, "spruce_bridge"), new RopeBridgeBlock(bridge));
				helper.register(new ResourceLocation(Constants.MOD_ID, "acacia_bridge"), new RopeBridgeBlock(bridge));
				helper.register(new ResourceLocation(Constants.MOD_ID, "dark_oak_bridge"), new RopeBridgeBlock(bridge));
				helper.register(new ResourceLocation(Constants.MOD_ID, "oak_rope_ladder"), new RopeLadderBlock(ladder));
				helper.register(new ResourceLocation(Constants.MOD_ID, "birch_rope_ladder"), new RopeLadderBlock(ladder));
				helper.register(new ResourceLocation(Constants.MOD_ID, "jungle_rope_ladder"), new RopeLadderBlock(ladder));
				helper.register(new ResourceLocation(Constants.MOD_ID, "spruce_rope_ladder"), new RopeLadderBlock(ladder));
				helper.register(new ResourceLocation(Constants.MOD_ID, "acacia_rope_ladder"), new RopeLadderBlock(ladder));
				helper.register(new ResourceLocation(Constants.MOD_ID, "dark_oak_rope_ladder"), new RopeLadderBlock(ladder));
				// ...
			}
		);

	}

	@SubscribeEvent
	public static void initItems(final RegisterEvent event) {
		Item.Properties properties = new Item.Properties().tab(RopeBridge.RopeBridgeTab);
		event.register(ForgeRegistries.Keys.ITEMS,
			helper -> {

				helper.register(new ResourceLocation(Constants.MOD_ID, "ladder_builder"), new ItemLadderBuilder(properties));
				helper.register(new ResourceLocation(Constants.MOD_ID, "bridge_builder_hook"), new Item(properties));
				helper.register(new ResourceLocation(Constants.MOD_ID, "bridge_builder_barrel"), new Item(properties));
				helper.register(new ResourceLocation(Constants.MOD_ID, "bridge_builder_handle"), new Item(properties));
				helper.register(new ResourceLocation(Constants.MOD_ID, "ladder_hook"), new Item(properties));
				helper.register(new ResourceLocation(Constants.MOD_ID, "bridge_builder"), new ItemBridgeBuilder(properties));
				helper.register(new ResourceLocation(Constants.MOD_ID, "rope"), new Item(properties));

				// ...
			}
		);


	}*/
}
