@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    // kotlin("android")
    id("com.android.library")
    // id("kotlinx-serialization")
    // id("com.google.devtools.ksp")
    // alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.plugin.serialization)
    alias(libs.plugins.build.ksp)
    // alias(libs.plugins.hilt.plugins)
}

android {
    namespace = "com.cxc.compose_common"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34

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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlin.compiler.get()
    }

}

dependencies {

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)

    androidTestImplementation(libs.bundles.test)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.bundles.debug)

    api(libs.bundles.activity.core)
    api(libs.bundles.lifecycle)
    api(libs.bundles.retrofit)
    api(libs.bundles.compose)
    api(libs.bundles.koin)
    api(libs.bundles.kotlin)
    api(libs.bundles.nav.compose)

    // api(libs.hilt.android)
    // api(libs.hilt.navigation.compose)
    // ksp(libs.hilt.ksp)


    implementation(libs.mmkv)
    api(libs.multidex)
    api(libs.utilcodex)
    api(libs.bugly)
    api(libs.dialogx)

    api(libs.cn.oaid)

}