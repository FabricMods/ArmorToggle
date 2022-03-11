package net.frektech.armor_toggle;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Main implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("armor_toggle");
	private static KeyBinding toggleArmorVisibilityBinding;

	private static KeyBinding toggleHeadGearBinding;
	private static KeyBinding toggleChestGearBinding;
	private static KeyBinding toggleLegsGearBinding;
	private static KeyBinding toggleFeetGearBinding;

	private static KeyBinding toggleElytraGearBinding;

	public static Map<String, Boolean> data;

	private static String configPath = "armor-toggle-keys.properties";

	@Override
	public void onInitialize() {
		LOGGER.info("Armor Toggle V1.0.0 is loading...");

		data = PersistentVariables.loadData(configPath);
		if (data == null) {
			data = new HashMap<String, Boolean>() {{
				put("mod", false);

				put("helmet", false);
				put("chestplate", false);
				put("leggings", false);
				put("boots", false);

				put("elytra", false);
			}};
		}

		toggleArmorVisibilityBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.armor_toggle.toggle",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_U,
				"category.armor_toggle"
		));

		toggleHeadGearBinding = emptyBinding("key.armor_toggle.toggle_head");
		toggleChestGearBinding = emptyBinding("key.armor_toggle.toggle_chest");
		toggleLegsGearBinding = emptyBinding("key.armor_toggle.toggle_legs");
		toggleFeetGearBinding = emptyBinding("key.armor_toggle.toggle_feet");

		toggleElytraGearBinding = emptyBinding("key.armor_toggle.toggle_elytra");

		//MinecraftClient minecraftCient = MinecraftClient.getInstance();
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleArmorVisibilityBinding.wasPressed()) { toggleGear(client, "mod"); }

			while (toggleHeadGearBinding.wasPressed()) { toggleGear(client, "helmet"); }
			while (toggleChestGearBinding.wasPressed()) { toggleGear(client, "chestplate"); }
			while (toggleLegsGearBinding.wasPressed()) { toggleGear(client, "leggings"); }
			while (toggleFeetGearBinding.wasPressed()) { toggleGear(client, "boots"); }

			while (toggleElytraGearBinding.wasPressed()) { toggleGear(client, "elytra"); }
		});

		LOGGER.info("Armor Toggle - Loaded!");
	}

	public static void toggleGear(MinecraftClient client, String name) {
		if (!data.containsKey(name)) {
			data.put(name, false);
		}

		data.replace(name, !data.get(name));
		PersistentVariables.saveData(configPath, data);

		String status = "§2Enabled";
		if (!data.get(name)) { status = "§4Disabled"; }

		client.inGameHud.addChatMessage(MessageType.SYSTEM, new LiteralText("§6[ArmorToggle] §7" + StringUtils.capitalize(name) + ": " + status), client.player.getUuid());
	}


	private static KeyBinding emptyBinding(String translationKey) {
		return KeyBindingHelper.registerKeyBinding(new KeyBinding(
				translationKey,
				InputUtil.Type.KEYSYM,
				-1,
				"category.armor_toggle"
		));
	}
}
