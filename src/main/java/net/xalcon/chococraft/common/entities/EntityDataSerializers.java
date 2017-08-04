package net.xalcon.chococraft.common.entities;

import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public class EntityDataSerializers
{
    public final static DataSerializer<EntityChocobo.ChocoboColor> CHOCOBO_COLOR = new GenericByteEnumSerializer<>(EntityChocobo.ChocoboColor.values());
    public final static DataSerializer<EntityChocobo.BagType> BAG_TYPE = new GenericByteEnumSerializer<>(EntityChocobo.BagType.values());
    public final static DataSerializer<EntityChocobo.MovementType> MOVEMENT_TYPE = new GenericByteEnumSerializer<>(EntityChocobo.MovementType.values());

    static
    {
        DataSerializers.registerSerializer(CHOCOBO_COLOR);
        DataSerializers.registerSerializer(BAG_TYPE);
        DataSerializers.registerSerializer(MOVEMENT_TYPE);
    }
}
