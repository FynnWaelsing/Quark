package vazkii.quark.content.tools.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.QuarkModule;

public class AbacusItem extends QuarkItem {

	public static final String TAG_POS_X = "boundPosX";
	public static final String TAG_POS_Y = "boundPosY";
	public static final String TAG_POS_Z = "boundPosZ";

	public static int MAX_COUNT = 48;

	public AbacusItem(QuarkModule module) {
		super("abacus", module, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getItemInHand();
		BlockPos curr = getBlockPos(stack);
		if(curr != null)
			setBlockPos(stack, null);
		else setBlockPos(stack, context.getClickedPos());

		return InteractionResult.SUCCESS;
	}

	public static void setBlockPos(ItemStack stack, BlockPos pos) {
		if(pos == null)
			ItemNBTHelper.setInt(stack, TAG_POS_Y, -1);
		else {
			ItemNBTHelper.setInt(stack, TAG_POS_X, pos.getX());
			ItemNBTHelper.setInt(stack, TAG_POS_Y, pos.getY());
			ItemNBTHelper.setInt(stack, TAG_POS_Z, pos.getZ());
		}
	}

	public static BlockPos getBlockPos(ItemStack stack) {
		int y = ItemNBTHelper.getInt(stack, TAG_POS_Y, -1);
		if(y == -1)
			return null;

		int x = ItemNBTHelper.getInt(stack, TAG_POS_X, 0);
		int z = ItemNBTHelper.getInt(stack, TAG_POS_Z, 0);
		return new BlockPos(x, y, z);
	}

	public static int getCount(ItemStack stack, BlockPos target, Level world) {
		BlockPos pos = getBlockPos(stack); 

		if(pos != null && !world.isEmptyBlock(target))
			return target.distManhattan(pos);

		return -1;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static int getCount(ItemStack stack, LivingEntity entityIn) {
		int count = -1;
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if(entityIn == player) {
			HitResult result = mc.hitResult;
			if(result instanceof BlockHitResult) {
				BlockPos target = ((BlockHitResult) result).getBlockPos();
				count = getCount(stack, target, player.level);
			}
		}
	
		return count;
	}

	@OnlyIn(Dist.CLIENT)
	public static float count(ItemStack stack, ClientLevel world, LivingEntity entityIn, int id) {
		int count = getCount(stack, entityIn);
		if(count == -1)
			return 9999;

		return 0.01F * count + 0.005F;
	}

}
