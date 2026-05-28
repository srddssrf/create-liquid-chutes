package net.srddssrf.liquid_chutes;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.ticks.TickPriority;

import static net.srddssrf.liquid_chutes.LiquidChutes.LIQUID_CHUTE_ENTITY;

public class LiquidChuteBlock extends Block implements ProperWaterloggedBlock, IWrenchable, IBE<LiquidChuteBlockEntity> {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public LiquidChuteBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState()
			.setValue(WATERLOGGED, false));
	}
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(WATERLOGGED));
    }

    // Facing state
    public Direction getInputSide(BlockState state) { return Direction.UP;}
    public Direction getOutputSide(BlockState state) { return Direction.DOWN;}

    // Waterlogged state
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return fluidState(pState);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState,
                                  LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }

    // Actual work is done by the block entity and Create's Fluid Propagator and Pipe Connection. Send ticks there if we get them
    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block otherBlock, BlockPos neighborPos,
                                boolean isMoving) {
        super.neighborChanged(state, world, pos, otherBlock, neighborPos, isMoving);
        if (pos.relative(getInputSide(state)) == neighborPos || pos.relative(getOutputSide(state)) == neighborPos) {
            world.scheduleTick(pos, this, 1, TickPriority.HIGH);
        }
    }
    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource r) {
        FluidPropagator.propagateChangedPipe(world, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, world, pos, newState, isMoving);
        boolean blockTypeChanged = !state.is(newState.getBlock());
        if (blockTypeChanged && !world.isClientSide)
            world.scheduleTick(pos, this, 1, TickPriority.HIGH);
    }
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, world, pos, oldState, isMoving);
        boolean blockTypeChanged = !state.is(oldState.getBlock());
        if (blockTypeChanged && !world.isClientSide)
            world.scheduleTick(pos, this, 1, TickPriority.HIGH);
    }

    public Class<LiquidChuteBlockEntity> getBlockEntityClass() {
        return LiquidChuteBlockEntity.class;
    }

    @Override
    public BlockEntityType<LiquidChuteBlockEntity> getBlockEntityType() {
        return LIQUID_CHUTE_ENTITY.get();
    }
}