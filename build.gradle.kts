plugins {
    kotlin("jvm")
    id("maven-publish")
    id("com.gradleup.shadow")
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
configurations.getByName("implementation").extendsFrom(embed)

repositories {
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
        return@dependencies
    }
}

tasks {
    if (project.platform.mcVersion < 12100) {
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
    }

    shadowJar {
        configurations.set(listOf(embed))
        exclude("gg/essential/**")
    }
    withType<net.fabricmc.loom.task.RemapJarTask>().configureEach {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
    }

    processResources {
        val version = project.version
        val minecraftVersion = project.platform.mcVersionStr

        inputs.property("version", version)
        inputs.property("minecraft_version", minecraftVersion)
        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version,
                "minecraft_version" to minecraftVersion,
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

afterEvaluate {
    val hasRemapJar = tasks.findByName("remapJar") != null
    val outputTaskName = if (hasRemapJar) "remapJar" else "shadowJar"

    tasks.register<Copy>("collectJars") {
        group = "build"
        description = "Copies this version's non-shadowed JARs to main/jars"

        val outputDir = projectDir.resolve("../../jars").normalize()
        dependsOn(outputTaskName)

        from(tasks.named(outputTaskName)) {
            include("*.jar")
            exclude { it.name.contains(" 1.1")}
            rename {
                "${rootProject.name}+${project.platform.mcVersionStr}.jar"
            }
        }
        into(outputDir)
    }

    tasks.named("build") {
        finalizedBy("collectJars")
    }

    configurations.named("default") {
        isCanBeConsumed = true
        isCanBeResolved = false
    }

    artifacts {
        add("default", tasks.named(outputTaskName))
    }
}
