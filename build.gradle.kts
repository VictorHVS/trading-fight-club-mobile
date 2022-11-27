import io.gitlab.arturbosch.detekt.Detekt
import kotlinx.kover.api.CounterType
import kotlinx.kover.api.IntellijEngine
import kotlinx.kover.api.KoverMergedFilters
import kotlinx.kover.api.KoverProjectConfig
import kotlinx.kover.api.KoverTaskExtension
import kotlinx.kover.api.KoverVerifyConfig
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestReport
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

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
    }
}

plugins {
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

val liba = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Test.isDev get() = name.contains("debug", ignoreCase = true)

fun Project.catalogVersion(alias: String) = liba.findVersion(alias).get().toString()
fun Project.catalogLib(alias: String) = liba.findLibrary(alias).get()

val koverIncludes = listOf("com.victorhvs.tfc.*")
val koverExcludes = listOf(
    // App
    "*.TFCApp",
    "*.initializers.*",

    // Apollo
    "*.remote.*.adapter.*",
    "*.remote.*.fragment.*",
    "*.remote.*.selections.*",
    "*.remote.*.type.*",
    "*.remote.*.*Mutation*",
    "*.remote.*.*Query*",

    // Common
    "*.common.*",

    // Common Android
    "*.BuildConfig",
    "*.*Activity",
    "*.*Fragment",
    "*.base.*",
    "*.navigation.*",

    // Compose
    "*.*ComposableSingletons*",

    // Hilt
    "*.di.*",
    "*.*Hilt_*",
    "*.*HiltModules*",
    "*.*_Factory",

    // Serializers
    "*$\$serializer",

    // Ui
    "*.ui.*.components.*",
    "*.ui.*.view.*",
    "*.theme.*",
)

allprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    group = "com.victorvs.tfc"
    version = "0.1.0"

    val engineVersion = catalogVersion("coverage-engine")

    extensions.configure<KoverProjectConfig> {
        engine.set(IntellijEngine(engineVersion))
        filters {
            classes {
                excludes.addAll(koverExcludes)
                includes.addAll(koverIncludes)
            }
        }
    }

    tasks.withType<Test> {
        extensions.configure<KoverTaskExtension> {
            isDisabled.set(!isDev)
        }
    }

    koverMerged {
        enable()
        filters { commonFilters() }
        verify { rules() }
    }

    dependencies {
        detektPlugins(catalogLib("detekt-compose"))
        detektPlugins(catalogLib("detekt-compose2"))
        detektPlugins(catalogLib("detekt-formatting"))
    }
}

val MIN_COVERED_PERCENTAGE = 80

fun KoverMergedFilters.commonFilters() {
    classes {
        excludes.addAll(koverExcludes)
        includes.addAll(koverIncludes)
    }
}

fun KoverVerifyConfig.rules() {
    rule {
        name = "Minimal instruction coverage rate in percent"
        bound {
            counter = CounterType.INSTRUCTION
            minValue = MIN_COVERED_PERCENTAGE
        }
    }
    rule {
        name = "Minimal line coverage rate in percent"
        bound {
            counter = CounterType.LINE
            minValue = MIN_COVERED_PERCENTAGE
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

@Suppress("UnstableApiUsage")
tasks.register("unitTests", TestReport::class) {
    val testTasks = subprojects.map { p ->
        p.tasks.withType<Test>().matching { t -> t.isDev }
    }

    mustRunAfter(testTasks)
    destinationDirectory.set(file("$buildDir/reports/allTests"))
    testResults.setFrom(testTasks)
}

tasks.register("detektAll", Detekt::class) {
    description = "Run detekt in all modules"

    parallel = true
    ignoreFailures = false
    autoCorrect = true
    buildUponDefaultConfig = true
    jvmTarget = JavaVersion.VERSION_1_8.toString()
    setSource(files(projectDir))
    config.setFrom(files("$rootDir/config/detekt.yml"))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**")

    reports {
        html.required.set(true)
        sarif.required.set(true)
        txt.required.set(false)
        xml.required.set(true)
    }
}
