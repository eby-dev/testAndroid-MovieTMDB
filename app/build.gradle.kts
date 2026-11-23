import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.GradleException

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

fun resolveProperty(key: String): String =
    localProperties.getProperty(key)?.takeIf { it.isNotBlank() }
        ?: System.getenv(key).orEmpty()

val tmdbApiKey: String = resolveProperty("TMDB_API_KEY")

val keystoreFile: String = resolveProperty("KEYSTORE_FILE")
val keystorePassword: String = resolveProperty("KEYSTORE_PASSWORD")
val releaseKeyAlias: String = resolveProperty("KEY_ALIAS")
val releaseKeyPassword: String = resolveProperty("KEY_PASSWORD")

gradle.taskGraph.whenReady {
    val isAssemblingRelease = allTasks.any { it.name.contains("Release") }
    if (isAssemblingRelease) {
        if (tmdbApiKey.isBlank()) {
            throw GradleException("TMDB_API_KEY is required for release builds.")
        }
        if (keystoreFile.isBlank() || keystorePassword.isBlank() ||
            releaseKeyAlias.isBlank() || releaseKeyPassword.isBlank()
        ) {
            throw GradleException(
                "Release signing (KEYSTORE_FILE/KEYSTORE_PASSWORD/KEY_ALIAS/KEY_PASSWORD) is required."
            )
        }
    }
}

android {
    namespace = "com.ahmadabuhasan.movietmdb"
    compileSdk = libs.versions.compileSdk.get().toInt()
    ndkVersion = libs.versions.ndkVersion.get()

    defaultConfig {
        applicationId = "com.ahmadabuhasan.movietmdb"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild {
            cmake {
                cppFlags("-Dapi_key=$tmdbApiKey")
            }
        }
    }

    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
        }
    }

    signingConfigs {
        create("release") {
            if (keystoreFile.isNotBlank()) {
                storeFile = file(keystoreFile)
                storePassword = keystorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.glide)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
