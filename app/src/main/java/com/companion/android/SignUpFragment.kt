package com.companion.android

import android.app.Activity
import android.content.Context
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
import androidx.databinding.library.baseAdapters.BR
import com.companion.android.databinding.FragmentSignUpBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_sign_up.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SignUpFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var emailInputLayout: TextInputLayout? = null
    private var model: AuthenticationViewModel? = null

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

        //var passwordUppercase: CheckedTextView = view.findViewById(R.id.fragment_sign_up_password_require_uppercase_letters)
        //var passwordLowercase: CheckedTextView = view.findViewById(R.id.fragment_sign_up_password_require_lowercase_letters)
        //var passwordNumber: CheckedTextView = view.findViewById(R.id.fragment_sign_up_password_require_numbers)
        //var passwordSpecialCharacters: CheckedTextView = view.findViewById(R.id.fragment_sign_up_password_require_special_character)

        model = object : AuthenticationViewModel() {
            override fun setPassword(value: String) {
                // Avoids infinite loops.
                if (user.password != value) {
                    user.password = value

                    user.password?.let {
                        if (it.length >= 8) {
                            val checkMark: Drawable? = context!!.getDrawable(R.drawable.ic_check_circle_24px)
                            checkMark?.setTint(ContextCompat.getColor(context!!, R.color.colorSecondaryLight))
                            fragment_sign_up_password_require_count?.setCompoundDrawables(checkMark, null, null, null)
                        }
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
        fun onClickLogIn(code: Int)
        fun onClickSignUp(code: Int)
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
