package ng.com.tcli.www.tcli

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ReadBook : AppCompatActivity() {
    lateinit var bookPDFLocation:String
    lateinit var bookPdfView:PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_book)

        bookPDFLocation = getBookIdFromFile()
        deleteTempFile()

        bookPdfView = findViewById<View>(R.id.bookPDFView) as PDFView
        RetrievePdfStream().execute(bookPDFLocation)
    }


    inner class RetrievePdfStream: AsyncTask<String, Void, InputStream>() {

        override fun doInBackground(vararg params: String?): InputStream? {
            var inputStream : InputStream? = null
            try {
                var url = URL(params[0])
                var urlConnection = url.openConnection() as HttpURLConnection
                if (urlConnection.responseCode==200){
                    inputStream = BufferedInputStream(urlConnection.inputStream)

                }

            }catch (e: IOException){
                return null
            }
            return inputStream
        }

        override fun onPostExecute(inputStream: InputStream?) {
            bookPdfView.fromStream(inputStream).load()
        }
    }


    fun getBookIdFromFile():String{
        val file = File(applicationContext.filesDir, "temp.txt")
        val contents = file.readText() // Read file
        return contents
    }

    fun deleteTempFile(){
        val file = File(applicationContext.filesDir, "temp.txt")
        file.delete()
    }

}
