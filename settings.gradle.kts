import org.gradle.api.internal.FeaturePreviews.Feature.VERSION_CATALOGS

enableFeaturePreview(VERSION_CATALOGS.name)

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ZeroGlu-Recipes"
include(":app")
