package net.xalcon.chococraft.client.models.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.xalcon.chococraft.common.entities.EntityChocobo;

public class ModelChocobo extends ModelBase
{
    //region Adult Mesh
    ModelRenderer head;
    ModelRenderer feather;
    ModelRenderer feather_2;

    ModelRenderer neck;

    ModelRenderer body;

    ModelRenderer tail;
    ModelRenderer tail_2;
    ModelRenderer tail_3;

    ModelRenderer rightWing;
    ModelRenderer leftWing;

    ModelRenderer rightThigh;
    ModelRenderer leftThigh;

    ModelRenderer rightShin;
    ModelRenderer leftShin;

    ModelRenderer talonRB;
    ModelRenderer talonRR;
    ModelRenderer talonRL;

    ModelRenderer talonLL;
    ModelRenderer talonLR;
    ModelRenderer talonLB;

    ModelRenderer rightSaddleBag;
    ModelRenderer leftSaddleBag;
    ModelRenderer packBag;
    //endregion

    //region Child Mesh
    public ModelRenderer child_head;
    public ModelRenderer child_body;
    public ModelRenderer child_rightleg;
    public ModelRenderer child_leftleg;
    //endregion

    public ModelChocobo()
    {
        this.textureWidth = 128;
        this.textureHeight = 128;

        // x = wide
        // y = height
        // z = length

        head = new ModelRenderer(this, 87, 2);
        neck = new ModelRenderer(this, 20, 45);
        feather = new ModelRenderer(this, 25, 18);
        feather_2 = new ModelRenderer(this, 45, 21);

        head.addBox(-3F, -12F, -9F, 6, 6, 12);
        head.setRotationPoint(1F, -8F, -5F);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);

        neck.addBox(-2F, -9F, -2F, 4, 14, 4);
        neck.setRotationPoint(1F, -8F, -5F);
        neck.mirror = true;
        setRotation(neck, 0F, 0F, 0F);

        feather.addBox(-3F, -11.5F, 3F, 6, 1, 5);
        feather.setRotationPoint(1F, -8F, -5F);
        feather.mirror = true;
        setRotation(feather, 0.1745329F, 0F, 0F);

        feather_2.addBox(-3F, -12F, 3F, 6, 6, 3);
        feather_2.setRotationPoint(1F, -8F, -5F);
        feather_2.mirror = true;
        setRotation(feather_2, 0F, 0F, 0F);

        body = new ModelRenderer(this, 77, 31);
        body.addBox(-3F, -4F, -3F, 12, 20, 11);
        body.setRotationPoint(-2F, 1F, -2F);
        body.mirror = true;
        setRotation(body, 1.37881F, 0F, 0F);

        // x = wide     + = left
        // y = height   + = down
        // z = length   + = back

        // body (1=4px)
        // x = wide     + = left
        // y = length   + =
        // z = height   + = up

        rightSaddleBag = new ModelRenderer(this, 2, 72);
        rightSaddleBag.addBox(-4.0F, 10.1F, 0.5F, 2, 6, 8);
        rightSaddleBag.setRotationPoint(-2F, 1F, -2F);
        rightSaddleBag.mirror = true;
        setRotation(rightSaddleBag, 1.37881F, 0F, 0F);

        leftSaddleBag = new ModelRenderer(this, 2, 96);
        leftSaddleBag.addBox(7.8F, 10.1F, 0.5F, 2, 6, 8);
        leftSaddleBag.setRotationPoint(-2F, 1F, -2F);
        leftSaddleBag.mirror = true;
        setRotation(leftSaddleBag, 1.37881F, 0F, 0F);

        packBag = new ModelRenderer(this, 50, 66);
        packBag.addBox(-2.0F, 3.0F, 8.0F, 10, 12, 6);
        packBag.setRotationPoint(-2F, 1F, -2F);
        packBag.mirror = true;
        setRotation(packBag, 1.37881F, 0F, 0F);


        tail = new ModelRenderer(this, 59, 2);
        tail.addBox(0F, 0F, 0F, 12, 14, 1);
        tail.setRotationPoint(-5F, -11F, 26.6F);
        tail.mirror = true;
        setRotation(tail, -0.9948377F, 0F, 0F);
        tail_2 = new ModelRenderer(this, 31, 2);
        tail_2.addBox(0F, 0F, 0F, 12, 9, 1);
        tail_2.setRotationPoint(-5F, -4F, 23F);
        tail_2.mirror = true;
        setRotation(tail_2, -1.308997F, 0F, 0F);
        tail_3 = new ModelRenderer(this, 31, 2);
        tail_3.addBox(0F, 0F, 0F, 12, 9, 1);
        tail_3.setRotationPoint(-5F, -1F, 23F);
        tail_3.mirror = true;
        setRotation(tail_3, -1.37881F, 0F, 0F);

