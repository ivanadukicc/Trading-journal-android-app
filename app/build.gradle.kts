plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.projekat"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.projekat"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"



        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "MY_API_KEY", "\"${property("MY_API_KEY")}\"")

    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(libs.room.common.jvm)
    implementation(libs.room.runtime)
    implementation(libs.recyclerview)
    implementation(libs.coordinatorlayout)
    implementation(libs.browser)
    annotationProcessor(libs.room.compiler)

    implementation(libs.common.java8)
    implementation(libs.viewmodel)
    implementation(libs.lifecycle.livedata)

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.browser:browser:1.8.0")


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


}