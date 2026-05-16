pluginManagement {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://repo.spongepowered.org/maven/")
        maven("https://repo.legacyfabric.net/repository/legacyfabric/")
    }

    plugins {
        id("com.gradleup.shadow") version "9.4.1"
    }
}

includeBuild("../essential-gradle-toolkit")
rootProject.name = "AutoRatterMixins"
rootProject.buildFileName = "root.gradle.kts"

val versionList = listOf(
    "1.8.9-forge",
    "1.12.2-forge",
    "1.12.2-fabric",
    "1.16.2-fabric",
    "1.21.10-fabric",
    "1.21.11-fabric",
    "26.1.2-fabric",
)
versionList.forEach { version ->
    file("versions/$version").mkdirs()
}

versionList.forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}
