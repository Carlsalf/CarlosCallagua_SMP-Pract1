package es.ua.eps.filmoteca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FilmRvAdapter(
    private val items: List<Film>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<FilmRvAdapter.VH>() {

    class VH(itemView: View, private val onClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val img: ImageView = itemView.findViewById(R.id.imgPosterRow)
        private val title: TextView = itemView.findViewById(R.id.txtTitleRow)
        private val director: TextView = itemView.findViewById(R.id.txtDirectorRow)

        fun bind(film: Film, position: Int) {
            val poster = if (film.imageResId != 0) film.imageResId else R.mipmap.ic_launcher
            img.setImageResource(poster)
            title.text = film.title ?: "<Sin título>"
            director.text = film.director ?: ""

            itemView.setOnClickListener { onClick(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_film_row, parent, false)
        return VH(v, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size
}
