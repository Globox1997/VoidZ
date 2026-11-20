package net.voidz;

import net.minecraft.resources.Identifier;
import net.voidz.init.ConfigInit;

public class VoidMain {

    public static final String MOD_ID = "voidz";

    public static void init() {
        ConfigInit.init();
    }

    public static Identifier identifierOf(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }
}

// You are LOVED!!!
// Jesus loves you unconditional!