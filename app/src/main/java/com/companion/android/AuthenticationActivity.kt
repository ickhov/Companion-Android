package com.companion.android

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class AuthenticationActivity : AppCompatActivity(),
    SignUpFragment.OnFragmentInteractionListener,
    LogInFragment.OnFragmentInteractionListener,
    ForgotPasswordEmailFragment.OnFragmentInteractionListener,
    ForgotPasswordNewPasswordFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mSignUpFragment: SignUpFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        //supportFragmentManager.beginTransaction().replace(R.id.activity_authentication, SignUpFragment()).commit()

        //supportFragmentManager.beginTransaction().replace(R.id.activity_authentication, LogInFragment()).commit()

        //supportFragmentManager.beginTransaction().replace(R.id.activity_authentication, ForgotPasswordEmailFragment()).commit()

        supportFragmentManager.beginTransaction().replace(R.id.activity_authentication, ForgotPasswordNewPasswordFragment()).commit()

    }
}
