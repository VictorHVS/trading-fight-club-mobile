plugins {
    id("com.android.application")
    id("org.sonarqube") version "3.3"
    kotlin("android")
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.victorhvs.tradingfightclub.android"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "VictorHVS_TradingFight.club")
        property("sonar.organization", "victorhvs")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}