# Run Paper ASWM Gradle Extension Plugin


## Usage
You need to start with a valid **runPaper** setup as [described here](https://plugins.gradle.org/docs/publish-plugin#approval).
Most properties should be supported.


```kotlin
import net.endrealm.useSlimeWorldPaper
// alternatively: import net.endrealm.useSlimeWorldPurpur
// alternatively: import net.endrealm.useSlimeWorldPufferfish

plugins {
    // Apply the plugin
    id("xyz.jpenilla.run-paper") version "2.2.0"
    id("com.infernalsuite.runPaper") version "1.0.0"
}

tasks {
    runServer {
        minecraftVersion("1.19.2")
        useSlimeWorldPaper()
        // alternatively: useSlimeWorldPurpur()
        // alternatively: useSlimeWorldPufferfish()
    }
}
```
Then run the task as you would with regular **runPaper**: `gradlew :runServer`


