package fzzyhmstrs.lootify.util;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.function.LootFunction;

public interface DirectlyAddableBuilder {
    void addPool(LootPool pool);
    void addFunction(LootFunction function);
}
