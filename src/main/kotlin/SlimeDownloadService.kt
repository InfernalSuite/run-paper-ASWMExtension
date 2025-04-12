package com.infernalsuite.runPaper

import com.fasterxml.jackson.databind.ObjectMapper
import org.gradle.api.file.Directory
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URI
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption.*

public class SlimeDownloadService {
    public companion object {
        private val LOGGER: Logger = Logging.getLogger(SlimeDownloadService::class.java)

        public fun resolve(project: String, version: String, cacheDir: Directory): Pair<File, File> {

            val cache = CacheContainer(cacheDir.dir("versions.json").asFile)
            val jarFolder = cacheDir.dir(version)

            val versionData = cache.getOrAddVersion(version)
            versionData.lastUpdateCheck = System.currentTimeMillis();

            LOGGER.lifecycle("Fetching latest slime build number for $version")
            val inputLine: String
            run {
                val url = URI("https://api.infernalsuite.com/v1/projects/$project/mcversion/$version/latest").toURL();
                val connection = url.openConnection()
                val reader = BufferedReader(
                    InputStreamReader(
                        connection.getInputStream()
                    )
                )
                inputLine = reader.readText()
                reader.close()
            }
            val mapper = ObjectMapper()

            val root = mapper.readTree(inputLine)
            var versionsHaveChanged = false
            val buildId = root["id"].asText()


            LOGGER.lifecycle("Iterating jar types")

            root["files"].forEach {
                var fileName = it["fileName"].asText();
                val sha256Hash = it["sha256Hash"].asText();
                val fileId = it["id"].asText()


                val type: CacheContainer.JarType
                if(fileName.contains("server")) {
                    type = CacheContainer.JarType.SERVER;
                } else if(fileName.contains("plugin")) {
                    type = CacheContainer.JarType.PLUGIN;
                } else {
                    return@forEach
                }
                LOGGER.lifecycle("Checking $type as $fileName $sha256Hash")

                val jarInfo = versionData.knownJars[type]

                if (jarInfo != null) {
                    if(jarInfo.sha256 == sha256Hash) return@forEach

                    val jarFile = jarFolder.file(jarInfo.fileName).asFile
                    jarFile.delete()
                    LOGGER.lifecycle("Deleting old $jarFile")
                }
                jarFolder.asFile.mkdirs()
                val jarFile = jarFolder.file(fileName)
                LOGGER.lifecycle("Downloading $type $version as $fileName")

                val url = URI("https://api.infernalsuite.com/v1/projects/$project/$buildId/download/$fileId").toURL();
                val connection = url.openConnection()

                // download jar
                Channels.newChannel(connection.getInputStream()).use { remote ->
                    FileChannel.open(jarFile.asFile.toPath(), CREATE, WRITE, TRUNCATE_EXISTING).use { dest ->
                        dest.transferFrom(remote, 0L, Long.MAX_VALUE)
                    }
                }
                versionData.knownJars[type] = CacheContainer.JarInfo(fileName, sha256Hash)
                versionsHaveChanged = true
                LOGGER.lifecycle("Finished downloading $type $version")
            }

            if(versionsHaveChanged) {
                cache.writeData()
                LOGGER.lifecycle("Version cache updated")
            }
            LOGGER.lifecycle("Selecting slime jars")

            val pluginData = versionData.knownJars[CacheContainer.JarType.PLUGIN]
            if(pluginData == null) {
                LOGGER.error("Could not find plugin data for $version")
                throw Exception()
            }

            val serverData = versionData.knownJars[CacheContainer.JarType.SERVER]
            if(serverData == null) {
                LOGGER.error("Could not find server data for $version")
                throw Exception()
            }

            return Pair(jarFolder.file(pluginData.fileName).asFile, jarFolder.file(serverData.fileName).asFile)
        }
    }
}
