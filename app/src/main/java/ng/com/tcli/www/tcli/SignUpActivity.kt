package ng.com.tcli.www.tcli

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


//Log in Page
class SignUpActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mDatabase = FirebaseDatabase.getInstance().getReference("Users")

        val signUpBtn = findViewById<View>(R.id.button_sign_up) as Button

        signUpBtn.setOnClickListener{
            this.registerUser()
        }

    }

    private fun registerUser(){
        val nametext = findViewById<View>(R.id.user_full_name_text) as EditText
        val emailText = findViewById<View>(R.id.user_email_text) as EditText
        val passwordText = findViewById<View>(R.id.user_password_text) as EditText
        val verifyText = findViewById<View>(R.id.user_verify_password_text) as EditText

        var userFullName = nametext.text.toString().trim()
        var userEmail = emailText.text.toString().trim()
        var userPass = passwordText.text.toString()
        var userVerifyPass =  verifyText.text.toString()

        if(!userFullName.isEmpty() && !userEmail.isEmpty() && !userPass.isEmpty() && !userVerifyPass.isEmpty()){
            //Is email Valid
            if(userEmail.contains('@')){
                //Do the two Password Fields match
                if(userPass.equals(userVerifyPass)){
                    //Fullname Must have at least one space character
                    if(userFullName.contains(' ')){
                        //Chekc if the user is nigerian /////////////////////////////
                        mAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(this){task->
                            if (task.isSuccessful){
                                //Make sure to authorize user so the user can edit the database on registration
                                val user = mAuth.currentUser
                                val uid = user!!.uid.toString()
                                var userMap = HashMap<String,String>()
                                userMap.put("Name", userFullName)
                                userMap.put("Email", userEmail)
                                userMap.put("Password", userPass)
                                mDatabase.child(uid).setValue(userMap)
                                startActivity(Intent(this,Welcome::class.java))
                                Toast.makeText(this,"Successfully Created usersAuth",Toast.LENGTH_LONG).show()
                                nametext.setText("")
                                emailText.setText("")
                                passwordText.setText("")
                                verifyText.setText("")
                            }else{
                                Toast.makeText(this,"Error Creating User",Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        Toast.makeText(this,"Ensure First and Last name are Separated",Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this,"Password Fields do not Match",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"Please Enter a Valid Email Address",Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this,"Please Fill in all Required Fields",Toast.LENGTH_LONG).show()
        }
    }

}
