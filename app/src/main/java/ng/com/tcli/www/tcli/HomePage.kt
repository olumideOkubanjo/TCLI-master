package ng.com.tcli.www.tcli

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ColorSpace
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class HomePage : AppCompatActivity(){

    lateinit var mRecyclerView: RecyclerView
    lateinit var booksDbRef : DatabaseReference

    lateinit var userInfoRefrence: DatabaseReference
    var usersAuth = FirebaseAuth.getInstance().currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val welcomeUser = findViewById<View>(R.id.user_welcome_text) as TextView

        var uid = usersAuth!!.uid

        lateinit var userFullName: String
        lateinit var userFullEmail:String


        userInfoRefrence = FirebaseDatabase.getInstance().getReference("Users")
        userInfoRefrence.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("USER Info Changed Not needed Yet not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                //
            }

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                for (h in 1..snapshot.childrenCount){
                    val result = snapshot.getValue() as HashMap<String,String>
                    val userid = snapshot.key.toString()

                    if (userid.equals(uid)){
                        userFullName = result.get("Name").toString()
                        userFullEmail = result.get("Email").toString()
                        welcomeUser.text = "Welcome " + userFullName
                        break
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


        //Recycler View
        mRecyclerView = findViewById(R.id.recycler_view)
        mRecyclerView.setHasFixedSize(true)

        //set layout as Linear Layout
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        //Send Queary to Firebase Database
        booksDbRef = FirebaseDatabase.getInstance().getReference("Books")


    }

    override fun onStart() {
        super.onStart()
        var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<BookModel,ViewHolder> = object : FirebaseRecyclerAdapter<BookModel,ViewHolder>(BookModel::class.java,R.layout.bookrow,ViewHolder::class.java,booksDbRef){
            override fun populateViewHolder(viewHolder: ViewHolder?, model: BookModel?, position: Int) {
                viewHolder!!.setDetails(applicationContext,model!!.getTitle(),model.getDescription(),model.getImage(),model)
            }


        }
        //set Adapter to recycler View
        mRecyclerView.adapter = firebaseRecyclerAdapter
    }

}
