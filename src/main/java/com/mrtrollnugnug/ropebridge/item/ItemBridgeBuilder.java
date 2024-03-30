package com.mrtrollnugnug.ropebridge.item;

import com.mrtrollnugnug.ropebridge.block.RopeBridgeBlock;
import com.mrtrollnugnug.ropebridge.handler.BridgeBuildingHandler;
import com.mrtrollnugnug.ropebridge.lib.Constants.Messages;
import com.mrtrollnugnug.ropebridge.lib.ModUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class ItemBridgeBuilder extends ItemBuilder {

	public ItemBridgeBuilder(Properties properties) {
		super(properties);
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
		super.onUsingTick(stack, player, count);
		if (player.level.isClientSide && player instanceof Player) {
			final Player p = (Player) player;
			rotatePlayerTowards(p, getNearestYaw(p));
		}
	}

	private static void rotatePlayerTowards(Player player, float target) {
		float yaw = player.yRotO % 360;
		if (yaw < 0) {
			yaw += 360;
		}
		rotatePlayerTo(player, yaw + (target - yaw) / 4);
	}

	private static void rotatePlayerTo(Player player, float yaw) {
		final float original = player.yRotO;
		player.yRotO = yaw;
		player.yRotO += player.yRotO - original;
	}

	private static float getNearestYaw(Player player) {
		float yaw = player.yRotO % 360;
		if (yaw < 0) {
			yaw += 360;
		}
		if (yaw < 45) {
			return 0F;
		}
		if (yaw > 45 && yaw <= 135) {
			return 90F;
		} else if (yaw > 135 && yaw <= 225) {
			return 180F;
		} else if (yaw > 225 && yaw <= 315) {
			return 270F;
		} else {
			return 360F;
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player && !world.isClientSide) {
			final Player player = (Player) entityLiving;
			if (this.getUseDuration(stack) - timeLeft > 10) {
				if (!player.isOnGround()) {
					ModUtils.tellPlayer(player, Messages.NOT_ON_GROUND);
				} else {
					final HitResult hit = trace(player);
					if (hit instanceof BlockHitResult) {
						final BlockPos floored = new BlockPos(Math.floor(player.getX()), Math.floor(player.getY()) - 1, Math.floor(player.getZ())).below();
						BlockPos target = ((BlockHitResult) hit).getBlockPos();
						BridgeBuildingHandler.newBridge(player, player.getMainHandItem(), floored, target);
					}
				}
			}
		}
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
		return false;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (isBridgeBlock(state.getBlock())) {
			return 1F;
		}
		return super.getDestroySpeed(stack, state);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TextComponent("- Hold right-click to build"));
		tooltip.add(new TextComponent("- Sneak to break whole bridge"));
	}

	private static boolean isBridgeBlock(Block block) {
		return block instanceof RopeBridgeBlock;
	}
}
