package ng.com.tcli.www.tcli

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class Admin : AppCompatActivity() {
    lateinit var userDbRef : DatabaseReference
    lateinit var booksDBRef: DatabaseReference
    var usersAuth = FirebaseAuth.getInstance().currentUser
    var storage = FirebaseStorage.getInstance()
    // Create a storage reference
    var storageRef = storage.reference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //Each Book has an author, book (bookLoacation), description, image, title

        userDbRef = FirebaseDatabase.getInstance().getReference("Users")
        booksDBRef = FirebaseDatabase.getInstance().getReference("Books")
        var imageRef: StorageReference? = storageRef.child("Book Cover Images")
        var booksRef: StorageReference? = storageRef.child("Books")





    }
}
