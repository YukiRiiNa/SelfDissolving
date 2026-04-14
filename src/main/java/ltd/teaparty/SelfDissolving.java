package ltd.teaparty;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SelfDissolving.MODID)
public class SelfDissolving {
    // 定义模组 ID
    public static final String MODID = "selfdissolving";

    // 定义一个日志记录器，方便我们在控制台输出信息
    private static final Logger LOGGER = LogUtils.getLogger();

    // 构造函数，NeoForge 加载模组时会调用这里
    public SelfDissolving(IEventBus modEventBus) {

        // 因为我们使用了底层的 Mixin 技术，核心消散逻辑都在 PollutantRandomTickMixin 里。
        LOGGER.info("[selfdissolving]load successfully!");
    }
}
