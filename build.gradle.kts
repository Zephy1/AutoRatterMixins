plugins {
    kotlin("jvm")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
}

version = property("mod_version").toString()
group = property("mod_group").toString()

tasks {
    processResources {
        val version = project.version
        val minecraftVersion = project.platform.mcVersionStr
        val javaVersion = project.java.toolchain.languageVersion.get().asInt()
        val minFabricApiVersion = project.findProperty("min-fabric-api")?.toString()

        inputs.property("version", version)
        inputs.property("minecraft_version", minecraftVersion)
        inputs.property("min_fabric_api_version", minFabricApiVersion.toString())
        filesMatching("fabric.mod.json") {
            expand(mapOf(
                "version" to project.version,
                "minecraft_version" to minecraftVersion,
                "min_fabric_api_version" to minFabricApiVersion,
            ))
        }

        inputs.property("javaVersion", javaVersion)
        filesMatching("autoratter.mixins.json") {
            filter { line ->
                line.replace("JAVA_\$javaVersion", "JAVA_$javaVersion")
            }
        }
    }
}

afterEvaluate {
    val hasRemapJar = tasks.findByName("remapJar") != null
    val outputTaskName = if (hasRemapJar) "remapJar" else "jar"

    tasks.register<Copy>("collectJars") {
        group = "build"
        description = "Copies this version's non-shadowed JARs to main/jars"

        val outputDir = projectDir.resolve("../../jars").normalize()
        dependsOn(outputTaskName)

        from(tasks.named(outputTaskName)) {
            include("*.jar")
            exclude { it.name.contains(" 1.2") && it.name.contains("-all") }
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
