buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.13")
    }
}
plugins {
    id("com.android.application") version "7.2.2" apply false
    id("com.android.library") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.0" apply false
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
