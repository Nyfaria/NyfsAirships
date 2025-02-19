package com.nyfaria.nyfsairships.blocks;

import com.mojang.datafixers.util.Pair;
import com.nyfaria.nyfsairships.ConfigUtils;
import com.nyfaria.nyfsairships.NyfsAirships;
import com.nyfaria.nyfsairships.entity.BlockShipEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockControlWheel extends HorizontalDirectionalBlock {
    public BlockControlWheel(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Block.box(1d, 0d, 1d, 15d, 8d, 15d);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            int balloons = Integer.parseInt(ConfigUtils.config.get("balloon"));
            String whitelist = ConfigUtils.config.getOrDefault("whitelist", "false");
            int type = -1;
            int blocks = 0;
            int balances = 0;
            HashMap<String, Integer> used = new HashMap<>();
            ListTag list = new ListTag();
            ListTag addons = new ListTag();
            CompoundTag storage = new CompoundTag();

            List<Integer[]> filtered = new ArrayList<>();
            List<Integer[]> accepted = new ArrayList<>();
            int mposx = 3;
            int mposz = 3;
            int nposx = -3;
            int nposz = -3;
            int mposy = 3;
            int nposy = -3;
            filtered.add(new Integer[]{0, 0, 0});
            while (!filtered.isEmpty()) {
                Integer[] thisPos = filtered.get(0);
                BlockPos gpos = pos.offset(thisPos[0], thisPos[1], thisPos[2]);
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        for (int k = -1; k < 2; k++) {
                            BlockState theState = world.getBlockState(gpos.offset(i, j, k));
                            if (!theState.isAir() && !theState.getBlock().getDescriptionId().contains("ore")
                                    && theState.getBlock() != Blocks.WATER
                                    && theState.getBlock() != Blocks.LAVA) {
                                Optional<Pair<TagKey<Block>, HolderSet.Named<Block>>> bop =Registry.BLOCK.getTags().collect(Collectors.toSet()).stream().filter(
                                        (pair) -> pair.getFirst() == NyfsAirships.BOAT_MATERIAL_BLACKLIST
                                ).findFirst();
//                                whitelist = "true";
                                boolean flag1 = (whitelist.equals("true") && theState.is(NyfsAirships.BOAT_MATERIAL));
                                boolean flag2 = (whitelist.equals("false") && !theState.is(NyfsAirships.BOAT_MATERIAL_BLACKLIST));
                                if (flag1 || flag2) {
                                    Integer[] passable = new Integer[]{thisPos[0] + i, thisPos[1] + j, thisPos[2] + k};
                                    if (i != 0 || j != 0 || k != 0) {
                                        boolean b = false;
                                        for (Integer[] integers : filtered) {
                                            if (Arrays.equals(integers, passable)) {
                                                b = true;
                                                break;
                                            }
                                        }
                                        if (!b) {
                                            boolean result = false;
                                            for (Integer[] inside : accepted) {
                                                if (Arrays.equals(inside, passable)) {
                                                    result = true;
                                                    break;
                                                }
                                            }
                                            if (!result) {
                                                filtered.add(passable);
                                                if (passable[0] > mposx) {
                                                    mposx = passable[0];
                                                }
                                                if (passable[0] < nposx) {
                                                    nposx = passable[0];
                                                }
                                                if (passable[1] > mposy) {
                                                    mposy = passable[1];
                                                }
                                                if (passable[1] < nposy) {
                                                    nposy = passable[1];
                                                }
                                                if (passable[2] > mposz) {
                                                    mposz = passable[2];
                                                }
                                                if (passable[2] < nposz) {
                                                    nposz = passable[2];
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                accepted.add(filtered.remove(0));
            }
            for (Integer[] integers : accepted) {
                int i = integers[0];
                int j = integers[1];
                int k = integers[2];
                {
                    addIfCan(used, world.getBlockState(pos.offset(i, j, k)).getBlock().getDescriptionId(), 1);
                    list.add(StringTag.valueOf(
                            Block.getId(world.getBlockState(pos.offset(i, j, k))) + " " + i + " " + j + " " + k));
                    blocks++;

                    if (world.getBlockState(pos.offset(i, j, k)).getBlock() == Blocks.BLAST_FURNACE) {
                        addons.add(StringTag.valueOf("engine"));
                    }
                    if (world.getBlockState(pos.offset(i, j, k)).getBlock() == Blocks.ANVIL) {
                        addons.add(StringTag.valueOf("altitude"));
                    }

                    if (world.getBlockEntity(pos.offset(i, j, k)) != null) {
                        CompoundTag data = new CompoundTag();
                        world.getBlockEntity(pos.offset(i, j, k)).saveMetadata(data);
                        storage.put(i + " " + j + " " + k, data);
                    }


//                    if (world.getBlockState(pos.offset(i, j, k)).getBlock() == NyfsAirships.FLOAT_BLOCK && (type == 0 || type == -1)) {
//                        type = 0; //watership
//                        balances += floats;
//
//                    }
                    if (world.getBlockState(pos.offset(i, j, k)).getBlock() == NyfsAirships.BALLOON_BLOCK && (type == 1 || type == -1)) {
                        type = 1; //airship
                        balances += balloons;

                    }
//                    if (world.getBlockState(pos.offset(i, j, k)).getBlock() == NyfsAirships.WHEEL_BLOCK && (type == 2 || type == -1)) {
//                        type = 2; //carriage
//                        balances += wheels;
//                    }
                }
            }
            System.out.println("blocks: " + blocks);
            System.out.println("balances: " + balances);
            if (type == -1) {
                player.displayClientMessage(new TextComponent("No wheel/float/balloon found"), false);
                return InteractionResult.FAIL;
            }
            if (balances < blocks) {
                player.displayClientMessage(new TextComponent("Cannot assemble, not enough floats/balloons/wheels"), false);
//                if (type == 0) {
//                    player.displayClientMessage(new TextComponent("Requires " + blocks / floats + " floats, you have " + balances / floats), false);
//
//                }
                if (type == 1) {
                    player.displayClientMessage(new TextComponent("Requires " + Mth.ceil((double) blocks / (double)balloons) + " balloons, you have " + balances / balloons), false);
                }
//                if (type == 2) {
//                    player.displayClientMessage(new TextComponent("Requires " + blocks / wheels + " wheels, you have" + balances / wheels), false);
//                }
                return InteractionResult.FAIL;
            }
            list.forEach(block ->
            {
                String[] vv = block.getAsString().split(" ");
                if (world.getBlockEntity(pos.offset(Integer.parseInt(vv[1]), Integer.parseInt(vv[2]), Integer.parseInt(vv[3]))) != null) {
                    Clearable.tryClear(world.getBlockEntity(pos.offset(Integer.parseInt(vv[1]), Integer.parseInt(vv[2]), Integer.parseInt(vv[3]))));
                }
                world.setBlockAndUpdate(pos.offset(Integer.parseInt(vv[1]), Integer.parseInt(vv[2]), Integer.parseInt(vv[3])), Blocks.AIR.defaultBlockState());
            });

            int offset = 1;
            if (player.getItemInHand(hand).getItem() == NyfsAirships.LIFT_JACK_ITEM) {
                if (player.getItemInHand(hand).hasTag()) {
                    offset = player.getItemInHand(hand).getTag().getInt("off");
                }
            }
            BlockShipEntity entity = NyfsAirships.BLOCK_SHIP_ENTITY_ENTITY_TYPE.spawn((ServerLevel) world, null, null, player, player.blockPosition(), MobSpawnType.EVENT, false, false);
            entity.setModel(list, getDirection(state), offset, type, storage, addons);
            if (type == 1 || type == 0) {
                entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.STICK));
            }
            if (type == 1) {
                entity.setNoGravity(true);
            }

            player.startRiding(entity, true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    private static int getDirection(BlockState state) {
        if (state.getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.EAST) {
            return 90;
        }
        if (state.getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.SOUTH) {
            return 180;
        }
        if (state.getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.WEST) {
            return 270;
        }
        return 0;
    }

    private static HashMap<String, Integer> addIfCan(HashMap<String, Integer> input, String key, int mod) {
        if (input.containsKey(key)) {
            input.put(key, input.get(key) + mod);
        } else {
            input.put(key, mod);
        }
        return input;
    }


}
