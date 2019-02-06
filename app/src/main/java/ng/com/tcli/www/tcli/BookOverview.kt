package ng.com.tcli.www.tcli

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.io.File

//Thiis the real one my G

class BookOverview : AppCompatActivity() {
    lateinit var bookId:String
    lateinit var userDbRef : DatabaseReference
    var usersAuth = FirebaseAuth.getInstance().currentUser

    lateinit var mBooksDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_overview)

        //Getting the Book ID
        bookId =  getIdFromFile()
        deleteTempFile()

        val bookChosenTitle = findViewById<View>(R.id.book_chosen_title) as TextView
        val bookChosenImage = findViewById<View>(R.id.book_cover_Image) as ImageView
        val bookChosenAuthor = findViewById<View>(R.id.author_full) as TextView
        val bookChosenDescription = findViewById<View>(R.id.description_full) as TextView
        val bookChosenButton = findViewById<View>(R.id.sign_out_begin_reading_btn) as Button

        mBooksDB = FirebaseDatabase.getInstance().getReference("Books").child(bookId)
        userDbRef = FirebaseDatabase.getInstance().getReference("Users").child(usersAuth!!.uid)

        //Set title
        mBooksDB.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                //Set title
                bookChosenTitle.text = snapshot.child("title").value.toString()

                //Setting image
                Picasso.get().load(snapshot.child("image").value.toString()).fit().into(bookChosenImage)

                //Set author
                bookChosenAuthor.text = "Author: " + snapshot.child("author").value.toString()

                //Set Description
                bookChosenDescription.text = "Description: " + snapshot.child("description").value.toString()

                //Set Button to Open a new Page and save the book Location id
                bookChosenButton.setOnClickListener {

                    //Saving the Book PDF Location to the temp file
                    applicationContext.openFileOutput("temp.txt", Context.MODE_PRIVATE).use {
                        it.write(snapshot.child("book").value.toString().toByteArray())

                        // Attaching the book to the user's name
                        var tempBookSignedLocation = snapshot.child("book").value as String
                        var tempNameBookSigned =  snapshot.child("title").value.toString()
                        userDbRef.child("Book").setValue(tempNameBookSigned)
                        userDbRef.child("BookLocation").setValue(tempBookSignedLocation)
                    }

                    //Starting the PDF View Activity for the ebook
                    startActivity(Intent(applicationContext,ReadBook::class.java))
                }


            }
        })




    }

    fun getIdFromFile():String{
        val file = File(applicationContext.filesDir, "temp.txt")
        val contents = file.readText() // Read file
        return contents
    }

    fun deleteTempFile(){
        val file = File(applicationContext.filesDir, "temp.txt")
        file.delete()
    }

}
