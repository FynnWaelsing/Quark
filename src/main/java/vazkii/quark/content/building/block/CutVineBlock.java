package vazkii.quark.content.building.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.interf.IBlockColorProvider;
import vazkii.quark.base.block.QuarkVineBlock;
import vazkii.quark.base.module.QuarkModule;

public class CutVineBlock extends QuarkVineBlock implements IBlockColorProvider {

	public CutVineBlock(QuarkModule module) {
		super(module, "cut_vine", false);
	}
	
	@Override
	public boolean canSupportAtFace(BlockGetter level, BlockPos pos, Direction dir) {
		if(dir != Direction.UP) {
            BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(dir);
            BlockState blockstate = level.getBlockState(pos.above());
            return blockstate.is(Blocks.VINE) && blockstate.getValue(booleanproperty);
		}
		
		return super.canSupportAtFace(level, pos, dir);
	}
	
	@Override 
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
		return new ItemStack(Items.VINE);
	}
	
    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockColor getBlockColor() {
        final BlockColors colors = Minecraft.getInstance().getBlockColors();
        final BlockState grass = Blocks.VINE.defaultBlockState();
        return (state, world, pos, tintIndex) -> colors.getColor(grass, world, pos, tintIndex);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemColor getItemColor() {
        final ItemColors colors = Minecraft.getInstance().getItemColors();
        final ItemStack grass = new ItemStack(Items.VINE);
        return (stack, tintIndex) -> colors.getColor(grass, tintIndex);
    }

}
