package com.dyn.robot.entity.ai;

import com.dyn.robot.entity.EntityRobot;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowsOwnerEX extends EntityAIBase {
	private EntityRobot follower;
	private EntityLivingBase theOwner;
	World theWorld;
	private double followSpeed;
	private PathNavigate entityPath;
	private int field_75343_h;
	float maxDist;
	float minDist;

	public EntityAIFollowsOwnerEX(EntityRobot follower, EntityLivingBase owner, double followSpeedIn, float minDistIn,
			float maxDistIn) {
		this.follower = follower;
		theWorld = follower.worldObj;
		followSpeed = followSpeedIn;
		entityPath = follower.getNavigator();
		minDist = minDistIn;
		maxDist = maxDistIn;
		setMutexBits(3);
		theOwner = owner;

		if (!(follower.getNavigator() instanceof PathNavigateGround)) {
			throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !entityPath.noPath() && (follower.getDistanceSqToEntity(theOwner) > (maxDist * maxDist));
	}

	private boolean func_181065_a(BlockPos p_181065_1_) {
		IBlockState iblockstate = theWorld.getBlockState(p_181065_1_);
		Block block = iblockstate.getBlock();
		return block == Blocks.air ? true : !block.isFullCube();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		entityPath.clearPathEntity();
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if ((theOwner == null) || !follower.getIsFollowing()) {
			return false;
		} else if ((theOwner instanceof EntityPlayer) && ((EntityPlayer) theOwner).isSpectator()) {
			return false;
		} else if (follower.getDistanceSqToEntity(theOwner) < (minDist * minDist)) {
			return false;
		}
		return true;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		field_75343_h = 0;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		follower.getLookHelper().setLookPositionWithEntity(theOwner, 10.0F, follower.getVerticalFaceSpeed());

		if (--field_75343_h <= 0) {
			field_75343_h = 10;

			if (!entityPath.tryMoveToEntityLiving(theOwner, followSpeed)) {
				if (follower.getDistanceSqToEntity(theOwner) >= 144.0D) {
					int i = MathHelper.floor_double(theOwner.posX) - 2;
					int j = MathHelper.floor_double(theOwner.posZ) - 2;
					int k = MathHelper.floor_double(theOwner.getEntityBoundingBox().minY);

					for (int l = 0; l <= 4; ++l) {
						for (int i1 = 0; i1 <= 4; ++i1) {
							if (((l < 1) || (i1 < 1) || (l > 3) || (i1 > 3))
									&& World.doesBlockHaveSolidTopSurface(theWorld, new BlockPos(i + l, k - 1, j + i1))
									&& func_181065_a(new BlockPos(i + l, k, j + i1))
									&& func_181065_a(new BlockPos(i + l, k + 1, j + i1))) {
								follower.setLocationAndAngles(i + l + 0.5F, k, j + i1 + 0.5F, follower.rotationYaw,
										follower.rotationPitch);
								entityPath.clearPathEntity();
								return;
							}
						}
					}
				}
			}
		}
	}
}
