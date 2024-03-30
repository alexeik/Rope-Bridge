package com.mrtrollnugnug.ropebridge.item;

import com.mrtrollnugnug.ropebridge.handler.LadderBuildingHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class ItemLadderBuilder extends ItemBuilder {

	public ItemLadderBuilder(Properties properties) {
		super(properties);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player && !world.isClientSide) {
			final Player player = (Player) entityLiving;
			if (this.getUseDuration(stack) - timeLeft > 5) {
				final HitResult hit = trace(player);
				if (hit instanceof BlockHitResult) {
					final BlockPos from = ((BlockHitResult) hit).getBlockPos();
					Direction side = ((BlockHitResult) hit).getDirection();
					LadderBuildingHandler.newLadder(from, player, player.getCommandSenderWorld(), side, player.getMainHandItem());
				}
			}
		}
	}
}
