package com.github.tacowasa059.dominomod.common.tileentity;

import com.github.tacowasa059.dominomod.core.init.TileEntityTypesInit;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class DominoTileEntity extends TileEntity {
    public DominoTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }
    public DominoTileEntity(){
        this(TileEntityTypesInit.DOMINO_TILE_ENTITY_TYPE.get());
    }
}
