package com.dsc.grocerymanagement.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dsc.grocerymanagement.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PhoneLoginActivity : AppCompatActivity() {
    private lateinit var mobileNumber: EditText
    private lateinit var enterOtpMobile: EditText
    private lateinit var btnLoginMobile: Button
    private lateinit var register:TextView
    private lateinit var btnRequestOtp: Button
    private var storedVerificationId: String = "0"
    private var mAuth: FirebaseAuth? = null
    private var pAuth: PhoneAuthProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_phone_login)
        mobileNumber = findViewById(R.id.MobileNumber)
        enterOtpMobile = findViewById(R.id.enterOtpMobile)
        register=findViewById(R.id.register)
        btnLoginMobile = findViewById(R.id.btnLoginMobile)
        btnRequestOtp = findViewById(R.id.btnRequestOtp)
        btnLoginMobile.visibility = View.GONE
        enterOtpMobile.visibility = View.GONE
        mAuth = FirebaseAuth.getInstance()
        pAuth = PhoneAuthProvider.getInstance()
        btnRequestOtp.setOnClickListener {
            val mobile = mobileNumber.text.toString()
            if (mobile.length == 10) {
                val mobNumber = "+91$mobile"
                pAuth!!.verifyPhoneNumber(
                        mobNumber, // Phone number to verify
                        60, // Timeout duration
                        TimeUnit.SECONDS, // Unit of timeout
                        this, // Activity (for callback binding)
                        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) { //If user is automatically verified by system
                                signInWithPhoneAuthCredential(phoneAuthCredential) //Logging in user as he/she is verified
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                when (e) {
                                    is FirebaseAuthInvalidCredentialsException -> {
                                        Toast.makeText(this@PhoneLoginActivity, "Invalid phone number",
                                                Toast.LENGTH_SHORT).show()
                                    }
                                    is FirebaseTooManyRequestsException -> {
                                        Toast.makeText(this@PhoneLoginActivity, "Maximum number of OTP'S sent!\nPlease try again later!!",
                                                Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        Toast.makeText(this@PhoneLoginActivity, e.toString(),
                                                Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            override fun onCodeSent(
                                    verificationId: String,
                                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                            ) {
                                Toast.makeText(this@PhoneLoginActivity, "OTP has been sent to your mobile!",
                                        Toast.LENGTH_SHORT).show()
                                storedVerificationId = verificationId //Verification Id to login user through OTP
                                this@PhoneLoginActivity.enableUserManuallyInputCode() //Making the OTP input field and login button available upon sending OTP
                            }
                        }) // OnVerificationStateChangedCallbacks

            } else {
                mobileNumber.error = "Number should be of 10 digits"
                mobileNumber.requestFocus()
            }
        }
        btnLoginMobile.setOnClickListener {
            val otp = enterOtpMobile.text.toString().trim()
            if (otp.length == 6) {
                val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
                signInWithPhoneAuthCredential(credential) //To sign-In the user with the input OTP
            } else {
                //Invalid OTP length
                enterOtpMobile.error = "Invalid OTP"
                enterOtpMobile.requestFocus()
            }
        }
        register.setOnClickListener {
            startActivity(Intent(this@PhoneLoginActivity,RegisterActivity::class.java))
        }
        mobileNumber.addTextChangedListener(textWatcher)
        enterOtpMobile.addTextChangedListener(textWatcher)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and change UI to Main Activity.
        val currentUser = mAuth!!.currentUser
        println("userr $currentUser")
        updateUI(currentUser)
    }

    fun enableUserManuallyInputCode() {
        //Making the OTP input field and login button available upon sending OTP
        btnLoginMobile.visibility = View.VISIBLE
        enterOtpMobile.visibility = View.VISIBLE
        btnRequestOtp.text = getString(R.string.Resend_OTP)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    when {
                        task.isSuccessful -> {
                            // Sign-In successful either direct or through OTP.
                            //Intent to next activity
                            val user = task.result?.user //Details of user which has currently logged-In
                            updateUI(user) //This is to change the activity upon login
                            //println("users $user")
                        }
                        task.exception is FirebaseAuthInvalidCredentialsException -> {
                            // Sign in failed
                            Toast.makeText(this@PhoneLoginActivity, "Incorrect OTP!",
                                    Toast.LENGTH_SHORT).show()
                            // The verification code entered was invalid
                        }
                        else -> {
                            Toast.makeText(this@PhoneLoginActivity, "Some error occurred!!",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
                }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(this@PhoneLoginActivity, "Login successful",
                    Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@PhoneLoginActivity, DashboardActivity::class.java))//Intent to main activity
            finish()
        }
    }

    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //println("watcher: length ${s!!.length}")
            //code to hide keyboard
            if (s?.length == 6&&currentFocus==enterOtpMobile) {
                enterOtpMobile.hideKeyboard()
            }
            if (s?.length == 10&&currentFocus==mobileNumber) {
                mobileNumber.hideKeyboard()
            }
        }
    }

    fun EditText.hideKeyboard(): Boolean {
        return (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
    }
}