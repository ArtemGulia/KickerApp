package com.g_art.kickerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.g_art.kickerapp.R;
import com.g_art.kickerapp.model.Game;
import com.g_art.kickerapp.model.Player;
import com.g_art.kickerapp.utils.RestClient;
import com.g_art.kickerapp.utils.api.GameApi;
import com.g_art.kickerapp.utils.prefs.SharedPrefsHandler;
import com.g_art.kickerapp.utils.api.UserApi;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.http.Headers;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, OnClickListener {

    private String playerId;
    private String playerName;
    private String provider;
    private String providerId;
    private String tokenId;

    public static final String TWITTER = "twitter";
    public static final String FACEBOOK = "facebook";
    public static final String GOOGLE = "google";

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "sKrTcP9bMAINWGBWUAsGLCmcL";
    private static final String TWITTER_SECRET = "2CyWwR7zP2Xt15eKuXAo2wUWZrwOOLEfyKKFcfhPgIuxRcsmOK";

    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;

    private TwitterLoginButton twLoginButton;
    private LoginButton fbLoginButton;
    private SignInButton signInButton;

    private CallbackManager fbCallbackManager;

    private ProgressDialog mProgressDialog;

    private SharedPrefsHandler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPrefs = this.getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loginHandler = SharedPrefsHandler.getInstance(mPrefs);

        initSocialSDK();
        setContentView(R.layout.activity_login);

        //===========================TWITTER==========================
        twitterInitialization();

        //===========================FACEBOOK==========================
        facebookInitialization();

        //===========================GOOGLE==========================
        googleInitialization();

    }

    private void initSocialSDK() {
        //Twitter initialization
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        //Facebook initialization
        FacebookSdk.sdkInitialize(getApplicationContext());
        fbCallbackManager = CallbackManager.Factory.create();
    }

    private void twitterInitialization() {
        twLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twLoginButton.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                TwitterAuthToken authToken = session.getAuthToken();
                String twitterToken = authToken.token;
                tokenId = twitterToken;
                playerId = "" + session.getUserId();
                playerName = session.getUserName();
                provider = TWITTER;

                loginSuccess();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
                Toast.makeText(getApplicationContext(), "Twitter Login Failed", Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        });
    }

    private void facebookInitialization() {
        fbLoginButton = (LoginButton) findViewById(R.id.fbLogin_button);
        fbLoginButton.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken fbToken = loginResult.getAccessToken();
                tokenId = fbToken.getToken();
                playerId = fbToken.getUserId();

                /* make the API call */
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + playerId,
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                JSONObject responseObject = response.getJSONObject();
                                if (responseObject != null) {
                                    try {
                                        playerName = responseObject.getString("name");
                                        provider = FACEBOOK;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    loginSuccess();
                                }
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("FaceBookSDK", "Login with Facebook failure", null);

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FaceBookSDK", "Login with Facebook failure", error);
            }
        });
    }

    private void googleInitialization() {
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        Scope[] scope = gso.getScopeArray();
        signInButton.setScopes(scope);
        // [END customize_button]
        signInButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        twLoginButton.onActivityResult(requestCode, resultCode, data);
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Google_SignIn", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String name = acct.getDisplayName();
            tokenId = acct.getIdToken();
            playerId = acct.getId();
            playerName = name;
            provider = GOOGLE;
            loginSuccess();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Google_SignIn", "onConnectionFailed:" + connectionResult.toString());
        Toast.makeText(getApplicationContext(), "Google_SignIn_Connection Failed", Toast.LENGTH_LONG).show();
        hideProgressDialog();
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("Google_SignIn", "Got cached sign-in");
            Toast.makeText(getApplicationContext(), "Got cached sign-in", Toast.LENGTH_SHORT).show();
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void loginSuccess() {
        loginHandler.save(SharedPrefsHandler.LOGGED, SharedPrefsHandler.IS_LOGGED);
        loginHandler.save(SharedPrefsHandler.PLAYER_NAME, playerName);
        loginHandler.save(SharedPrefsHandler.PLAYER_ID, playerId);
        loginHandler.save(SharedPrefsHandler.PROVIDER_ID, playerId);
        loginHandler.save(SharedPrefsHandler.TOKEN_ID, tokenId);
        loginHandler.save(SharedPrefsHandler.LOGIN_PROVIDER, provider);

        getPlayer(provider, playerId, playerName);
    }

    private void getPlayer(String provider, String providerId, String displayName) {
        UserApi userApi = RestClient.getUserApi();
        Map<String, String> profile = new HashMap<>();
        profile.put(SharedPrefsHandler.PROVIDER_ID, providerId);
        profile.put(SharedPrefsHandler.PLAYER_NAME, displayName);
        profile.put(SharedPrefsHandler.LOGIN_PROVIDER, provider);
        userApi.loginPlayerOrCreate(profile, new retrofit.Callback<Player>() {
            @Override
            public void success(Player player, Response response) {
                if (response != null) {
                    List<Header> headers = response.getHeaders();
                    for (Header header : headers) {
                        String headerName = header.getName();
                        if (SharedPrefsHandler.COOKIE.equals(headerName)) {
                            loginHandler.save(SharedPrefsHandler.COOKIE, header.getValue());
                            break;
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("player", player);
                    setResult(RESULT_OK, intent);
                    hideProgressDialog();
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), error.getUrl(), Toast.LENGTH_LONG).show();
                }
            }
        });

//        GameApi gameApi = RestClient.getGameApi();
//
//        gameApi.getAllGames(new retrofit.Callback<List<Game>>() {
//            @Override
//            public void success(List<Game> games, Response response) {
//                if (response != null) {
//
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                if (error != null) {
//                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//                    Log.d("Response", error.getResponse().toString());
//                }
//            }
//        });
    }


    /***
     * Google signIn
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}

