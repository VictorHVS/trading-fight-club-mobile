import kotlinx.kover.api.KoverProjectConfig

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.10")
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
        classpath("org.jacoco:org.jacoco.core:0.8.8")
    }
}

plugins {
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
}

allprojects {
    apply(plugin = "kover")

    group = "com.victorvs.tfc"
    version = "0.1.0"

    extensions.configure<KoverProjectConfig> {
        isDisabled.set(false)
        engine.set(kotlinx.kover.api.JacocoEngine("0.8.8"))
    }

    koverMerged {
        enable()
        xmlReport {
            onCheck.set(false)
            reportFile.set(layout.buildDirectory.file("$buildDir/reports/kover/result.xml"))
        }
        htmlReport {
            onCheck.set(false)
            reportDir.set(layout.buildDirectory.dir("$buildDir/reports/kover/html-result"))
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}