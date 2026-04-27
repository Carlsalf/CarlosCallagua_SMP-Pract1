package es.ua.eps.filmoteca

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import es.ua.eps.filmoteca.databinding.ItemFilmBinding

class FilmListAdapter(
    context: Context,
    private val items: List<Film>
) : ArrayAdapter<Film>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: ItemFilmBinding
        val row: View

        if (convertView == null) {
            binding = ItemFilmBinding.inflate(LayoutInflater.from(context), parent, false)
            row = binding.root
            row.tag = binding
        } else {
            row = convertView
            binding = row.tag as ItemFilmBinding
        }

        val film = items[position]
        val posterRes = if (film.imageResId != 0) film.imageResId else R.mipmap.ic_launcher

        binding.imgPosterItem.setImageResource(posterRes)
        binding.txtTitleItem.text = film.title ?: "<Sin título>"
        binding.txtDirectorItem.text = film.director ?: ""

        return row
    }
}
