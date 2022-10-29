package gd.rf.acro.platos.entity;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class BlockShipEntityRenderer extends MobRenderer<BlockShipEntity,BlockShipEntityModel> {


    public BlockShipEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BlockShipEntityModel(),1);
    }

    @Override
    public ResourceLocation getTextureLocation(BlockShipEntity entity) {
        return new ResourceLocation("minecraft","textures/block/acacia_planks.png");
    }

    @Override
    public boolean shouldRender(BlockShipEntity mobEntity, Frustum frustum, double d, double e, double f) {
        if(mobEntity.position().distanceToSqr(new Vec3(d,e,f))<100*100)
        {
            return true;
        }
        return super.shouldRender(mobEntity, frustum, d, e, f);
    }
}
