package com.github.tacowasa059.dominomod.common.block;

import com.github.tacowasa059.dominomod.core.init.TileEntityTypesInit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DominoBlock extends Block  {
    public static IntegerProperty ROTATION =BlockStateProperties.ROTATION_0_15;

    public static BooleanProperty falling =BlockStateProperties.FALLING;

    //北
    private static final VoxelShape SHAPE_8= Block.makeCuboidShape(2, 0, 6, 14, 24, 10);
    private static final VoxelShape SHAPE_8_DROPPED =Block.makeCuboidShape(2, 0, -16, 14, 4, 8);


    //西
    private static final VoxelShape SHAPE_4=Block.makeCuboidShape(6, 0, 2, 10, 24, 14);
    private static final VoxelShape SHAPE_4_DROPPED=Block.makeCuboidShape(-16, 0, 2, 8, 4, 14);
    //南
    private static final VoxelShape SHAPE_0_DROPPED=Block.makeCuboidShape(2, 0, 8, 14, 4, 32);
    //東
    private static final VoxelShape SHAPE_12_DROPPED=Block.makeCuboidShape(8, 0, 2, 32, 4, 14);
    public DominoBlock() {
        super(AbstractBlock.Properties.create(Material.IRON, MaterialColor.GRAY).hardnessAndResistance(1f)
                .sound(SoundType.STONE));
        this.setDefaultState((this.stateContainer.getBaseState()).with(ROTATION, 0).with(falling,false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        if(state.get(falling)){
            switch (state.get(ROTATION)){
                case 0:
                case 14:
                case 15:
                case 1:
                    return SHAPE_0_DROPPED;
                case 2:
                case 3:
                case 4:
                case 5:
                    return SHAPE_4_DROPPED;
                case 6:
                case 7:
                case 8:
                case 9:
                    return SHAPE_8_DROPPED;
                case 10:
                case 11:
                case 12:
                case 13:
                    return SHAPE_12_DROPPED;

            }
        }
        else{
            switch (state.get(ROTATION)){
                case 0:
                case 8:
                case 6:
                case 14:
                case 7:
                case 15:
                case 1:
                case 9:
                    return SHAPE_8;
                case 2:
                case 10:
                case 3:
                case 11:
                case 4:
                case 12:
                case 5:
                case 13:
                    return SHAPE_4;
            }
        }
        return SHAPE_8;
    }
    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
        return p_185471_1_.with(ROTATION, p_185471_2_.mirrorRotation(p_185471_1_.get(ROTATION), 16));
    }

    @Override
    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        return p_185499_1_.with(ROTATION, p_185499_2_.rotate(p_185499_1_.get(ROTATION), 16));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return this.getDefaultState().with(ROTATION, MathHelper.floor((double)((180.0F + p_196258_1_.getPlacementYaw()) * 16.0F / 360.0F) + 0.5) & 15);
    }


    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        super.fillStateContainer(p_206840_1_);
        p_206840_1_.add(ROTATION, falling);
    }

    @Override
    public void onEntityCollision(BlockState p_196262_1_, World p_196262_2_, BlockPos p_196262_3_, Entity p_196262_4_) {
        if(!p_196262_2_.isRemote()&&!p_196262_1_.get(falling)){
            //倒れる方向の指定
            double X=(double)(p_196262_3_.getX())+0.5-p_196262_4_.getPosX();
            double Z=(double)(p_196262_3_.getZ())+0.5-p_196262_4_.getPosZ();
            int k=p_196262_1_.get(ROTATION);
            double x=-Math.sin(2*Math.PI*k/16);
            double z=Math.cos(2*Math.PI*k/16);
            int new_k=k;
            if(x*X+z*Z<0) {
                new_k = (k + 8) % 16;
                synchronized (p_196262_2_) {
                    //クライアントに正しく状態変更を反映させるためには、setBlockStateのフラグを3）
                    p_196262_2_.setBlockState(p_196262_3_, p_196262_1_.with(falling, true).with(ROTATION, new_k), 3);
                    this.playSound_Drop(p_196262_2_, p_196262_3_);
                }
            }
            else{
                synchronized (p_196262_2_) {

                    p_196262_2_.setBlockState(p_196262_3_, p_196262_1_.with(falling, true), 3);
                    this.playSound_Drop(p_196262_2_, p_196262_3_);
                }
            }
            onNeighboringCollision(new_k,p_196262_2_,p_196262_3_);
        }
    }
    private void onNeighboringCollision(int rotation,World world,BlockPos pos){
        BlockPos[] pos_list;
        switch (rotation) {
            case 1:
                pos_list = new BlockPos[]{pos.add(0, -1, 1), pos.add(-1, -1, 1)};
                break;
            case 2:
                pos_list = new BlockPos[]{pos.add(-1, -1, 1)};
                break;
            case 3:
                pos_list = new BlockPos[]{pos.add(-1, -1, 1), pos.add(-1, -1, 0)};
                break;
            case 4:
                pos_list = new BlockPos[]{pos.add(-1, -1, 0)};
                break;
            case 5:
                pos_list = new BlockPos[]{pos.add(-1, -1, -1), pos.add(-1, -1, 0)};
                break;
            case 6:
                pos_list = new BlockPos[]{pos.add(-1, -1, -1)};
                break;
            case 7:
                pos_list = new BlockPos[]{pos.add(-1, -1, -1), pos.add(0, -1, -1)};
                break;
            case 8:
                pos_list = new BlockPos[]{pos.add(0, -1, -1)};
                break;
            case 9:
                pos_list = new BlockPos[]{pos.add(0, -1, -1), pos.add(1, -1, -1)};
                break;
            case 10:
                pos_list = new BlockPos[]{pos.add(1, -1, -1)};
                break;
            case 11:
                pos_list = new BlockPos[]{pos.add(1, -1, -1), pos.add(1, -1, 0)};
                break;
            case 12:
                pos_list = new BlockPos[]{pos.add(1, -1, 0)};
                break;
            case 13:
                pos_list = new BlockPos[]{pos.add(1, -1, 0), pos.add(1, -1, 1)};
                break;
            case 14:
                pos_list = new BlockPos[]{pos.add(1, -1, 1)};
                break;
            case 15:
                pos_list = new BlockPos[]{pos.add(1, -1, 1), pos.add(0, -1, 1)};
                break;
            default:
                pos_list = new BlockPos[]{pos.add(0, -1, 1)};
                break;
        }

        for(BlockPos pos1:pos_list){
            for(int i=0;i<3;i++){
                BlockPos pos2=new BlockPos(pos1.getX(),pos1.getY()+i,pos1.getZ());
                BlockState next_state=world.getBlockState(pos2);
                if(next_state.getBlock() instanceof DominoBlock){
                    if(next_state.hasProperty(falling)&&!next_state.get(falling)) {
                        //倒れる方向の指定
                        double X = pos2.getX() - pos.getX();
                        double Z = pos2.getZ() - pos.getZ();
                        int k = next_state.get(ROTATION);
                        double x = -Math.sin(2 * Math.PI * k / 16);
                        double z = Math.cos(2 * Math.PI * k / 16);
                        final int new_k;
                        if (x * X + z * Z < 0) {
                            new_k = (k + 8) % 16;
                            synchronized (world){
                                world.setBlockState(pos2, next_state.with(falling, true).with(ROTATION, new_k), 3);

                                this.playSound_Drop(world, pos2);
                            }
                        } else {
                            new_k = k;
                            synchronized (world) {
                                world.setBlockState(pos2, next_state.with(falling, true), 3);
                                this.playSound_Drop(world, pos2);
                            }
                        }


                        //隣はスケジューラー0.1s後に倒す
                        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                        executorService.schedule(() -> {
                            onNeighboringCollision(new_k,world,pos2);
                        }, 100, TimeUnit.MILLISECONDS);

                    }
                }
            }
        }
    }



    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote) { // サーバーサイドでのみ実行
            if (state.get(falling)) { // fallingがtrueの場合
                world.setBlockState(pos, state.with(falling, false), 3); // fallingをfalseに設定
                this.playSound_Set(world,pos);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }


    private void playSound_Drop(World world, BlockPos pos) {
        if (!world.isRemote()) { // サーバー側でのみ実行
            world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_DOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }
    private void playSound_Set(World world, BlockPos pos) {
        if (!world.isRemote()) { // サーバー側でのみ実行
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }



    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypesInit.DOMINO_TILE_ENTITY_TYPE.get().create();
    }
    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(world, pos, state, player);
        if (!world.isRemote() && !player.isCreative()) {
            spawnAsEntity(world, pos, new ItemStack(this));
        }
    }

}
