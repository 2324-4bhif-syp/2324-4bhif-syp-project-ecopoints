import java.util.Properties
import kotlin.collections.set

plugins {
    kotlin("kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}
android {
    namespace = "at.htl.ecopoints"
    compileSdk = 34

    defaultConfig {
        applicationId = "at.htl.ecopoints"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        manifestPlaceholders["googleMapsApiKey"] = properties.getProperty("MAPS_API_KEY")
    }

//    val properties = Properties()
//    properties.load(project.rootProject.file("local.properties").inputStream())
//    man["googleMapsApiKey"] = properties.getProperty("MAPS_API_KEY")


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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.hilt.android)
    kapt (libs.com.google.dagger.hilt.compiler2)

    // For instrumentation tests
    androidTestImplementation  (libs.dagger.hilt.android.testing)
    kaptAndroidTest (libs.com.google.dagger.hilt.compiler2)

    // For local unit tests
    testImplementation (libs.dagger.hilt.android.testing)
    kaptTest (libs.com.google.dagger.hilt.compiler2)

    implementation (libs.androidx.runtime)
    implementation (libs.androidx.foundation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx.v280)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom.v20240500))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.material)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20240903))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    //csv
    implementation (libs.opencsv)

    // layout
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.ui.ui.tooling)
    implementation(libs.foundation)
    implementation(libs.material)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.constraintlayout)


    //MAPS
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // RxJava
    implementation(libs.rxjava)
    implementation(libs.androidx.runtime.rxjava3.v167)


    //hilt
    implementation(libs.rxjava)
    implementation(libs.androidx.runtime.rxjava3)
    kapt(libs.hilt.android.compiler)


    // Jackson
    implementation(libs.jackson.databind)

    // OBD
    implementation (libs.kotlin.obd.api)

    implementation (libs.gson)

    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation (libs.okhttp)

    //Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    //Accompanist (Permission)
    implementation(libs.accompanist.permissions)
}

kapt {
    correctErrorTypes = true
}