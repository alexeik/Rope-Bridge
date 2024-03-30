package com.mrtrollnugnug.ropebridge.lib;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class BlockItemUseContextExt extends BlockPlaceContext {
	public BlockItemUseContextExt(Level worldIn, @Nullable Player playerIn, InteractionHand handIn, ItemStack stackIn, BlockHitResult rayTraceResultIn) {
		super(worldIn, playerIn, handIn, stackIn, rayTraceResultIn);
	}
}
