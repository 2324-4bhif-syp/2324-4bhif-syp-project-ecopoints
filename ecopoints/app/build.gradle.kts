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

    implementation("com.google.dagger:hilt-android:2.52")
    kapt ("com.google.dagger:hilt-compiler:2.52")

    // For instrumentation tests
    androidTestImplementation  ("com.google.dagger:hilt-android-testing:2.52")
    kaptAndroidTest ("com.google.dagger:hilt-compiler:2.52")

    // For local unit tests
    testImplementation (libs.dagger.hilt.android.testing)
    kaptTest (libs.com.google.dagger.hilt.compiler2)

    implementation ("androidx.compose.runtime:runtime")
    implementation ("androidx.compose.foundation:foundation")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //csv
    implementation ("com.opencsv:opencsv:5.9")

    // layout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.ui:ui:1.6.7")
    implementation("androidx.compose.ui:ui-tooling:1.6.7")
    implementation("androidx.compose.foundation:foundation:1.6.7")
    implementation("androidx.compose.material:material:1.6.7")
    implementation("androidx.compose.material:material-icons-core:1.6.7")
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")


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