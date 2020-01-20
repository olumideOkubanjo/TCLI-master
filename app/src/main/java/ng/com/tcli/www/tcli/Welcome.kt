package ng.com.tcli.www.tcli

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_welcome.*

class Welcome : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var userDbRef : DatabaseReference
    var usersAuth = FirebaseAuth.getInstance().currentUser


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId==R.id.nav_home -> //Home Page
                startActivity(Intent(this,Welcome::class.java))

            item.itemId==R.id.nav_profile -> //Open a fragment for profile
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ProfileFragment()).commit()

            item.itemId==R.id.nav_settings -> //Start the settings activity
                startActivity(Intent(this,Settings::class.java))

            item.itemId==R.id.nav_contact -> //Open a fragment for Contact
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ContactFragment()).commit()

            item.itemId==R.id.nav_share -> //open Share activity
                startActivity(Intent(this,Share::class.java))

            item.itemId==R.id.nav_booksAvailable ->
                startActivity(Intent(this,HomePage::class.java))
            item.itemId==R.id.nav_admin -> {
                if (usersAuth!!.uid == "F84FjJVTW2Tx8ZFbc95bWVF9yvy2") {
                    Toast.makeText(this,"Admin Access Granted", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Admin::class.java))
                }else{
                    Toast.makeText(this,"Admin Access Denied", Toast.LENGTH_LONG).show()
                }
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private lateinit var drawer:DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val toolbar = findViewById<View>(R.id.toolbar) as android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar)


        drawer = findViewById(R.id.drawer_layout)
        var navigationView : NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //If Device is rotated
        if(savedInstanceState==null){
            navigationView.setCheckedItem(R.id.nav_home)
            //Open a fragment here
            //Because I already use the welcome page, there's no need to load a fragment
//            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ProfileFragment()).commit()

        }



        var userID = usersAuth!!.uid

        val navView = nav_view.getHeaderView(0)
        var userNavPicture = navView.findViewById(R.id.user_profile_picture) as ImageView
        var userNavName = navView.findViewById(R.id.user_nav_name) as TextView
        var userNavEmail = navView.findViewById(R.id.user_nav_email) as TextView

        userDbRef = FirebaseDatabase.getInstance().getReference("Users").child(userID)
        userDbRef.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {

                // Temp Filler

            }

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                when {
                    //set User Full Name
                    snapshot.key.toString().equals("Name") ->
                        //Set User Profile Picture
                        userNavName.text = snapshot.value.toString()
                    snapshot.key.toString().equals("picture")->
                        Picasso.get().load(snapshot.value.toString()).fit().into(userNavPicture)
                    snapshot.key.toString().equals("Email")->
                        userNavEmail.text = snapshot.value.toString()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                // 
            }
        })





    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
        }
    }

}
