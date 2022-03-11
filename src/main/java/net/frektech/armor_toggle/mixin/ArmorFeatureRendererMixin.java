package net.frektech.armor_toggle.mixin;

import net.frektech.armor_toggle.Main;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
	public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> context) {
		super(context);
	}

	@Shadow
	private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model) {}

	@Shadow
	private A getArmor(EquipmentSlot slot) {
		return null;
	}


	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void renderMixin(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (!Main.data.getOrDefault("mod", false)) {
			return; // Mod disabled, let vanilla mechanics take care of armor rendering.
		}

		if (Main.data.getOrDefault("chestplate", false)) {
			this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.CHEST, i, this.getArmor(EquipmentSlot.CHEST));
		}

		if (Main.data.getOrDefault("leggings", false)){
			this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.LEGS, i, this.getArmor(EquipmentSlot.LEGS));
		}

		if (Main.data.getOrDefault("boots", false)) {
			this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.FEET, i, this.getArmor(EquipmentSlot.FEET));
		}

		if (Main.data.getOrDefault("helmet", false)) {
			this.renderArmor(matrixStack, vertexConsumerProvider, livingEntity, EquipmentSlot.HEAD, i, this.getArmor(EquipmentSlot.HEAD));
		}

		ci.cancel();
	}
}
