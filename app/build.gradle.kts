plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp") version "1.7.0-1.0.6"
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.rsicarelli.zeroglu_recipes"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.startup:startup-runtime:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.1")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.compose.ui:ui:1.2.0")
    implementation("androidx.compose.material3:material3:1.0.0-alpha09")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.0")
    implementation("androidx.compose.material:material:1.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")
    implementation("androidx.activity:activity-compose:1.5.0")
    implementation("com.google.android.material:material:1.6.0-beta01")
    implementation("io.github.raamcosta.compose-destinations:core:1.6.12-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.6.12-beta")
    implementation("com.google.firebase:firebase-firestore-ktx:24.1.1")
    testImplementation("junit:junit:4.13.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.2.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.2.0")
}
