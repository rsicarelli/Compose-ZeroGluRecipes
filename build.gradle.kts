buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.13")
    }
}
plugins {
    id("com.android.application") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21" apply false
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
