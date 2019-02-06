package ng.com.tcli.www.tcli

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment: Fragment() {
    lateinit var userDbRef : DatabaseReference
    var usersAuth = FirebaseAuth.getInstance().currentUser
    lateinit var mBooksDB: DatabaseReference

    private lateinit var currentBookName:String
    private lateinit var currentBookLocation:String



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        var userId = usersAuth!!.uid

        fun createBookPDFLocationFile(name:String){
            view.context.openFileOutput("temp.txt", Context.MODE_PRIVATE).use {
                //Wrtie the book Location to the file
                it.write(currentBookLocation.toByteArray())
            }
        }

        userDbRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)


        userDbRef.addChildEventListener(object: ChildEventListener, View.OnClickListener {
            override fun onClick(v: View?) {
                //Add an On click method
                when {
                    R.id.currentBookBlock==v!!.id->{
                        //Create PDF File Location
                        //makeSure Book Location is defined before this
                        createBookPDFLocationFile(currentBookName)
                        startActivity(Intent(v.context,ReadBook::class.java))
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                //
            }

            override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
                //
            }

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                when{
                    //set User Full Name
                    snapshot.key.toString().equals("Name") ->
                        view.user_name.text = snapshot.value.toString()
                    //Set User Profile Picture
                    snapshot.key.toString().equals("picture") ->
                        Picasso.get().load(snapshot.value.toString()).fit().into(view.user_personalProfile_Pic)
                    //Set Books read
                    snapshot.key.toString().equals("books_read")->
                        view.books_read.text = snapshot.value.toString()
                    //Set Favorite genre
                    snapshot.key.toString().equals("genre")->
                        view.user_favorite_Genre.text = snapshot.value.toString()
                    //Set email
                    snapshot.key.toString().equals("Email")->
                        view.user_email.text = snapshot.value.toString()
                    //set Phone Number
                    snapshot.key.toString().equals("phone")->
                        view.user_phoneNumber.text = snapshot.value.toString()
                    //Set Name/id of current book and add an onclick listener to it(Create that temp file to open the book in pdf view)
                    snapshot.key.toString().equals("Book")->{
                        view.user_currentBook.text = "Continue : " + snapshot.value.toString()
                        currentBookName = snapshot.value.toString()
                        view.currentBookBlock.setOnClickListener(this)
                    }
                    snapshot.key.toString().equals("BookLocation")->{
                        currentBookLocation = snapshot.value.toString()
                    }
                    //Set Achievements(Not sure what that is yet)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })



        return view
    }

}