        // x = wide    + = left
        // y = height   + = down
        // z = length  + = back

        rightWing = new ModelRenderer(this, 40, 37);
        leftWing = new ModelRenderer(this, 40, 37);

        rightWing.addBox(-1F, 0F, -3F, 1, 10, 16);
        rightWing.setRotationPoint(-5F, -7F, 0F);
        rightWing.mirror = true;
        setRotation(rightWing, 0F, -0.0174533F, 0F);

        leftWing.addBox(-1F, 0F, -3F, 1, 10, 16);
        leftWing.setRotationPoint(8F, -7F, 0F);
        leftWing.mirror = true;
        setRotation(leftWing, 0F, 0.0174533F, 0F);

        // right leg
        rightThigh = new ModelRenderer(this, 20, 27);
        rightShin = new ModelRenderer(this, 17, 2);
        talonRB = new ModelRenderer(this, 111, 22);
        talonRR = new ModelRenderer(this, 67, 20);
        talonRL = new ModelRenderer(this, 67, 20);

        rightThigh.addBox(-1F, 0F, -3F, 4, 9, 4);
        rightThigh.setRotationPoint(-4F, 4F, 5F);
        rightThigh.mirror = true;

        rightShin.addBox(-1F, 8F, 1F, 3, 9, 3);
        rightShin.setRotationPoint(-4F, 4F, 5F);
        rightShin.mirror = true;

        talonRR.addBox(-1F, 14F, -14F, 2, 2, 7);
        talonRR.setRotationPoint(-4F, 4F, 5F);
        talonRR.mirror = true;

        talonRL.addBox(0.5F, 14F, -14F, 2, 2, 7);
        talonRL.setRotationPoint(-4F, 4F, 5F);
        talonRL.mirror = true;

        talonRB.addBox(-0.5F, 13F, 9F, 2, 2, 4);
        talonRB.setRotationPoint(-4F, 4F, 5F);
        talonRB.mirror = true;

        leftThigh = new ModelRenderer(this, 20, 27);
        leftShin = new ModelRenderer(this, 17, 2);
        talonLL = new ModelRenderer(this, 67, 20);
        talonLR = new ModelRenderer(this, 67, 20);
        talonLB = new ModelRenderer(this, 111, 22);

        leftThigh.addBox(-1F, 0F, -3F, 4, 9, 4);
        leftThigh.setRotationPoint(4F, 4F, 5F);
        leftThigh.mirror = true;

        leftShin.addBox(0F, 8F, 1F, 3, 9, 3);
        leftShin.setRotationPoint(4F, 4F, 5F);
        leftShin.mirror = true;

        talonLR.addBox(0F, 14F, -14F, 2, 2, 7);
        talonLR.setRotationPoint(4F, 4F, 5F);
        talonLR.mirror = true;

        talonLL.addBox(1.5F, 14F, -14F, 2, 2, 7);
        talonLL.setRotationPoint(4F, 4F, 5F);
        talonLL.mirror = true;

        talonLB.addBox(0.5F, 13F, 9F, 2, 2, 4);
        talonLB.setRotationPoint(4F, 4F, 5F);
        talonLB.mirror = true;

        this.setRightLegXRotation(0.0F);
        this.setLeftLegXRotation(0.0F);

