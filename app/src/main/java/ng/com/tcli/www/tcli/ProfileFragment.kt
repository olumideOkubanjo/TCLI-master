package ng.com.tcli.www.tcli

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.nav_header.view.*

class ProfileFragment: Fragment() {
    lateinit var userDbRef : DatabaseReference
    var usersAuth = FirebaseAuth.getInstance().currentUser



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater!!.inflate(R.layout.fragment_profile, container, false)


        var userId = usersAuth!!.uid

        userDbRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)


        userDbRef.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                when{
                    //set User Full Name
                    snapshot.key.toString().equals("Name") ->
                        view.user_name.text = snapshot.getValue().toString()
                    //Set User Profile Picture
                    snapshot.key.toString().equals("picture") ->
                        Picasso.get().load(snapshot.getValue().toString()).fit().into(view.user_personalProfile_Pic)
                    //Set Books read
                    snapshot.key.toString().equals("books_read")->
                        view.books_read.text = snapshot.getValue().toString()
                    //Set Favorite genre
                    snapshot.key.toString().equals("genre")->
                        view.user_favorite_Genre.text = snapshot.getValue().toString()
                    //Set email
                    snapshot.key.toString().equals("Email")->
                        view.user_email.text = snapshot.getValue().toString()
                    //set Phone Number
                    snapshot.key.toString().equals("phone")->
                        view.user_phoneNumber.text = snapshot.getValue().toString()
                    //Set Name/id of current book and add an onclick listener to it(Create that temp file to open the book in pdf view)
                    snapshot.key.toString().equals("Book")->
                        view.user_currentBook.text = "Continue : " + snapshot.getValue().toString()
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