package vazkii.quark.base.client.config.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import vazkii.quark.api.config.IConfigCategory;
import vazkii.quark.base.client.config.obj.AbstractStringInputObject;
import vazkii.quark.base.client.config.obj.ListObject;

public abstract class AbstractQScreen extends Screen {
	
	private final Screen parent;
	
	public AbstractQScreen(Screen parent) {
		super(new TextComponent(""));
		this.parent = parent;
	}
	
	@Override
	public void render(PoseStack mstack, int mouseX, int mouseY, float pticks) {
		super.render(mstack, mouseX, mouseY, pticks);
	}
	
	public void returnToParent(Button button) {
		minecraft.setScreen(parent);
	}
	
	public OnPress webLink(String url) {
		return b -> Util.getPlatform().openUri(url);
	}
	
	public OnPress categoryLink(IConfigCategory category) {
		return b -> minecraft.setScreen(new CategoryScreen(this, category));
	}
	
	public <T> OnPress stringInput(AbstractStringInputObject<T> object) {
		return b -> minecraft.setScreen(new StringInputScreen<T>(this, object));
	}
	
	public OnPress listInput(ListObject object) {
		return b -> minecraft.setScreen(new ListInputScreen(this, object));
	}

}
