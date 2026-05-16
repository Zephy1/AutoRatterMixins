plugins {
    kotlin("jvm") version "2.3.10" apply false // Don't bump, depends on preprocessor
    id("gg.essential.multi-version.root")
}

preprocess.strictExtraMappings.set(true)
preprocess {
    val fabric26_01_02 = createNode("26.1.2-fabric", 26_01_02, "srg")
    val fabric12111 = createNode("1.21.11-fabric", 12111, "yarn")
    val fabric12110 = createNode("1.21.10-fabric", 12110, "yarn")
    val fabric11602 = createNode("1.16.2-fabric", 11602, "intermediary")
    val fabric11202 = createNode("1.12.2-fabric", 11202, "intermediary")
    val forge11202 = createNode("1.12.2-forge", 11202, "intermediary")
    val forge10809 = createNode("1.8.9-forge", 10809, "mcp")

    fabric26_01_02.link(fabric12111)
    fabric12111.link(fabric12110)
    fabric12110.link(fabric11602)
    fabric11602.link(fabric11202)
    fabric11202.link(forge11202)
    forge11202.link(forge10809)
}
