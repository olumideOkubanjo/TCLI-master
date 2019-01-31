package ng.com.tcli.www.tcli

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

//This is the real one my G
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var mView: View
    lateinit var mTempBtn: Button


    init {
        mView = itemView
    }

    //Home Page
    fun setDetails(ctx: Context, title:String, description : String, image:String,model: BookModel){
        //Views
        var mTitleTv: TextView = mView.findViewById(R.id.rTitleTv)
        var mDetailTv: TextView = mView.findViewById(R.id.rDescriptionTv)
        var mImageIv: ImageView = mView.findViewById(R.id.rImageView)
        var mReadBtn: Button = mView.findViewById(R.id.read_button)

        mTempBtn = mReadBtn
        val context:Context = ctx

        var booksDbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Books")
        var bookREFID = "WHOOPS SOMETHING WENT WRONG"
        //Saving the model id to a file
        mTempBtn.setOnClickListener{
            booksDbRef.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("USER Info Changed Not needed Yet not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                    for(h in snapshot.children){
                        if(h.key.toString().equals("title") and h.getValue()!!.equals(model.getTitle())){
                            bookREFID = h.ref.parent!!.key.toString()
                        }
                    }

                    context.openFileOutput("temp.txt", Context.MODE_PRIVATE).use {
                        it.write(bookREFID.toByteArray())
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })


            ctx.startActivity(Intent(ctx,BookOverview::class.java))
        }


        mTitleTv.setText(title)
        mDetailTv.setText(description)
        Picasso.get().load(image).fit().into(mImageIv)
    }

}