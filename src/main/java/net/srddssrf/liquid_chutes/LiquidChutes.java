package net.srddssrf.liquid_chutes;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.simibubi.create.foundation.data.SharedProperties;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class LiquidChutes implements ModInitializer {
    public static final String MOD_ID = "liquidchutes";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create("liquidchutes").defaultCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.key());


    public static final BlockEntry<LiquidChuteBlock> LIQUID_CHUTE = REGISTRATE.block("liquid_chute", LiquidChuteBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_ORANGE)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion())
            .transform(TagGen.pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<LiquidChuteKneeBlock> LIQUID_CHUTE_KNEE = REGISTRATE.block("liquid_chute_knee", LiquidChuteKneeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_ORANGE)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion())
            .transform(TagGen.pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<LiquidChuteElbowBlock> LIQUID_CHUTE_ELBOW = REGISTRATE.block("liquid_chute_elbow", LiquidChuteElbowBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_ORANGE)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion())
            .transform(TagGen.pickaxeOnly())
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntityEntry<LiquidChuteBlockEntity> LIQUID_CHUTE_ENTITY = REGISTRATE.blockEntity("liquid_chute_block_entity", LiquidChuteBlockEntity::new)
            .validBlocks(LIQUID_CHUTE, LIQUID_CHUTE_ELBOW, LIQUID_CHUTE_KNEE)
            .register();

    @Override
    public void onInitialize() {
        REGISTRATE.register();
    }

}
