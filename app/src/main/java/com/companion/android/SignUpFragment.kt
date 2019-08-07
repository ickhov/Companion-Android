package com.companion.android

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.library.baseAdapters.BR
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import com.companion.android.databinding.FragmentSignUpBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.lang.Exception

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SignUpFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var emailInputLayout: TextInputLayout? = null
    private var model: AuthenticationViewModel? = null
    private var contain8Char: Boolean = false
    private var containUpper: Boolean = false
    private var containLower: Boolean = false
    private var containNumber: Boolean = false
    private var containSpecialChar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)

        val regexUppercase = Regex(""".*[A-Z]""")
        val regexLowercase = Regex(""".*[a-z]""")
        val regexNumber = Regex(""".*[0-9]""")
        val regexSpecialCharacter = Regex(""".*[^A-Za-z0-9]""")

        val checkMark: Drawable? = context!!.getDrawable(R.drawable.ic_check_circle_24px)
        val color: Int = ContextCompat.getColor(context!!, R.color.colorSecondaryLight)
        checkMark!!.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        model = object : AuthenticationViewModel() {
            override fun setPassword(value: String) {
                // Avoids infinite loops.
                if (user.password != value) {
                    user.password = value

                    user.password?.let {
                        if (it.length >= 8 && !contain8Char) {
                            contain8Char = true
                            fragment_sign_up_password_require_count?.setCompoundDrawablesWithIntrinsicBounds(checkMark, null, null, null)
                        } else if (it.length < 8 && contain8Char) {
                            contain8Char = false
                            fragment_sign_up_password_require_count?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_24px, 0,0,0)
                        }

                        if (regexUppercase.containsMatchIn(it) && !containUpper) {
                            containUpper = true
                            fragment_sign_up_password_require_uppercase_letters?.setCompoundDrawablesWithIntrinsicBounds(checkMark, null, null, null)
                        } else if (!regexUppercase.containsMatchIn(it) && containUpper) {
                            containUpper = false
                            fragment_sign_up_password_require_uppercase_letters?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_24px, 0,0,0)
                        }

                        if (regexLowercase.containsMatchIn(it) && !containLower) {
                            containLower = true
                            fragment_sign_up_password_require_lowercase_letters?.setCompoundDrawablesWithIntrinsicBounds(checkMark, null, null, null)
                        } else if (!regexLowercase.containsMatchIn(it) && containLower) {
                            containLower = false
                            fragment_sign_up_password_require_lowercase_letters?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_24px, 0,0,0)
                        }

                        if (regexNumber.containsMatchIn(it) && !containNumber) {
                            containNumber = true
                            fragment_sign_up_password_require_numbers?.setCompoundDrawablesWithIntrinsicBounds(checkMark, null, null, null)
                        } else if (!regexNumber.containsMatchIn(it) && containNumber) {
                            containNumber = false
                            fragment_sign_up_password_require_numbers?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_24px, 0,0,0)
                        }

                        if (regexSpecialCharacter.containsMatchIn(it) && !containSpecialChar) {
                            containSpecialChar = true
                            fragment_sign_up_password_require_special_character?.setCompoundDrawablesWithIntrinsicBounds(checkMark, null, null, null)
                        } else if (!regexSpecialCharacter.containsMatchIn(it) && containSpecialChar) {
                            containSpecialChar = false
                            fragment_sign_up_password_require_special_character?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_24px, 0,0,0)
                        } else {}
                    }

                    // Notify observers of a new value.
                    notifyPropertyChanged(BR.model)
                }
            }
        }

        binding.model = model
        binding.onClick = listener

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }

        listener!!.apply {
            setCurrentFragment(SIGN_UP_FRAGMENT)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
        fun setCurrentFragment(name: String)
        fun onClickLogIn()
        fun onClickSignUp()
        fun onClickSignUp(user: User)
        fun onClickEmailVerification()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        @JvmStatic
        fun newInstance() =
            SignUpFragment()
    }
}
