# NHAPP - Android Setup Instructions

This is a minimal, clean, and deployable Jetpack Compose Android app built for Supabase without a custom node server.

## 1. Supabase Setup

1. Create a project at [Supabase](https://supabase.com).
2. Go to **Authentication -> Providers -> Email** and enable **"Enable Email OTP"**.
3. Go to **SQL Editor** and run the schema defined in your `implementation_plan.md` to create tables, indexes, and RLS policies.
4. Go to **Project Settings -> API** and copy your `Project URL` and `anon public` key.
5. Open `app/src/main/java/com/nhapp/data/remote/SupabaseClient.kt` and paste your URL and Anon Key where indicated.

## 2. Firebase Cloud Messaging (FCM) Setup

1. Create a project at [Firebase Console](https://console.firebase.google.com).
2. Add an Android App with the package name `com.nhapp`.
3. Download the `google-services.json` file.
4. Place the `google-services.json` file exactly inside the `app/` directory.
5. In the top-level `build.gradle.kts`, uncomment `id("com.google.gms.google-services") version "4.4.0" apply false`.
6. In `app/build.gradle.kts`, uncomment `id("com.google.gms.google-services")` plugin.
7. Also in `app/build.gradle.kts`, add the FCM dependencies:
   ```kotlin
   implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
   implementation("com.google.firebase:firebase-messaging-ktx")
   ```
8. Sync the Gradle project.

## 3. Running the App

1. Open the `NHAPP` folder in Android Studio.
2. Wait for Gradle wrapper to download sync all dependencies (Compose BOM, Supabase KT, Ktor).
3. Select an emulator or physical device and press **Run**.
4. To test push notifications while offline, you will need to map the stored FCM tokens to a Supabase Edge Function that sends the push message when a row is inserted in the `messages` table.
