package gd.rf.acro.platos;

import gd.rf.acro.platos.entity.BlockShipEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlatosTransporters.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Events {

    @SubscribeEvent
    public static void registerBoat(RegistryEvent.Register<EntityType<?>> entityTypeRegister)
    {
        entityTypeRegister.getRegistry().register(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE);
//        DefaultAttributes.put(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE, BlockShipEntity.createAttributes().build());
        SpawnPlacements.register(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlockShipEntity::checkAnimalSpawnRules);
        System.out.println("ship attributes registered!");
    }
    @SubscribeEvent
    public static void onAttributes(EntityAttributeCreationEvent event){
        event.put(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE, BlockShipEntity.createAttributes().build());
    }


}
