package fzzyhmstrs.lootify.mixins;

import fzzyhmstrs.lootify.util.DirectlyAddableBuilder;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.function.LootFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(LootTable.Builder.class)
public class LootTableBuilderMixin implements DirectlyAddableBuilder {

    @Shadow @Final
    private List<LootPool> pools;

    @Shadow @Final
    private List<LootFunction> functions;

    @Override
    public void addPool(LootPool pool) {
        this.pools.add(pool);
    }

    @Override
    public void addFunction(LootFunction function) {
        this.functions.add(function);
    }
}
