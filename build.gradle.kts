plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.1"
}

dependencies {
    implementation(libs.bundles.jackson)
    implementation("xyz.jpenilla.run-paper:xyz.jpenilla.run-paper.gradle.plugin:2.3.1")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

kotlin {
    explicitApi()
}

group = "com.infernalsuite"
version = "1.0"
gradlePlugin {
    website = "https://infernalsuite.com/"
    vcsUrl = "https://github.com/InfernalSuite/run-paper-ASWMExtension"
    plugins {
        create("com.infernalsuite.run-paper") {
            id = "com.infernalsuite.run-paper"
            displayName = "RunPaper Plugin Extension to support Advanced Slime World Manager server jars"
            description = "RunPaper Plugin Extension to support Advanced Slime World Manager server jars"
            tags = listOf("minecraft", "paper", "extension")
            implementationClass = "com.infernalsuite.runPaper.RunPaperPlugin"
        }
    }
}
