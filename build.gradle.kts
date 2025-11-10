plugins {
    kotlin("jvm")
    id("maven-publish")
    id("com.github.johnrengelman.shadow")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
}

version = property("mod_version").toString()
group = property("mod_group").toString()

configurations.all {
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-jvm")
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-core-jvm")
}

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
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.fabricmc.net")
    maven("https://maven.architectury.dev")
    maven("https://maven.minecraftforge.net")
    maven("https://repo.essential.gg/repository/maven-public")
    maven("https://repo.spongepowered.org/maven/")
    maven("https://repo.legacyfabric.net/repository/legacyfabric/")
}

dependencies {
    if (project.platform.mcVersion == 10809) {
        modCompileOnly("gg.essential:essential-$platform:4167+g4594ad6e6")
//        embed("gg.essential:loader-launchwrapper:1.2.3")
        compileOnly("org.spongepowered:mixin:0.7.11-SNAPSHOT")
    } else if (project.platform.mcVersion >= 12100) {
        when (project.platform.mcVersion) {
            12105 -> {
                modCompileOnly("gg.essential:universalcraft-1.21.5-fabric:436")
                modImplementation("net.fabricmc.fabric-api:fabric-api:0.128.2+1.21.5")
            }
            12108 -> {
                modCompileOnly("gg.essential:universalcraft-1.21.7-fabric:436")
                modImplementation("net.fabricmc.fabric-api:fabric-api:0.136.0+1.21.8")
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
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.platform.mcVersionStr)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.platform.mcVersionStr,
        )
    }
}

tasks.named<Jar>("jar") {
    if (project.platform.mcVersion < 12100) {
        from(sourceSets.main.get().output)

        manifest {
            attributes(
                "FMLCorePlugin" to "org.zephy.autoratter.ColorMixinLoader",
                "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
                "FMLAT" to "autorattermixins_at.cfg",
                "MixinConfigs" to "autoratter.legacy.mixins.json"
            )
        }

        exclude(
            "autoratter.modern.mixins.json",
        )
    }

    exclude(
        "META-INF/maven/**",
        "META-INF/*.SF",
        "META-INF/*.RSA",
        "META-INF/*.DSA",
        "**/*.java",
        "org/spongepowered/**"
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