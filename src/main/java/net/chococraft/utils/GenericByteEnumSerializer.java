package net.chococraft.utils;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

public class GenericByteEnumSerializer<E extends Enum<E>> implements DataSerializer<E>
{
    private E[] values;

    public GenericByteEnumSerializer(E[] values)
    {
        this.values = values;
    }

    @Override
    public void write(PacketBuffer buf, E value)
    {
        buf.writeByte(value.ordinal());
    }

    @Override
    public E read(PacketBuffer buf) throws IOException
    {
        return values[buf.readByte()];
    }

    @Override
    public DataParameter<E> createKey(int id)
    {
        return new DataParameter<>(id, this);
    }

    @Override
    public E copyValue(E value)
    {
        return value;
    }
}
