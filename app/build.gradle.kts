import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialize)
}

android {
    namespace = "com.stevens.software.qrcraft"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.stevens.software.qrcraft"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":uitoolkit"))
    implementation(project(":history"))
    implementation(project(":generator"))
    implementation(project(":result"))
    implementation(project(":scanner"))
    implementation(project(":analyzer"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.core.splashscreen)
    implementation(libs.koin)
    implementation(libs.koin.compose)
    implementation(libs.compose.navigation)
    implementation(libs.kotlin.serialization)
    implementation(libs.androidx.ui.tooling.preview)

}