plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id ("kotlin-kapt")
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id ("dagger.hilt.android.plugin")
}


android {
    compileSdk =32
    buildFeatures {
        // Enables Jetpack Compose for this module
        compose= true
    }
    defaultConfig {
        applicationId ="cs.hku.hktransportandroid"
        minSdk =30
        targetSdk= 32
        versionCode= 1
        versionName ="1.0"

        testInstrumentationRunner ="androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled =false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    productFlavors{
        flavorDimensions += "api"
        create("api"){
            dimension = "api"
        }
        create("stub"){
            dimension = "api"
        }
    }
    compileOptions {
        sourceCompatibility =JavaVersion.VERSION_1_8
        targetCompatibility= JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion= "1.1.1"
    }
}

dependencies {
    val lifecycleVersion = "2.2.0"

    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    val navVersion = "2.4.2"

    implementation ("androidx.navigation:navigation-compose:$navVersion")

    //compose
    implementation("com.google.accompanist:accompanist-permissions:0.24.13-rc")
    implementation("com.google.accompanist:accompanist-flowlayout:0.24.13-rc")
    implementation ("androidx.compose.ui:ui:1.2.0-beta02")
    // Tooling support (Previews, etc.)
    implementation ("androidx.compose.ui:ui-tooling:1.2.0-beta02")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.2.0-beta02")

    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation ("androidx.compose.foundation:foundation:1.2.0-beta02")
    // Material Design
    implementation ("androidx.compose.material:material:1.2.0-beta02")
    // Material design icons
    implementation ("androidx.compose.material:material-icons-core:1.2.0-alpha07")
    implementation ("androidx.compose.material:material-icons-extended:1.2.0-alpha07")
    // Integration with activities
    implementation ("androidx.activity:activity-compose:1.4.0")
    // Integration with ViewModels
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")
    // Integration with observables
    implementation ("androidx.compose.runtime:runtime-livedata:1.1.1")
    implementation ("androidx.compose.runtime:runtime-rxjava2:1.1.1")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.4.0")
    //logging
    implementation ("com.squareup.okhttp3:logging-interceptor:3.9.0")
    //google map
    implementation ("com.google.maps.android:maps-compose:2.1.1")
    implementation ("com.google.android.gms:play-services-maps:18.0.2")

    //location
    implementation ("com.google.android.gms:play-services-location:20.0.0")

    //room
    val roomVersion = "2.4.2"
    implementation ("androidx.room:room-runtime:$roomVersion")
    kapt ("androidx.room:room-compiler:$roomVersion")
    implementation ("androidx.room:room-ktx:$roomVersion")
    //moshi
    implementation ("com.squareup.retrofit2:converter-moshi:2.4.0")
    implementation("com.squareup.moshi:moshi:1.13.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")
    //hilt
    implementation ("com.google.dagger:hilt-android:2.38.1")
    kapt ("com.google.dagger:hilt-compiler:2.38.1")
    //hilt with navigation compose
    //https://developer.android.com/jetpack/compose/libraries
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")


    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.appcompat:appcompat:1.4.1")
    implementation ("com.google.android.material:material:1.6.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
}