        // child
        this.textureWidth = 64;
        this.textureHeight = 32;
        child_head = new ModelRenderer(this, 0, 0);
        child_head.addBox(-1.5F, -3F, -1.5F, 3, 3, 3, 0.0F);
        child_head.setRotationPoint(0.0F, 18F, -2.5F);
        child_body = new ModelRenderer(this, 0, 6);
        child_body.addBox(-2F, -2.5F, -2F, 4, 4, 4, 0.0F);
        child_body.setRotationPoint(0.0F, 20F, 0.0F);
        child_rightleg = new ModelRenderer(this, 12, 0);
        child_rightleg.addBox(-0.5F, 0.0F, -1F, 1, 2, 1, 0.0F);
        child_rightleg.setRotationPoint(-1F, 22F, 0.5F);
        child_leftleg = new ModelRenderer(this, 12, 0);
        child_leftleg.addBox(-0.5F, 0.0F, -1F, 1, 2, 1, 0.0F);
        child_leftleg.setRotationPoint(1.0F, 22F, 0.5F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        if(!(entity instanceof EntityChocobo)) return;

        if(((EntityChocobo) entity).isChild())
        {
            child_head.render(scale);
            child_body.render(scale);
            child_rightleg.render(scale);
            child_leftleg.render(scale);
        }
        else
        {
            head.render(scale);
            feather.render(scale);
            feather_2.render(scale);

            neck.render(scale);
            body.render(scale);
            leftSaddleBag.render(scale);
            rightSaddleBag.render(scale);
            packBag.render(scale);

            rightWing.render(scale);
            leftWing.render(scale);

            tail.render(scale);
            tail_2.render(scale);
            tail_3.render(scale);

            rightThigh.render(scale);
            leftThigh.render(scale);

            rightShin.render(scale);
            leftShin.render(scale);

            talonRB.render(scale);
            talonRR.render(scale);
            talonRL.render(scale);

            talonLL.render(scale);
            talonLR.render(scale);
            talonLB.render(scale);
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity)
    {
        if(!(entity instanceof EntityChocobo)) return;

        if(((EntityChocobo) entity).isChild())
        {
            child_head.rotateAngleX = -(headPitch / 57.29578F);
            child_head.rotateAngleY = netHeadYaw / 57.29578F;
            child_rightleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            child_leftleg.rotateAngleY = MathHelper.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
        }
        else
        {
            // ageInTicks = wing z movement (flutter)
            // netHeadYaw = head y movement
            // headPitch = head x movement
            // cos(limbSwing) and limbSwingAmount = leg movement

            float pi = (float)Math.PI;

            // head/neck movement
            head.rotateAngleX = headPitch * (pi/180F);
            head.rotateAngleY = netHeadYaw * (pi/180F);
            neck.rotateAngleX = 0.0F;
            neck.rotateAngleY = head.rotateAngleY;
            feather.rotateAngleX = head.rotateAngleX + 0.1745329F;
            feather.rotateAngleY = head.rotateAngleY;
            feather_2.rotateAngleX = head.rotateAngleX;
            feather_2.rotateAngleY = head.rotateAngleY;


            // walking animation
            this.setRightLegXRotation(MathHelper.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount);
            this.setLeftLegXRotation(MathHelper.cos(limbSwing * 0.6662F + pi) * 0.8F * limbSwingAmount);

            // flying animation
            if (Math.abs(entity.motionY) > 0.1F || !entity.onGround)
            {
                setRotation(rightWing, (pi/2F) - (pi/12), -0.0174533F, -90 + MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
                setRotation(leftWing, (pi/2F) - (pi/12), 0.0174533F, 90 + MathHelper.cos(limbSwing * 0.6662F + pi) * 1.4F * limbSwingAmount);
                this.setLeftLegXRotation(0.6F);
                this.setRightLegXRotation(0.6F);
            }
            else
            {
                // reset wings
                setRotation(rightWing, 0F, -0.0174533F, 0F);
                setRotation(leftWing, 0F, 0.0174533F, 0F);
            }
        }
    }

    private void setLeftLegXRotation(float deltaX)
    {
        setRotation(leftThigh, 0.2094395F + deltaX, 0F, 0F);
        setRotation(leftShin, -0.1919862F + deltaX, 0F, 0F);
        setRotation(talonLR, 0.3490659F + deltaX, 0.1570796F, 0F);
        setRotation(talonLL, 0.3490659F + deltaX, -0.1919862F, 0F);
        setRotation(talonLB, -0.5235988F + deltaX, 0F, 0F);
    }

    private void setRightLegXRotation(float deltaX)
    {
        setRotation(rightThigh, 0.2094395F + deltaX, 0F, 0F);
        setRotation(rightShin, -0.1919862F + deltaX, 0F, 0F);
        setRotation(talonRR, 0.3490659F + deltaX, 0.1919862F, 0F);
        setRotation(talonRL, 0.3490659F + deltaX, -0.1570796F, 0F);
        setRotation(talonRB, -0.5235988F + deltaX, 0F, 0F);
    }
}
