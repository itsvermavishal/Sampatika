plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
}

android {
    namespace = "com.finance.sampatika"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.finance.sampatika"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.0.1"

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

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.4.0")


    // Navigation (ONLY ONCE)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // UI
    implementation(libs.smoothbottombar)
    implementation(libs.android.spinkit)

    // Networking
    implementation(libs.retrofit.v290)
    implementation(libs.retrofit2.converter.gson)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Image loading
    implementation(libs.glide)

    // Room (FIXED)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}