package com.nhapp.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    // TODO: Replace with your actual Supabase URL and Anon Key from the dashboard
    private const val SUPABASE_URL = "https://mpiptfzjrixbzpzpxjfj.supabase.co"
    private const val SUPABASE_ANON_KEY = "sb_publishable_pab_R5GFZYBOYtpfN_JF1Q_Xf1UrHWP"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
        install(Auth)
        install(Realtime)
        install(Storage)
    }
}
