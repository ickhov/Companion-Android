package com.companion.android

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.results.SignUpResult

class AuthenticationActivity : AppCompatActivity(),
    SignUpFragment.OnFragmentInteractionListener,
    LogInFragment.OnFragmentInteractionListener,
    ForgotPasswordEmailFragment.OnFragmentInteractionListener,
    ForgotPasswordNewPasswordFragment.OnFragmentInteractionListener,
    EmailVerificationFragment.OnFragmentInteractionListener {

    override fun onClickSignUp(user: User) {

        val email: String = user.email
        val name: String = user.name
        val password: String? = user.password

        val attributes: HashMap<String, String> = hashMapOf("email" to email, "name" to name)

        AWSMobileClient.getInstance().signUp(email, password, attributes, null, object : Callback<SignUpResult> {
            override fun onResult(result: SignUpResult?) {
                Log.e(TAG_SIGN_UP, "Sign-up successful")
                onClickEmailVerification()
            }

            override fun onError(e: Exception?) {
                Log.e(TAG_SIGN_UP, "Sign-up error", e)
            }
        })
    }

    override fun onClickEmailVerification() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        performTransition(TAG_SIGN_UP, TAG_EMAIL_VERIFICATION, null)
    }

    override fun onClickLogIn() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        performTransition(TAG_SIGN_UP, TAG_LOG_IN, R.id.fragment_sign_up_title)
    }

    override fun onClickSignUp() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        performTransition(TAG_LOG_IN, TAG_SIGN_UP, R.id.fragment_log_in_title)
    }

    override fun onClickForgotPassword() {
        Handler().postDelayed(
            { supportActionBar?.setDisplayHomeAsUpEnabled(true) },
            1300)

        performTransition(TAG_LOG_IN, TAG_FORGOT_PASSWORD_EMAIL, null)
        //supportFragmentManager.beginTransaction().replace(R.id.activity_authentication_container, ForgotPasswordEmailFragment.newInstance(), TAG_FORGOT_PASSWORD_EMAIL).commit()

    }

    override fun setCurrentFragment(name: String) {
        mCurrentFragment = name
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mToolbar: Toolbar? = null
    private var mSignUpFragment: SignUpFragment? = null
    private var mCurrentFragment: String? = null
    private var mFragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        mToolbar = findViewById(R.id.activity_toolbar)

        mToolbar?.apply {
            navigationIcon = getDrawable(R.drawable.ic_left_24px)
        }

        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportFragmentManager.beginTransaction().replace(R.id.activity_authentication_container, LogInFragment.newInstance(), TAG_LOG_IN).commit()

        AWSMobileClient.getInstance().initialize(applicationContext, object : Callback<UserStateDetails> {

            override fun onResult(userStateDetails: UserStateDetails) {
                Log.i("INIT", "onResult: " + userStateDetails.userState)
            }

            override fun onError(e: Exception) {
                Log.e("INIT", "Initialization error.", e)
            }
        }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            when (mCurrentFragment) {
                FORGOT_PASSWORD_EMAIL_FRAGMENT -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    performTransition(TAG_FORGOT_PASSWORD_EMAIL, TAG_LOG_IN, null)
                }

                FORGOT_PASSWORD_NEW_PASSWORD_FRAGMENT -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportFragmentManager.beginTransaction().replace(R.id.activity_authentication_container, ForgotPasswordEmailFragment.newInstance(), TAG_FORGOT_PASSWORD_EMAIL).commit()
                }
            }

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun performTransition(previousFragmentTag: String,
                                  nextFragmentTag: String,
                                  sharedElementID: Int?) {
        if (isDestroyed) {
            return
        }

        val previousFragment = mFragmentManager.findFragmentByTag(previousFragmentTag)
        val nextFragment = getFragmentByTag(nextFragmentTag)

        val fragmentTransaction = mFragmentManager.beginTransaction()

        // 1. Exit for Previous Fragment
        val exitFade = Fade()
        exitFade.duration = 300
        previousFragment!!.exitTransition = exitFade

        if (sharedElementID != null) {
            // 2. Shared Elements Transition
            val enterTransitionSet = MoveTransition()
            enterTransitionSet.duration = 1000
            enterTransitionSet.startDelay = 300
            nextFragment.sharedElementEnterTransition = enterTransitionSet
        }

        // 3. Enter Transition for New Fragment
        val enterFade = Fade()
        enterFade.duration = 1000
        enterFade.startDelay = 300
        nextFragment.enterTransition = enterFade

        if (sharedElementID != null) {
            val title: View = findViewById(sharedElementID)
            fragmentTransaction.addSharedElement(title, title.transitionName)
        }

        fragmentTransaction.replace(R.id.activity_authentication_container, nextFragment, nextFragmentTag)
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun getFragmentByTag(fragmentTag: String): Fragment {
        return when(fragmentTag) {
            TAG_LOG_IN -> LogInFragment.newInstance()
            TAG_SIGN_UP -> SignUpFragment.newInstance()
            TAG_FORGOT_PASSWORD_EMAIL -> ForgotPasswordEmailFragment.newInstance()
            TAG_EMAIL_VERIFICATION -> EmailVerificationFragment.newInstance()
            else -> ForgotPasswordNewPasswordFragment.newInstance()
        }
    }
}
