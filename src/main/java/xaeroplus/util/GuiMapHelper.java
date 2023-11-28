package xaeroplus.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import xaero.map.gui.GuiMap;
import xaero.map.gui.GuiSettings;
import xaeroplus.mixin.client.MixinGuiMapAccessor;

import java.util.Optional;

public class GuiMapHelper {
    public static Optional<GuiMap> getGuiMap() {
        Screen currentScreen = MinecraftClient.getInstance().currentScreen;
        if (currentScreen instanceof GuiMap) {
            return Optional.of((GuiMap) currentScreen);
        } else if (currentScreen instanceof GuiSettings screen) {
            if (screen.parent instanceof GuiMap map)
                return Optional.of(map);
            else if (screen.escape instanceof GuiMap map) {
                return Optional.of(map);
            }
        }
        return Optional.empty();
    }
    public static double getCameraX(final GuiMap guiMap) {
        return ((MixinGuiMapAccessor) guiMap).getCameraX();
    }
    public static double getCameraZ(final GuiMap guiMap) {
        return ((MixinGuiMapAccessor)guiMap).getCameraZ();
    }
    public static double getDestScale(final GuiMap guiMap) {
        return ((MixinGuiMapAccessor) guiMap).getDestScale();
    }

    public static int getGuiMapRegionSize(final GuiMap guiMap) {
        // this is intentionally overestimating as we prefer to have a few more chunks than less
        return (int) Math.max((5.0 / getDestScale(guiMap)), 3);
    }

    public static boolean isGuiMapLoaded() {
        return MinecraftClient.getInstance().currentScreen instanceof GuiMap;
    }

    public static int getGuiMapCenterRegionX(final GuiMap guiMap) {
        return ChunkUtils.coordToRegionCoord(getCameraX(guiMap));
    }

    public static int getGuiMapCenterRegionZ(final GuiMap guiMap) {
        return ChunkUtils.coordToRegionCoord(getCameraZ(guiMap));
    }

}
