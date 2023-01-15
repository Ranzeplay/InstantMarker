package me.ranzeplay.instantmarker;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class Localization {
    public static Text SelfMarkBlock(Block block, BlockPos pos) {
        return Text.empty()
                .append(Text.translatable("text.instantmarker.mark_block"))
                .append(Text.translatable(block.getTranslationKey()).formatted(Formatting.BOLD, Formatting.GREEN))
                .append(Text.translatable("text.instantmarker.separator"))
                .append(Text.translatable("text.instantmarker.loc"))
                .append(Text.literal(String.format("(%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ())).formatted(Formatting.YELLOW));
    }

    public static Text SelfMarkEntity(Entity entity) {
        return Text.empty()
                .append(Text.translatable("text.instantmarker.mark_entity"))
                .append(entity.getEntityName())
                .append(Text.translatable("text.instantmarker.separator"))
                .append(Text.translatable("text.instantmarker.loc"))
                .append(String.format("(%.1f, %.1f, %.1f)", entity.getX(), entity.getY(), entity.getZ()));
    }
}
