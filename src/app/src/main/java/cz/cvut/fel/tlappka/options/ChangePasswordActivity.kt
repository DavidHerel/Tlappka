package cz.cvut.fel.tlappka.options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.renderscript.ScriptGroup
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import cz.cvut.fel.tlappka.MainActivity
import cz.cvut.fel.tlappka.R
import cz.cvut.fel.tlappka.databinding.ActivityChangePasswordBinding
import cz.cvut.fel.tlappka.databinding.ActivitySettingsBinding
import cz.cvut.fel.tlappka.login.ForgotPasswordActivity
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChangePasswordBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityChangePasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        val toolbar : Toolbar = binding.changePasswordToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Nastavení účtu")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()
        binding.changePasswordButton.setOnClickListener {
            changePassword()
        }
        binding.forgottenPasswordText.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun changePassword() {
        val user : FirebaseUser? = auth.currentUser
        val userEmail = user!!.email
        val oldPassword = old_password.text.toString() //couldn't make it work with databinding
        val newPassword = new_password.text.toString()
        val newPassword2 = new_password_again.text.toString()
        if (oldPassword.isEmpty() || newPassword.isEmpty() || newPassword2.isEmpty()) {
            Toast.makeText(applicationContext, "Please fill all passwords", Toast.LENGTH_LONG).show()
            return
        } else {
            val credential = EmailAuthProvider.getCredential(userEmail!!, oldPassword)
            user.reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (newPassword.equals(newPassword2)) {
                        user.updatePassword(newPassword).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Password changed successfully.", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, "You have to enter the same password twice", Toast.LENGTH_LONG).show()
                    }
                }
                else {
                    Toast.makeText(applicationContext, "Wrong password", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, AccountSettingsActivity::class.java)
        NavUtils.navigateUpTo(this, intent)
        return true
    }
}
