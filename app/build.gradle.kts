import com.google.protobuf.gradle.id

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.luiooiul.beat"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.luiooiul.beat"
        minSdk = 21
        targetSdk = 33
        versionCode = 2
        versionName = "1.1"

        resourceConfigurations += listOf("en")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packagingOptions {
        resources {
            excludes += setOf(
                "kotlin/**",
                "google/**",
                "META-INF/*.version",
                "DebugProbesKt.bin",
                "kotlin-tooling-metadata.json"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.12"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") {
                    option("lite")
                }
                id("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("com.google.dagger:hilt-android:2.44.2")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.compose.foundation:foundation:1.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha03")
    implementation("com.google.protobuf:protobuf-kotlin-lite:3.21.12")
    kapt("com.google.dagger:hilt-android-compiler:2.44.2")
}