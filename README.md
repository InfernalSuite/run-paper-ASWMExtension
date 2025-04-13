# Run Paper ASWM Gradle Extension Plugin


## Usage
You need to start with a valid **runPaper** setup as [described here](https://github.com/jpenilla/run-task/wiki/Basic-Usage).
Most properties should be supported.


```kotlin
import com.infernalsuite.useSlimeWorldPaper
// alternatively: import com.infernalsuite.useSlimeWorldPurpur
// alternatively: import com.infernalsuite.useSlimeWorldPufferfish

plugins {
    // Apply the plugin
    id("xyz.jpenilla.run-paper") version "2.2.0"
    id("com.infernalsuite.run-paper") version "1.0.0"
}

tasks {
    runServer {
        minecraftVersion("1.21.4")
        useSlimeWorldPaper()
        // alternatively: useSlimeWorldPurpur()
        // alternatively: useSlimeWorldPufferfish()
    }
}
```
Then run the task as you would with regular **runPaper**: `gradlew :runServer`


