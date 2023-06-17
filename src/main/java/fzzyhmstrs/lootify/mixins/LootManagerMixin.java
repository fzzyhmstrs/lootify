package fzzyhmstrs.lootify.mixins;

import com.google.gson.JsonElement;
import fzzyhmstrs.lootify.server.ServerResourceData;
import net.minecraft.loot.LootManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(value = LootManager.class, priority = 2500)
public class LootManagerMixin {

    @Inject(method = "reload", at = @At("HEAD"))
    private void lootify_loadLootifyLootTables(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor, CallbackInfoReturnable<CompletableFuture<Void>> cir){
        ServerResourceData.initLootifyTableLoading();
        ServerResourceData.loadLootifyTables(manager);
    }

    @Inject(method = "reload", at = @At("RETURN"), cancellable = true)
    private void lootify_finishApplyingLootifyLootTables(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor, CallbackInfoReturnable<CompletableFuture<Void>> cir){
        ServerResourceData.logMissedLootifyTables();
        cir.setReturnValue(cir.getReturnValue());
    }

}
