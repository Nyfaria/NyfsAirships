package com.nyfaria.nyfsairships.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;

public class BlockShipEntityModel extends EntityModel<BlockShipEntity> {
    private HashMap<String, ListTag> entities;
    private String ship;
    private int direction;
    private float xOffset;
    private float zOffset;
    private int yOffset;

    @Override
    public void setupAnim(BlockShipEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if(this.entities==null)
        {
            this.entities=new HashMap<>();
        }
        if(entity.getItemBySlot(EquipmentSlot.CHEST).getItem()== Items.OAK_PLANKS)
        {
            CompoundTag tag = entity.getItemBySlot(EquipmentSlot.CHEST).getTag();
            this.ship=tag.getString("model");
            this.entities.put(this.ship,(ListTag)tag.get("parts"));
            this.direction=tag.getInt("direction");
            this.xOffset=getxOffset();
            this.zOffset=getzOffset();
            this.yOffset=tag.getInt("offset");
        }
    }

    private float getxOffset()
    {
        switch (this.direction)
        {
            case 90:
                return 1f;
            case 180:
            case 0:
                return -0.5f;
            case 270:
                return -2f;
            default:
                return 0f;
        }
    }
    private float getzOffset()
    {
        switch (this.direction)
        {
            case 90:
            case 270:
                return -0.5f;
            case 180:
                return 1f;
            case 0:
                return -2f;
            default:
                return 0f;
        }
    }



    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        if(this.ship!=null)
        {
            MultiBufferSource vertexConsumerProvider = Minecraft.getInstance().renderBuffers().bufferSource();
            matrices.pushPose();
            matrices.scale(-1.0F, -1.0F, 1.0F);
            matrices.mulPose(Vector3f.YP.rotationDegrees(this.direction));
            entities.get(this.ship).forEach(line->
            {
                String[] compound = line.getAsString().split(" ");
                //Block block = Registry.BLOCK.get(new Identifier(compound[0]));
                matrices.pushPose();
                matrices.translate(Double.parseDouble(compound[1])+this.xOffset,Double.parseDouble(compound[2])+this.yOffset-1.5,Double.parseDouble(compound[3])+this.zOffset);
                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Block.stateById(Integer.parseInt(compound[0])),matrices, vertexConsumerProvider,light,overlay);
                matrices.popPose();
            });
            matrices.popPose();
        }
    }
}