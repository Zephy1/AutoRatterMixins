plugins {
    kotlin("jvm")
    id("maven-publish")
    id("com.github.johnrengelman.shadow")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
}

version = property("mod_version").toString()
group = property("mod_group").toString()

val embed by configurations.creating
configurations.implementation.get().extendsFrom(embed)

loom {
	mixin {
		useLegacyMixinAp.set(true)
		defaultRefmapName.set("autoratter.mixins.refmap.json")
	}

	runConfigs {
		named("client") {
			ideConfigGenerated(true)
			programArgs("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
		}
	}
}

repositories {
	maven("https://files.minecraftforge.net/maven")
	maven("https://repo.spongepowered.org/maven") {
		name = "sponge"
	}
}

dependencies {
    if (project.platform.mcVersion <= 12100) {
//        modCompileOnly("gg.essential:essential-$platform:4167+g4594ad6e6")
//        embed("gg.essential:loader-launchwrapper:1.2.3")
        if (project.platform.mcVersion == 10809) {
            embed("org.spongepowered:mixin:0.7.11-SNAPSHOT")
        }
    } else {
        when (project.platform.mcVersion) {
            12105 -> {
                modCompileOnly("gg.essential:universalcraft-1.21.5-fabric:436")
                modImplementation("net.fabricmc.fabric-api:fabric-api:0.128.2+1.21.5")
            }
            12106 -> {
                modCompileOnly("gg.essential:universalcraft-1.21.6-fabric:436")
                modImplementation("net.fabricmc.fabric-api:fabric-api:0.128.2+1.21.6")
            }
            12107 -> {
                modCompileOnly("gg.essential:universalcraft-1.21.7-fabric:436")
                modImplementation("net.fabricmc.fabric-api:fabric-api:0.129.0+1.21.7")
            }
            12108 -> {
                modCompileOnly("gg.essential:universalcraft-1.21.7-fabric:436")
                modImplementation("net.fabricmc.fabric-api:fabric-api:0.136.0+1.21.8")
            }
            12109 -> {
                modCompileOnly("gg.essential:universalcraft-1.21.9-fabric:436")
                modImplementation("net.fabricmc.fabric-api:fabric-api:0.134.0+1.21.9")
            }
            12110 -> {
                modCompileOnly("gg.essential:universalcraft-1.21.9-fabric:436")
                modImplementation("net.fabricmc.fabric-api:fabric-api:0.136.0+1.21.10")
            }
//            12111 -> {
//                modCompileOnly("gg.essential:universalcraft-1.21.7-fabric:427")
//                modImplementation("net.fabricmc.fabric-api:fabric-api:0.133.0+1.21.8")
//            }
            else -> throw IllegalStateException("Unsupported MC version: ${project.platform.mcVersion}")
        }
        modCompileOnly("gg.essential:elementa:712")
        modImplementation("net.fabricmc:fabric-loader:0.17.3")
        modImplementation("net.fabricmc:fabric-language-kotlin:1.12.3+kotlin.2.0.21")
    }

//	if (project.platform.mcVersion == 12105) {
//		minecraft("com.mojang:minecraft:${project.extra["minecraft_version"]}")
//		mappings("net.fabricmc:yarn:${project.extra["yarn_mappings"]}:v2")
//		modImplementation("net.fabricmc:fabric-loader:${project.extra["loader_version"]}")
//		modImplementation("net.fabricmc.fabric-api:fabric-api:${project.extra["fabric_version"]}")
//		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//
//		modImplementation("gg.essential:universalcraft-1.21.5-fabric:${project.extra["universalcraft_version"]}") {
//			exclude(group = "gg.essential", module = "universalcraft")
//		}
//		modImplementation("gg.essential:elementa:${project.extra["elementa_version"]}")
//	}
//
//	if (project.platform.mcVersion == 10809) {
//		embed("org.spongepowered:mixin:0.7.11-SNAPSHOT")
//	}
}

tasks.processResources {
	inputs.property("version", project.version)

	if (project.platform.mcVersion >= 12100) {
		inputs.property("minecraft_version", project.platform.mcVersionStr)
		filteringCharset = "UTF-8"

		filesMatching("fabric.mod.json") {
			expand(
				"version" to project.version,
				"minecraft_version" to project.platform.mcVersionStr,
			)
		}
	}
}

tasks.named<Jar>("jar") {
	if (project.platform.mcVersion < 12100) {
		from(sourceSets.main.get().output)

		manifest {
			attributes(
				"FMLCorePlugin" to "org.zephy.autoratter.ColorMixinLoader",
				"TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
				"FMLAT" to "autorattermixins_at.cfg",
				"MixinConfigs" to "autoratter.legacy.mixins.json"
			)
		}
	}

    val embedConfig = configurations.getByName("embed")
    from(embedConfig.map {
        if (it.isDirectory) it else zipTree(it).matching {
            exclude("META-INF/*.SF", "META-INF/*.RSA", "META-INF/*.DSA")
        }
    })

    exclude(
        "META-INF/maven/**",
        "META-INF/*.SF",
        "META-INF/*.RSA",
        "META-INF/*.DSA",
        "**/*.java"
    )
}

tasks.register<Copy>("collectJars") {
    group = "build"
    description = "Copies this version’s non-shadowed JARs to main/jars"

    val outputDir = projectDir.resolve("../../jars").normalize()
    dependsOn("remapJar")

    from(tasks.named("remapJar")) {
        include("*.jar")
        exclude("*-all.jar")

        exclude { fileTreeElement ->
            fileTreeElement.name.contains(" 1.1")
        }

        rename { fileName ->
            fileName
                .replace(".jar", ".unloaded")
                .replace("-forge", "")
                .replace("-fabric", "")
                .replace(" ", "-")
        }
    }
    into(outputDir)
}
tasks.named("build") {
    finalizedBy("collectJars")
}