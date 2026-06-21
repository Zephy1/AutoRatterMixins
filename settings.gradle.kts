pluginManagement {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/repository/maven-public")

    plugins {
        id("com.gradleup.shadow") version "9.4.1"
    }
}

includeBuild("../essential-gradle-toolkit")
rootProject.name = "AutoRatterMixins"
rootProject.buildFileName = "root.gradle.kts"

val versionList = listOf(
    "26.1.2-fabric",
    "1.21.11-fabric",
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
