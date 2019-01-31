package ng.com.tcli.www.tcli

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.TextView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home_page.*
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception

//Thiis the real one my G

class BookOverview : AppCompatActivity() {
    lateinit var bookId:String

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

        //Set title
        mBooksDB.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                //Set title
                bookChosenTitle.text = snapshot.child("title").getValue().toString()

                //Setting image
                Picasso.get().load(snapshot.child("image").getValue().toString()).fit().into(bookChosenImage)

                //Set author
                bookChosenAuthor.text = "Author: " + snapshot.child("author").getValue().toString()

                //Set Description
                bookChosenDescription.text = "Description: " + snapshot.child("description").getValue().toString()

                //Set Button to Open a new Page and save the book Location id
                bookChosenButton.setOnClickListener {

                    //Saving the Book PDF Location to the temp file
                    applicationContext.openFileOutput("temp.txt", Context.MODE_PRIVATE).use {
                        it.write(snapshot.child("book").getValue().toString().toByteArray())
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
