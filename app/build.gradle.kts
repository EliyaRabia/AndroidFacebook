plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.androidfacebook"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.androidfacebook"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary=true;

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation ("com.google.code.gson:gson:2.8.9")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.github.bumptech.glide:glide:3.7.0")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}

fun implemtation(s: String) {

}
