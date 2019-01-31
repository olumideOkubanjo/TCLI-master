package ng.com.tcli.www.tcli

import android.accounts.AuthenticatorDescription
import android.view.View

class BookModel {
    private lateinit var title :String
    private lateinit var image :String
    private lateinit var description: String



    //Constructor
    init {
    }
    fun getTitle(): String {
        return this.title
    }

    fun setTitle(title:String){
        this.title = title
    }

    fun getImage():String{
        return this.image
    }

    fun setImage(img:String){
        this.image = img
    }

    fun getDescription():String{
        return this.description
    }

    fun setDescription(disc:String){
        this.description = disc
    }

}