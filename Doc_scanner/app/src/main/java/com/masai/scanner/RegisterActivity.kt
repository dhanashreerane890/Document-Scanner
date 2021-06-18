package com.masai.scanner

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.masai.scanner.bottom_navigation.ContainerActivity

import com.masai.scanner.bottom_navigation.HomeFragment

import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    private var progressDialog: ProgressDialog? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupActionBar()

        val window: Window = window

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.statusBarColor = this.resources.getColor(R.color.theme_color)
        auth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        emailAndPasswordLogin()
    }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_my_register_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back)
            actionBar.title = resources.getString(R.string.sign_in)
        }
       toolbar_my_register_activity.setNavigationOnClickListener { onBackPressed() }

    }
    private fun emailAndPasswordLogin() {

        btnRegister.setOnClickListener(View.OnClickListener { register() })
        tvSignIn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
            )
        })
    }
    private fun register() {
        val email: String = etEmail.text.toString()
        val password: String = etPassword.text.toString()
        if (TextUtils.isEmpty(email)) {
            etEmail.error = "Enter your email"
            return
        } else if (TextUtils.isEmpty(password)) {
            etPassword.error = "Enter your password"
            return
        } else if (password.length < 4) {
            etPassword.setError("Length should be > 4")
            return
        } else if (!isValidEmail(email)!!) {
            etEmail.error = "invalid email"
            return
        }
        progressDialog!!.setMessage("Please wait...")
        progressDialog!!.show()
        progressDialog!!.setCanceledOnTouchOutside(false)
        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Successfully registered",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this@RegisterActivity,ContainerActivity::class.java))
                    finish()
//                    val user = auth!!.currentUser
//                    sendUserData(user)
                } else {
                    Toast.makeText(this@RegisterActivity, "Sign up fail!", Toast.LENGTH_LONG)
                        .show()
                }
                progressDialog!!.dismiss()
            }
    }
    private fun isValidEmail(target: CharSequence): Boolean? {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

}