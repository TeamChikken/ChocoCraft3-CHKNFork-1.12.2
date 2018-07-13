package net.slayer5934.chococraft.utils.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.tileentity.TileEntity;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AttachedTileEntity
{
    String name();
    Class<? extends TileEntity> tile();
}
