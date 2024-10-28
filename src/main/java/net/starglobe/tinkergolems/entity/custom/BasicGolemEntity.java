package net.starglobe.tinkergolems.entity.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.event.GameEvent;

public class BasicGolemEntity extends GolemEntity implements Shearable {
    private static final TrackedData<Byte> BASIC_GOLEM_FLAGS = DataTracker.registerData(BasicGolemEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final byte HAS_HAT_FLAG = 16;

    public BasicGolemEntity(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BASIC_GOLEM_FLAGS, (byte)16);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FindRailBlocksGoal(this, 0.9f, Blocks.RAIL));
        this.goalSelector.add(1, new FindRailBlocksGoal(this, 0.9f, Blocks.POWERED_RAIL));
        this.goalSelector.add(1, new FindRailBlocksGoal(this, 0.9f, Blocks.ACTIVATOR_RAIL));
        this.goalSelector.add(1, new FindRailBlocksGoal(this, 0.9f, Blocks.DETECTOR_RAIL));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1D));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_COPPER_PLACE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ITEM_AXE_SCRAPE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ITEM_AXE_SCRAPE;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.BLOCK_COPPER_STEP;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    public static DefaultAttributeContainer.Builder createBasicGolemAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Hat", this.hasHat());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Hat")) {
            this.setHasHat(nbt.getBoolean("Hat"));
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.SHEARS) && this.isShearable()) {
            this.sheared(SoundCategory.PLAYERS);
            this.emitGameEvent(GameEvent.SHEAR, player);
            if (!this.getWorld().isClient) {
                itemStack.damage(1, player, playerx -> playerx.sendToolBreakStatus(hand));
            }

            return ActionResult.success(this.getWorld().isClient);
        } else if (itemStack.isOf(Items.DIAMOND_HELMET) && !this.isShearable()) {
            this.equiped(SoundCategory.PLAYERS);
            this.emitGameEvent(GameEvent.EQUIP, player);
            if (!this.getWorld().isClient) {
                itemStack.damage(1, player, playerx -> playerx.sendToolBreakStatus(hand));
            }

            return ActionResult.success(this.getWorld().isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    public void sheared(SoundCategory shearedSoundCategory) {
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SNOW_GOLEM_SHEAR, shearedSoundCategory, 1.0F, 1.0F);
        if (!this.getWorld().isClient()) {
            this.setHasHat(false);
            this.dropStack(new ItemStack(Items.DIAMOND_HELMET), 1.7F);
        }
    }

    public void equiped(SoundCategory shearedSoundCategory) {
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, shearedSoundCategory, 1.0F, 1.0F);
        if (!this.getWorld().isClient()) {
            this.setHasHat(true);
        }
    }

    @Override
    public boolean isShearable() {
        return this.isAlive() && this.hasHat();
    }

    public boolean hasHat() {
        return (this.dataTracker.get(BASIC_GOLEM_FLAGS) & 16) != 0;
    }

    public void setHasHat(boolean hasHat) {
        byte b = this.dataTracker.get(BASIC_GOLEM_FLAGS);
        if (hasHat) {
            this.dataTracker.set(BASIC_GOLEM_FLAGS, (byte)(b | 16));
        } else {
            this.dataTracker.set(BASIC_GOLEM_FLAGS, (byte)(b & -17));
        }
    }

    class FindRailBlocksGoal extends MoveToTargetPosGoal {
        final Block targetBlock;
        final MobEntity findRailMob;
        private int counter;
        private static final int MAX_COOLDOWN = 20;

        public FindRailBlocksGoal(PathAwareEntity mob, double speed, Block targetBlock) {
            super(mob, speed, 24);
            this.targetBlock = targetBlock;
            this.findRailMob = mob;
        }

        @Override
        public boolean canStart() {
            if (this.cooldown > 0) {
                this.cooldown--;
                return false;
            } else if (this.findTargetPos()) {
                this.cooldown = toGoalTicks(20);
                return true;
            } else {
                this.cooldown = this.getInterval(this.mob);
                return false;
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.findRailMob.fallDistance = 1.0F;
        }

        @Override
        public void start() {
            super.start();
            this.counter = 0;
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            Chunk chunk = world.getChunk(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
            return chunk == null
                    ? false
                    : chunk.getBlockState(pos).isOf(this.targetBlock) && chunk.getBlockState(pos.up()).isAir() && chunk.getBlockState(pos.up(1)).isAir();
        }
    }
}
