package gd.rf.acro.platos.entity;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BlockShipEntityRenderer extends MobRenderer<BlockShipEntity,BlockShipEntityModel> {


    public BlockShipEntityRenderer(EntityRendererProvider.Context p_i50961_1_) {
        super(p_i50961_1_, new BlockShipEntityModel(), 1);
    }

    @Override
    public ResourceLocation getTextureLocation(BlockShipEntity entity) {
        return new ResourceLocation("minecraft","textures/block/acacia_planks.png");
    }
}
