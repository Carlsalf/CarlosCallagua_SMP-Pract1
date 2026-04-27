package es.ua.eps.filmoteca

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes

class FilmAdapter(
    context: Context,
    @LayoutRes private val layoutId: Int,
    private val items: List<Film>
) : ArrayAdapter<Film>(context, layoutId, items) {

    private data class Holder(
        val img: ImageView,
        val title: TextView,
        val director: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View
        val h: Holder
        if (convertView == null) {
            v = LayoutInflater.from(context).inflate(layoutId, parent, false)
            h = Holder(
                v.findViewById(R.id.imgPosterRow),
                v.findViewById(R.id.txtTitleRow),
                v.findViewById(R.id.txtDirectorRow)
            )
            v.tag = h
        } else {
            v = convertView
            h = v.tag as Holder
        }

        val film = items[position]
        h.img.setImageResource(if (film.imageResId != 0) film.imageResId else R.mipmap.ic_launcher)
        h.title.text = film.title ?: "<Sin título>"
        h.director.text = film.director ?: ""

        return v
    }
}
