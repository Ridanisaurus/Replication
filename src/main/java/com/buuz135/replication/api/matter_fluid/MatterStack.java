package com.buuz135.replication.api.matter_fluid;

import com.buuz135.replication.ReplicationRegistry;
import com.buuz135.replication.api.IMatterType;
import com.buuz135.replication.api.MatterType;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class MatterStack {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final MatterStack EMPTY = new MatterStack(MatterType.EMPTY, 0);


    private boolean isEmpty;
    private int amount;
    private Holder.Reference<IMatterType> matterDelegate;

    public MatterStack(IMatterType matter, int amount)
    {
        if (matter == null)
        {
            LOGGER.fatal("Null fluid supplied to matterstack. Did you try and create a stack for an unregistered matter?");
            throw new IllegalArgumentException("Cannot create a matterstack from a null fluid");
        }else if (ReplicationRegistry.MATTER_TYPES_REGISTRY.get().getKey(matter) == null)
        {
            LOGGER.fatal("Failed attempt to create a FluidStack for an unregistered Fluid {} (type {})", ReplicationRegistry.MATTER_TYPES_REGISTRY.get().getKey(matter), matter.getClass().getName());
            throw new IllegalArgumentException("Cannot create a fluidstack from an unregistered fluid");
        }
        this.matterDelegate = ReplicationRegistry.MATTER_TYPES_REGISTRY.get().getDelegateOrThrow(matter);
        this.amount = amount;

        updateEmpty();
    }

    public MatterStack(MatterStack matterStack, int amount)
    {
        this(matterStack.getMatterType(), amount);
    }

    /**
     * This provides a safe method for retrieving a FluidStack - if the Fluid is invalid, the stack
     * will return as null.
     */
    public static MatterStack loadFluidStackFromNBT(CompoundTag nbt)
    {
        if (nbt == null)
        {
            return EMPTY;
        }
        if (!nbt.contains("MatterName", Tag.TAG_STRING))
        {
            return EMPTY;
        }

        ResourceLocation matterName = new ResourceLocation(nbt.getString("MatterName"));
        IMatterType matterType = ReplicationRegistry.MATTER_TYPES_REGISTRY.get().getValue(matterName);
        if (matterType == null)
        {
            return EMPTY;
        }
        MatterStack stack = new MatterStack(matterType, nbt.getInt("Amount"));

        return stack;
    }

    public CompoundTag writeToNBT(CompoundTag nbt)
    {
        nbt.putString("FluidName", ReplicationRegistry.MATTER_TYPES_REGISTRY.get().getKey(getMatterType()).toString());
        nbt.putInt("Amount", amount);
        return nbt;
    }

    public void writeToPacket(FriendlyByteBuf buf)
    {
        buf.writeRegistryId(ReplicationRegistry.MATTER_TYPES_REGISTRY.get(), getMatterType());
        buf.writeVarInt(getAmount());
    }

    public static MatterStack readFromPacket(FriendlyByteBuf buf)
    {
        IMatterType fluid = buf.readRegistryId();
        int amount = buf.readVarInt();
        if (fluid == MatterType.EMPTY) return EMPTY;
        return new MatterStack(fluid, amount);
    }

    public final IMatterType getMatterType()
    {
        return isEmpty ? MatterType.EMPTY : matterDelegate.get();
    }

    public final IMatterType getRawMatter()
    {
        return matterDelegate.get();
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    protected void updateEmpty() {
        isEmpty = getRawMatter() == MatterType.EMPTY || amount <= 0;
    }

    public int getAmount()
    {
        return isEmpty ? 0 : amount ;
    }

    public void setAmount(int amount)
    {
        if (getRawMatter() == MatterType.EMPTY) throw new IllegalStateException("Can't modify the empty stack.");
        this.amount = amount;
        updateEmpty();
    }

    public void grow(int amount) {
        setAmount(this.amount + amount);
    }

    public void shrink(int amount) {
        setAmount(this.amount - amount);
    }

    public Component getDisplayName()
    {
        return Component.literal(this.getMatterType().getName());
    }

    public String getTranslationKey()
    {
        return this.getMatterType().getName();
    }

    /**
     * @return A copy of this FluidStack
     */
    public MatterStack copy()
    {
        return new MatterStack(getMatterType(), amount);
    }

    /**
     * Determines if the FluidIDs and NBT Tags are equal. This does not check amounts.
     *
     * @param other
     *            The FluidStack for comparison
     * @return true if the Fluids (IDs and NBT Tags) are the same
     */
    public boolean isMatterEqual(@NotNull MatterStack other)
    {
        return getMatterType() == other.getMatterType();
    }

    public boolean containsMatter(@NotNull MatterStack other)
    {
        return isMatterEqual(other) && amount >= other.amount;
    }
    public boolean isMatterStackIdentical(MatterStack other)
    {
        return isMatterEqual(other) && amount == other.amount;
    }

    public boolean isMatterEqual(@NotNull ItemStack other)
    {
        // TODO return FluidUtil.getFluidContained(other).map(this::isFluidEqual).orElse(false);
        return false;
    }

    @Override
    public final int hashCode()
    {
        int code = 1;
        code = 31*code + getMatterType().hashCode();
        code = 31*code + amount;
        return code;
    }

    /**
     * Default equality comparison for a FluidStack. Same functionality as isFluidEqual().
     *
     * This is included for use in data structures.
     */
    @Override
    public final boolean equals(Object o)
    {
        if (!(o instanceof MatterStack))
        {
            return false;
        }
        return isMatterEqual((MatterStack) o);
    }

}
