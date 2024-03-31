package com.mrtrollnugnug.ropebridge;

import com.mrtrollnugnug.ropebridge.handler.ConfigHandler;
import com.mrtrollnugnug.ropebridge.handler.ContentHandler;
import com.mrtrollnugnug.ropebridge.lib.Constants;
import com.mrtrollnugnug.ropebridge.lib.ModUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class RopeBridge {

	public static final CreativeModeTab RopeBridgeTab = new CreativeModeTab("RopeBridgeTab") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ContentHandler.bridge_builder.get());
		}
	};

	public RopeBridge() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,ConfigHandler.GENERAL_SPEC,"rope_bridge.toml");
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ContentHandler.register(modEventBus);
		modEventBus.addListener(this::preInit);
	}

	private void preInit(FMLCommonSetupEvent event) {
		ModUtils.initMap();

		System.out.println("DEBUG_000" + event.description());
	}
}
