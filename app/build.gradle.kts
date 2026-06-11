plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.trigodourado.app"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.trigodourado.app"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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

    viewBinding {
        enable = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.core.splashscreen)
    implementation(libs.material)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.recyclerview)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.room.runtime)
    implementation(libs.security.crypto)
    implementation(libs.viewpager2)
    annotationProcessor(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
