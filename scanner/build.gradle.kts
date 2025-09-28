plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.stevens.software.scanner"
    compileSdk = 36
    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(project(":uitoolkit"))
    implementation(project(":analyzer"))
    implementation(project(":core"))

    implementation(libs.androidx.material3)
    implementation(libs.koin.compose)
    implementation(libs.barcode.scanning)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    implementation(libs.camera2)
    implementation(libs.camera.extensions)
    implementation(libs.camera.compose)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
}