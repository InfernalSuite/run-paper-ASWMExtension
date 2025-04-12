package com.infernalsuite.runPaper
import xyz.jpenilla.runpaper.task.RunServer

public fun RunServer.useSlimeWorldPaper() {
    useSlimeWorldGeneric("asp")
}

public fun RunServer.useSlimeWorldPurpur() {
    useSlimeWorldGeneric("aspurpur")

}
public fun RunServer.useSlimeWorldPufferfish() {
    useSlimeWorldGeneric("aspufferfish")
}

internal fun RunServer.useSlimeWorldGeneric(slug: String) {
    val cacheDir = project.layout.buildDirectory.get().dir(slug).dir("slime_cache")
    val jars = SlimeDownloadService.resolve(slug, version.get(), cacheDir)
    pluginJars(jars.first)
    runJar(jars.second)
}
