package com.infernalsuite.runPaper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.util.*


public class CacheContainer public constructor(private val file: File) {

    private val versions: MutableMap<String, VersionData>;
    private val mapper = ObjectMapper()


    init {
        if(!file.exists()){
            file.parentFile.mkdirs()
            versions = HashMap()
        } else {
            versions = file.bufferedReader().use { reader -> mapper.readValue(reader) }
        }
    }

    public fun writeData() {
        file.bufferedWriter().use { writer -> mapper.writeValue(writer, versions) }
    }

    public fun getOrAddVersion(version: String): VersionData {
        return versions.computeIfAbsent(version) {VersionData()}
    }

    public data class JarInfo(
        var fileName: String = "",
        var sha256: String = ""
    )

    public data class VersionData(
        var lastUpdateCheck: Long = 0L,
        var knownJars: EnumMap<JarType, JarInfo> = EnumMap(JarType::class.java),
    )

    public enum class JarType {
        PLUGIN,
        SERVER
    }
}
