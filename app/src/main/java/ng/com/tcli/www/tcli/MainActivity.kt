package ng.com.tcli.www.tcli

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


//Log in Page
class MainActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginBtn = findViewById<View>(R.id.buttonLogin) as Button
        val registerSwitchText = findViewById<View>(R.id.signUp_text_switch) as TextView

        loginBtn.setOnClickListener {View ->
            this.login()
        }

        registerSwitchText.setOnClickListener {View ->
            this.register()
        }
    }

    private fun login(){
        val emailText = findViewById<View>(R.id.user_email_text) as EditText
        val passwordText = findViewById<View>(R.id.user_password_text) as EditText

        var email = emailText.text.toString().trim()
        var password = passwordText.text.toString()

        if (!email.isEmpty() && !password.isEmpty()){
            this.mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
//                    startActivity(Intent(this,HomePage::class.java))
                    startActivity(Intent(this,Welcome::class.java))
                    Toast.makeText(this,"Sucessful Login",Toast.LENGTH_LONG).show()
                    emailText.setText("")
                    passwordText.setText("")
                }else{
                    Toast.makeText(this,"Error Loggin In", Toast.LENGTH_LONG).show()
                }
            }
        }else{
            Toast.makeText(this,"Please Fill Required Fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun register(){
        startActivity(Intent(this, SignUpActivity :: class.java))
    }
}
