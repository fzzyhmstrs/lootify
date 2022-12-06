package fzzyhmstrs.lootify;

import fzzyhmstrs.lootify.server.ServerResourceData;
import fzzyhmstrs.lootify.util.DirectlyAddableBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.function.LootFunction;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class Lootify implements ModInitializer {

    public static String MOD_ID = "lootify";
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("lootify");
    public static boolean DEBUG = true;

    @Override
    public void onInitialize() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (ServerResourceData.hasLootifyTable(id)){
                Collection<LootTable> newTables = ServerResourceData.getLootifyTable(id);
                for (LootTable table : newTables){
                    LootPool[] pools = table.pools;
                    for (LootPool pool: pools){
                        ((DirectlyAddableBuilder)tableBuilder).addPool(pool);
                    }
                    LootFunction[] functions = table.functions;
                    for (LootFunction function: functions){
                        ((DirectlyAddableBuilder)tableBuilder).addFunction(function);
                    }
                }
            }
        });
    }
}
