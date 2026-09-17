import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    kotlin("plugin.serialization")
}
if (file("google-services.json").exists()) { apply(plugin = "com.google.gms.google-services") }

val localConfig = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

android {
    namespace = "com.example.gustto"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.gustto"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["MAPS_API_KEY"] = localConfig.getProperty("MAPS_API_KEY", "")
        buildConfigField("String", "MAPS_API_KEY", "\"${localConfig.getProperty("MAPS_API_KEY", "")}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures { buildConfig = true }
    testOptions { unitTests.isIncludeAndroidResources = true }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation(platform("com.google.firebase:firebase-bom:34.19.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-dataconnect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("com.google.code.gson:gson:2.11.0")
    testImplementation(libs.junit)
    testImplementation("org.robolectric:robolectric:4.16.1")
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}

// Keep test workers on the Android Studio-compatible LTS runtime.
tasks.withType<Test>().configureEach {
    javaLauncher.set(javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) })
}
