package com.happy.friendogly.presentation.ui.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.happy.friendogly.BuildConfig

class GoogleSignInContract : ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {
    override fun createIntent(
        context: Context,
        input: Int,
    ): Intent {
        val googleSignInClient =
            GoogleSignIn.getClient(
                context,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                    .requestServerAuthCode(BuildConfig.GOOGLE_CLIENT_ID)
                    .build(),
            )
        return googleSignInClient.signInIntent
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): Task<GoogleSignInAccount>? {
        return when (resultCode) {
            Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
            else -> null
        }
    }
}
