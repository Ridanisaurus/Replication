package com.buuz135.replication.network.element.type;

import com.buuz135.replication.Replication;
import com.buuz135.replication.api.INetworkDirectionalConnection;
import com.buuz135.replication.api.network.NetworkElement;
import com.buuz135.replication.block.tile.NetworkBlockEntity;
import com.buuz135.replication.network.Network;
import com.buuz135.replication.network.element.NetworkElementFactory;
import com.buuz135.replication.network.matter.MatterNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class DefaultMatterNetworkElement extends NetworkElement {

    public static final ResourceLocation ID = new ResourceLocation(Replication.MOD_ID, "default_matter");

    public DefaultMatterNetworkElement(Level level, BlockPos pos) {
        super(level, pos);
    }

    @Override
    public void joinNetwork(Network network) {
        super.joinNetwork(network);
        if (network instanceof MatterNetwork matterNetwork){
            matterNetwork.addElement(this);
        }
    }

    @Override
    public void leaveNetwork() {
        if (this.network instanceof MatterNetwork matterNetwork){
            matterNetwork.removeElement(this);
        }
        super.leaveNetwork();
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public ResourceLocation getNetworkType() {
        return MatterNetwork.MATTER;
    }

    @Override
    public boolean canConnectFrom(Direction direction) {
        var state = this.level.getBlockState(this.pos);
        if (state.getBlock() instanceof INetworkDirectionalConnection networkDirectionalConnection){
            return networkDirectionalConnection.canConnect(state, direction);
        }
        return true;
    }

    public static class Factory implements NetworkElementFactory {

        @Override
        public NetworkElement createFromNbt(Level level, CompoundTag tag) {
            BlockPos pos = BlockPos.of(tag.getLong("pos"));
            DefaultMatterNetworkElement element = new DefaultMatterNetworkElement(level, pos);
            return element;
        }
    }
}
