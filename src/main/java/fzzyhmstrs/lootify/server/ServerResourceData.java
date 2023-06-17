package fzzyhmstrs.lootify.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fzzyhmstrs.lootify.Lootify;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.LootTable;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.util.*;

public class ServerResourceData {

    private static final Multimap<Identifier, LootTable> LOOTIFY_TABLES = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
    private static final List<Identifier> FOUND_TABLES = new LinkedList<>();
    private static final Gson GSON = LootGsons.getTableGsonBuilder().create();
    private static final int LOOTIFY_PATH_LENGTH = "lootify_tables/".length();
    private static final int FILE_SUFFIX_LENGTH = ".json".length();

    public static void loadLootifyTables(ResourceManager resourceManager){
        resourceManager.findResources("lootify_tables",path -> path.getPath().endsWith(".json")).forEach(ServerResourceData::loadLootifyTable);
    }

    private static void loadLootifyTable(Identifier id, Resource resource){
        if (Lootify.DEBUG) Lootify.LOGGER.info("Reading direct drop table from file: " + id.toString());
        String path = id.getPath();
        Identifier id2 = new Identifier(id.getNamespace(), path.substring(LOOTIFY_PATH_LENGTH, path.length() - FILE_SUFFIX_LENGTH));
        String path2 = id2.getPath();
        /*if (!(path2.startsWith("gameplay/") || path2.startsWith("chests/") || path2.startsWith("blocks/") || path2.startsWith("entities/") || path2.startsWith("entities/"))){
            Lootify.LOGGER.error("File path for [" + id + "] not correct; needs a 'blocks', 'chests', 'gameplay', or 'entities' subfolder. Skipping.");
            Lootify.LOGGER.error("Example: [./data/mod_id/direct_drops/blocks/cobblestone.json] is a valid block direct drop table path for a block added by [mod_id].");
            return;
        }*/
        try {
            BufferedReader reader = resource.getReader();
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            LootTable lootTable = GSON.fromJson(json, LootTable.class);
            if (lootTable != null) {
                LOOTIFY_TABLES.put(id2, lootTable);
            } else {
                Lootify.LOGGER.error("Loot table in file [" + id + "] is empty!");
            }
        } catch(Exception e){
            Lootify.LOGGER.error("Failed to open or read lootify table file: " + id);
        }
    }

    public static boolean hasLootifyTable(Identifier id){
        return LOOTIFY_TABLES.containsKey(id);
    }

    public static void findTable(Identifier id){
        FOUND_TABLES.add(id);
    }

    public static Collection<LootTable> getLootifyTable(Identifier id){
        return LOOTIFY_TABLES.get(id);
    }

    public static void initLootifyTableLoading(){
        FOUND_TABLES.clear();
        LOOTIFY_TABLES.clear();
    }

    public static void logMissedLootifyTables(){
        for (Identifier id : LOOTIFY_TABLES.keys()){
            if (!FOUND_TABLES.contains(id)){
                Lootify.LOGGER.warn("Lootify table [" + id + "] couldn't be matched to an existing loot table");
            }
        }
    }
}
