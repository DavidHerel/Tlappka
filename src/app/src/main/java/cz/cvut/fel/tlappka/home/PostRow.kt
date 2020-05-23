package cz.cvut.fel.tlappka.home

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import cz.cvut.fel.tlappka.R
import kotlinx.android.synthetic.main.activity_post_row_newsfeed.*

class PostRow : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_row_newsfeed)
        comment.setOnFocusChangeListener { v, hasFocus ->
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.currentFocus?.windowToken, 0)
        }
    }
}
