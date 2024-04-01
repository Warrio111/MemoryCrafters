package com.example.memorycrafters;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
public class AuthActivity extends AppCompatActivity {
    private static final String TAG = "AuthActivity";
    private static final Integer RC_SIGN_IN  = 123;

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";
    private static final String FIREBASE_TOS_URL = "https://firebase.google.com/terms/";
    private static final String GOOGLE_PRIVACY_POLICY_URL = "https://www.google" +
            ".com/policies/privacy/";
    private static final String FIREBASE_PRIVACY_POLICY_URL = "https://firebase.google" +
            ".com/terms/analytics/#7_privacy";
    private ActivityResultLauncher<Intent> signInLauncher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // Create and launch sign-in intent
        if (auth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            // You can also get the user's email address
            String email = auth.getCurrentUser().getEmail();
            String displayName = auth.getCurrentUser().getDisplayName();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startSignInFlow();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                String displayName = user.getDisplayName();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                if (response == null) {
                    showSnackbar(findViewById(android.R.id.content), R.string.sign_in_cancelled);
                }
                assert response != null;
                handleSignInError(response.getError().getErrorCode());
            }
            Log.e(TAG, "onActivityResult: " + response.getError().getErrorCode());
        }
    }
    private void handleSignInError(int errorCode) {
        switch (errorCode) {
            case ErrorCodes.NO_NETWORK:
                showSnackbar(findViewById(android.R.id.content), R.string.no_internet_connection);
                break;
            case ErrorCodes.ERROR_USER_DISABLED:
                showSnackbar(findViewById(android.R.id.content), R.string.account_disabled);
                break;
            default:
                showSnackbar(findViewById(android.R.id.content), R.string.unknown_error);
                break;
        }
    }

    private void startSignInFlow() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setTosAndPrivacyPolicyUrls(
                        GOOGLE_TOS_URL,
                        GOOGLE_PRIVACY_POLICY_URL)
                .build();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showSnackbar(View view, int messageId) {
        Snackbar.make(view, messageId, Snackbar.LENGTH_SHORT).show();
    }
}