package net.voidz.init;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.voidz.config.VoidConfig;

public class ConfigInit {
    public static VoidConfig CONFIG = new VoidConfig();

    public static void init() {
        AutoConfig.register(VoidConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(VoidConfig.class).getConfig();
    }

}
