plugins {
    id("com.android.application")
    id("org.jetbrains.compose")
    kotlin("android")
}

android {
    namespace = "com.probrotechsolutions.laffalitto.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.probrotechsolutions.laffalitto.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

//
//        buildConfigField("String", "APIKEY_JOKE", "${project.findProperty("apiKey.joke")}")
//        buildConfigField("String", "HOST.JOKE", "${project.findProperty("host.joke")}")
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.ui:ui-tooling:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.compose.foundation:foundation:1.4.3")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.activity:activity-compose:1.7.2")
}