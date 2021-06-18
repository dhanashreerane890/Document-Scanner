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
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    private var progressDialog: ProgressDialog? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupActionBar()
        auth = FirebaseAuth.getInstance()

        emailAndPasswordLogin()


        val window: Window = window

// clear FLAG_TRANSLUCENT_STATUS flag:

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

// finally change the color
        window.statusBarColor = this.resources.getColor(R.color.theme_color)
        tvRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }





    private fun setupActionBar() {
        setSupportActionBar(toolbar_my_profile_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back)
            actionBar.title = resources.getString(R.string.sign_in)
        }
        toolbar_my_profile_activity.setNavigationOnClickListener { onBackPressed() }

    }
    private fun emailAndPasswordLogin() {


        progressDialog = ProgressDialog(this)
        btnLogin.setOnClickListener(View.OnClickListener { Login() })
        tvRegister.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                )
            )
        })
    }
    private fun Login() {
        val email: String = etEmail2.text.toString()
        val password: String = etPassword2.text.toString()
        if (TextUtils.isEmpty(email)) {
            etEmail2.error = "Enter your email"
            return
        } else if (TextUtils.isEmpty(password)) {
            etPassword2.error = "Enter your password"
            return
        } else if (password.length < 4) {
            etPassword2.error = "Length should be > 4"
            return
        } else if (!isValidEmail(email)!!) {
            etEmail2.error = "invalid email"
            return
        }
        progressDialog!!.setMessage("Please wait...")
        progressDialog!!.show()
        progressDialog!!.setCanceledOnTouchOutside(false)
        auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Successfully Login", Toast.LENGTH_LONG)
                        .show()

                      startActivity(Intent(this@LoginActivity,ContainerActivity::class.java))
                    finish()
//                    val user = auth!!.currentUser
//                    sendUserData(user)
                } else {
                    Toast.makeText(this@LoginActivity, "Login fail!", Toast.LENGTH_LONG).show()
                }
                progressDialog!!.dismiss()
            }
    }
    private fun isValidEmail(target: CharSequence): Boolean? {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}