package ltd.teaparty.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// 目标类：Minecraft 底层控制所有方块状态的基类
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class PollutantRandomTickMixin {
    @Shadow public abstract Block getBlock();

    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void forceRandomTick(CallbackInfoReturnable<Boolean> cir) {
        String modid = BuiltInRegistries.BLOCK.getKey(this.getBlock()).getNamespace();
        if ("adpother".equals(modid)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void onRandomTick(ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        String blockId = BuiltInRegistries.BLOCK.getKey(this.getBlock()).toString();

        if (blockId.startsWith("adpother:")) {
            float decayChance = 0.05f;
            // 1. 处理气体或粉尘
            if (blockId.contains("carbon") || blockId.contains("sulfur") || blockId.contains("dust")) {
                if (random.nextFloat() < decayChance) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
            // 2. 处理污染水源
            else if (blockId.contains("polluted_water")) {

                // 获取当前坐标的方块状态
                BlockState state = level.getBlockState(pos);

                // 先安全检查这个方块是否有 LEVEL 属性，如果有，再判断它的值是不是 0 (即水源)
                if (state.hasProperty(BlockStateProperties.LEVEL) && state.getValue(BlockStateProperties.LEVEL) == 0) {

                    if (random.nextFloat() < decayChance) {
                        level.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                    }

                }
            }
        }
    }
}