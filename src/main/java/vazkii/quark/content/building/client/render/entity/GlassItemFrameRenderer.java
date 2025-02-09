package vazkii.quark.content.building.client.render.entity;

import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.common.MinecraftForge;
import vazkii.quark.base.Quark;
import vazkii.quark.content.building.entity.GlassItemFrame;

/**
 * @author WireSegal
 * Created at 11:58 AM on 8/25/19.
 */

@OnlyIn(Dist.CLIENT)
public class GlassItemFrameRenderer extends EntityRenderer<GlassItemFrame> {

	private static final ModelResourceLocation LOCATION_MODEL = new ModelResourceLocation(new ResourceLocation(Quark.MOD_ID, "glass_frame"), "inventory");

	private static final List<Direction> SIGN_DIRECTIONS = Arrays.asList(new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST });

	private static BannerBlockEntity banner = new BannerBlockEntity(BlockPos.ZERO, Blocks.WHITE_BANNER.defaultBlockState());
	private final ModelPart bannerModel;

	private final Minecraft mc = Minecraft.getInstance();
	private final ItemRenderer itemRenderer;
	private final ItemFrameRenderer<?> defaultRenderer;

	public GlassItemFrameRenderer(EntityRendererProvider.Context context) {
		super(context);

		ModelPart part = context.bakeLayer(ModelLayers.BANNER);
		this.bannerModel = part.getChild("flag");
		
		Minecraft mc = Minecraft.getInstance();
		this.itemRenderer = mc.getItemRenderer();
		this.defaultRenderer = (ItemFrameRenderer<?>) mc.getEntityRenderDispatcher().renderers.get(EntityType.ITEM_FRAME);
	}

	@Override
	public void render(GlassItemFrame p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource p_225623_5_, int p_225623_6_) {
		super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
		p_225623_4_.pushPose();
		Direction direction = p_225623_1_.getDirection();
		Vec3 Vector3d = this.getRenderOffset(p_225623_1_, p_225623_3_);
		p_225623_4_.translate(-Vector3d.x(), -Vector3d.y(), -Vector3d.z());
		p_225623_4_.translate((double)direction.getStepX() * 0.46875D, (double)direction.getStepY() * 0.46875D, (double)direction.getStepZ() * 0.46875D);
		p_225623_4_.mulPose(Vector3f.XP.rotationDegrees(p_225623_1_.getXRot()));
		p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(180.0F - p_225623_1_.getYRot()));
		BlockRenderDispatcher blockrendererdispatcher = this.mc.getBlockRenderer();
		ModelManager modelmanager = blockrendererdispatcher.getBlockModelShaper().getModelManager();

		ItemStack itemstack = p_225623_1_.getItem();

		if(p_225623_1_.getEntityData().get(GlassItemFrame.IS_SHINY))
			p_225623_6_ = 0xF000F0;

		if (itemstack.isEmpty()) {
			p_225623_4_.pushPose();
			p_225623_4_.translate(-0.5D, -0.5D, -0.5D);
			blockrendererdispatcher.getModelRenderer().renderModel(p_225623_4_.last(), p_225623_5_.getBuffer(Sheets.cutoutBlockSheet()), (BlockState)null, modelmanager.getModel(LOCATION_MODEL), 1.0F, 1.0F, 1.0F, p_225623_6_, OverlayTexture.NO_OVERLAY);
			p_225623_4_.popPose();
		} else {
			renderItemStack(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_, itemstack);
		}

		p_225623_4_.popPose();
	}

	@Override
	public Vec3 getRenderOffset(GlassItemFrame p_225627_1_, float p_225627_2_) {
		return new Vec3((double)((float)p_225627_1_.getDirection().getStepX() * 0.3F), -0.25D, (double)((float)p_225627_1_.getDirection().getStepZ() * 0.3F));
	}

	@Override
	public ResourceLocation getTextureLocation(GlassItemFrame p_110775_1_) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

	@Override
	protected boolean shouldShowName(GlassItemFrame p_177070_1_) {
		if (Minecraft.renderNames() && !p_177070_1_.getItem().isEmpty() && p_177070_1_.getItem().hasCustomHoverName() && this.entityRenderDispatcher.crosshairPickEntity == p_177070_1_) {
			double d0 = this.entityRenderDispatcher.distanceToSqr(p_177070_1_);
			float f = p_177070_1_.isDiscrete() ? 32.0F : 64.0F;
			return d0 < (double)(f * f);
		} else {
			return false;
		}
	}

	@Override
	protected void renderNameTag(GlassItemFrame p_225629_1_, Component p_225629_2_, PoseStack p_225629_3_, MultiBufferSource p_225629_4_, int p_225629_5_) {
		super.renderNameTag(p_225629_1_, p_225629_1_.getItem().getHoverName(), p_225629_3_, p_225629_4_, p_225629_5_);
	}

	protected void renderItemStack(GlassItemFrame itemFrame, float p_225623_2_, float p_225623_3_, PoseStack matrix, MultiBufferSource buff, int p_225623_6_, ItemStack stack) {
		if (!stack.isEmpty()) {
			matrix.pushPose();
			MapItemSavedData mapdata = MapItem.getSavedData(stack, itemFrame.level);

			sign: if(itemFrame.isOnSign()) {
				BlockPos back = itemFrame.getBehindPos();
				BlockState state = itemFrame.level.getBlockState(back);

				Direction ourDirection = itemFrame.getDirection().getOpposite();

				int signRotation = state.getValue(StandingSignBlock.ROTATION);
				Direction signDirection = SIGN_DIRECTIONS.get(signRotation / 4);
				if(signRotation % 4 == 0 ? (signDirection != ourDirection) : (signDirection.getOpposite() == ourDirection))
					break sign;

				int ourRotation = SIGN_DIRECTIONS.indexOf(ourDirection) * 4;
				int rotation = signRotation - ourRotation;
				float angle = -rotation * 22.5F;

				matrix.translate(0, 0.35, 0.8);
				matrix.scale(0.4F, 0.4F, 0.4F);
				matrix.translate(0, 0, 0.5);
				matrix.mulPose(Vector3f.YP.rotationDegrees(angle));
				matrix.translate(0, 0, -0.5);
				matrix.translate(0, 0, -0.085);
			}

			int rotation = mapdata != null ? itemFrame.getRotation() % 4 * 2 : itemFrame.getRotation();
			matrix.mulPose(Vector3f.ZP.rotationDegrees((float) rotation * 360.0F / 8.0F));

			if (!MinecraftForge.EVENT_BUS.post(new RenderItemInFrameEvent(itemFrame, defaultRenderer, matrix, buff, p_225623_6_))) {
				if (mapdata != null) {
					matrix.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
					matrix.scale(0.0078125F, 0.0078125F, 0.0078125F);
					matrix.translate(-64.0F, -64.0F, 62.5F); // <- Use 62.5 instead of 64 to prevent z-fighting
					
					Integer mapID = MapItem.getMapId(stack);
					this.mc.gameRenderer.getMapRenderer().render(matrix, buff, mapID, mapdata, true, p_225623_6_);
				} else {
					float s = 1.5F;
					if (stack.getItem() instanceof BannerItem) {
						banner.fromItem(stack, ((BannerItem) stack.getItem()).getColor());
						List<Pair<BannerPattern, DyeColor>> patterns = banner.getPatterns();

						matrix.pushPose();
						matrix.translate(0.0001F, -0.5001F, 0.55F);
						matrix.scale(0.799999F, 0.399999F, 0.5F);
						BannerRenderer.renderPatterns(matrix, buff, p_225623_6_, OverlayTexture.NO_OVERLAY, bannerModel, ModelBakery.BANNER_BASE, true, patterns);
						matrix.popPose();
					}
					else {
						if (stack.getItem() instanceof ShieldItem) {
							s = 4F;
							matrix.translate(-0.25F, 0F, 0.5F);
							matrix.scale(s, s, s);
						} else {
							matrix.translate(0F, 0F, 0.475F);
							matrix.scale(s, s, s);
						}
						matrix.scale(0.5F, 0.5F, 0.5F);
						this.itemRenderer.renderStatic(stack, ItemTransforms.TransformType.FIXED, p_225623_6_, OverlayTexture.NO_OVERLAY, matrix, buff, 0);
					}
				}
			}

			matrix.popPose();
		}
	}
}
