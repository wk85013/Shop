package ute.com.shop

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btn_signup.setOnClickListener {
            val sEmail = ed_email.text.toString().trim()
            val sPassword = ed_password.text.toString()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener {
                if (it.isSuccessful()) {
                    AlertDialog.Builder(this)
                        .setTitle("Signup")
                        .setMessage("success")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                            setResult(Activity.RESULT_OK)
                            finish()
                        })
                        .show()
//                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                } else
                    AlertDialog.Builder(this)
                        .setTitle("Signup")
                        .setMessage(it.exception?.message)
                        .show()


            }


        }
    }
}
