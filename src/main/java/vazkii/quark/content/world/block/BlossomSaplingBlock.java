package vazkii.quark.content.world.block;

import java.util.OptionalInt;
import java.util.Random;
import java.util.function.BooleanSupplier;

import net.minecraft.core.NonNullList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.base.handler.RenderLayerHandler;
import vazkii.quark.base.handler.RenderLayerHandler.RenderTypeSkeleton;
import vazkii.quark.base.module.QuarkModule;

public class BlossomSaplingBlock extends SaplingBlock implements IQuarkBlock {

	private final QuarkModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public BlossomSaplingBlock(String colorName, QuarkModule module, BlossomTree tree, Block leaf) {
		super(tree, Block.Properties.copy(Blocks.OAK_SAPLING));
		this.module = module;

		RegistryHelper.registerBlock(this, colorName + "_blossom_sapling");
		RegistryHelper.setCreativeTab(this, CreativeModeTab.TAB_DECORATIONS);
		tree.sapling = this;
		
		RenderLayerHandler.setRenderType(this, RenderTypeSkeleton.CUTOUT);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if(isEnabled() || group == CreativeModeTab.TAB_SEARCH)
			super.fillItemCategory(group, items);
	}

	@Override
	public QuarkModule getModule() {
		return module;
	}

	@Override
	public BlossomSaplingBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

	public static class BlossomTree extends AbstractTreeGrower {

		public final TreeConfiguration config;
		public final BlockState leaf;
		public BlossomSaplingBlock sapling;

		public BlossomTree(Block leafBlock) { // TODO CONTENT these can be made a lot more interesting with new parameters
			config = (new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(Blocks.SPRUCE_LOG),
					new FancyTrunkPlacer(3, 11, 0), 
					BlockStateProvider.simple(leafBlock), 
					new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4), // <- Copy of what Features.FANCY_OAK uses
					new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))))
					.ignoreVines()
					.build();
			
			leaf = leafBlock.defaultBlockState();
		}

		@Override
		protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random rand, boolean hjskfsd) {
			return Feature.TREE.configured(config);
		}
		
	}

}
