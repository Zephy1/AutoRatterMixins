package org.zephy.autoratter;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8.9")
public class ColorMixinLoader implements IFMLLoadingPlugin
{
    public ColorMixinLoader() {
        System.out.println("ColorMixinLoader initializing...");
        MixinBootstrap.init();
        Mixins.addConfiguration("autoratter.legacy.mixins.json");
        MixinEnvironment.getCurrentEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        System.out.println("ColorMixinLoader initialized!");
    }

    @Override
    public String[] getASMTransformerClass() { return new String[0]; }

    @Override
    public String getModContainerClass() { return null; }

    @Override
    public String getSetupClass() { return null; }

    @Override
    public void injectData(Map data) { }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}