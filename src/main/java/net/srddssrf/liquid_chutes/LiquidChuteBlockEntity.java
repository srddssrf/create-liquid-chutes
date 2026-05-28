package net.srddssrf.liquid_chutes;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class LiquidChuteBlockEntity extends SmartBlockEntity {
    private Direction inputSide = Direction.UP;
    private Direction outputSide = Direction.DOWN;

    final public Float deliveredPressure = 20f; // 20 pressure gives 10 mb/t (forge mb). Exactly enough for level 1 steam engine.

    public LiquidChuteBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        updateFacing(state);
    }

    private void updateFacing(BlockState state) {
        if (state.getBlock() instanceof LiquidChuteKneeBlock) {
            inputSide = state.getValue(LiquidChuteKneeBlock.HORIZONTAL_FACING);
        } else if (state.getBlock() instanceof LiquidChuteElbowBlock) {
            outputSide = state.getValue(LiquidChuteElbowBlock.HORIZONTAL_FACING);
        }
    }
    @Override
    public void tick() {
        super.tick();

    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new ChuteFluidTransferBehaviour(this));
    }

    // code for fluid transfer setup. Basically all we do is set pressure to neighbour blocks
    // Create handles the actual transfer and pipe connections
    class ChuteFluidTransferBehaviour extends FluidTransportBehaviour {
        public ChuteFluidTransferBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public void tick() {
            updateFacing(getBlockState());

            if (interfaces.containsKey(outputSide) && interfaces.containsKey(inputSide)) {
                interfaces.get(inputSide).determineSource(level, worldPosition);
                interfaces.get(outputSide).determineSource(level, worldPosition);
                interfaces.get(inputSide).getPressure().set(true, deliveredPressure);
                interfaces.get(inputSide).getPressure().set(false, 0f);
                interfaces.get(outputSide).getPressure().set(false, deliveredPressure);
                interfaces.get(outputSide).getPressure().set(true, 0f);

                // not chainable
                if (FluidPropagator.getPipe(level, getBlockPos().relative(inputSide)) != null)
                    interfaces.get(inputSide).getPressure().set(true, 0f);
                if (FluidPropagator.getPipe(level, getBlockPos().relative(outputSide)) != null)
                    interfaces.get(outputSide).getPressure().set(false, 0f);
            }
            super.tick();
        }

        @Override
        public boolean canHaveFlowToward(BlockState state, Direction direction) {
            return (direction == outputSide || direction == inputSide);
        }

    }
}


