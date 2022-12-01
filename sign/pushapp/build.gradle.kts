plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.pushapp"
    compileSdk = 32

    defaultConfig {
        applicationId = "com.example.pushapp"
        minSdk = 23
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":androidCore:sdk")) {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel")
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
    }
    implementation(project(":sign:sdk")) {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel")
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
    }

    implementation(platform("com.google.firebase:firebase-bom:31.1.0"))

    implementation("androidx.core:core-ktx:1.7.0") {
        exclude(group)
    }
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-messaging-ktx:23.0.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}