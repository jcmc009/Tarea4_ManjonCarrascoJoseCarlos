package dam.pmdm.spyrothedragon.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dam.pmdm.spyrothedragon.MainActivity
import dam.pmdm.spyrothedragon.R
import dam.pmdm.spyrothedragon.models.World

class WorldsAdapter(
    private val list: List<World>
) : RecyclerView.Adapter<WorldsAdapter.WorldsViewHolder>() {

    private val worldImages = mapOf(
        "sunny_beach" to R.drawable.sunny_beach,
        "midday_gardens" to R.drawable.midday_gardens,
        "autumn_plains" to R.drawable.autumn_plains,
        "glimmer" to R.drawable.glimmer,
        "cloud_spires" to R.drawable.cloud_spires,
        "hurricane_halls" to R.drawable.hurricane_halls,
        "frozen_altars" to R.drawable.frozen_altars,
        "lost_fleet" to R.drawable.lost_fleet,
        "sunset_beach" to R.drawable.sunset_beach
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview, parent, false)
        return WorldsViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorldsViewHolder, position: Int) {
        val world = list[position]
        holder.nameTextView.text = world.name

        val drawableRes = worldImages[world.image] ?: R.drawable.placeholder
        holder.imageImageView.setImageResource(drawableRes)

        // LÓGICA DEL EASTER EGG (3 CLICS) EN ESTE MUNDO

        holder.itemView.setOnClickListener {
            // 1. Calculamos la hora actual en milisegundos para tomarla como referencia
            val tiempoActual = System.currentTimeMillis()

            // 2. Si pulsa en menos de 500ms desde el último clic, sumamos 1
            if (tiempoActual - holder.tiempoUltimoClic < 500) {
                holder.contadorClics++
            } else {
                // Si ha tardado mucho, reiniciamos a 1
                holder.contadorClics = 1
            }

            // Guardamos el tiempo para compararlo en el siguiente clic
            holder.tiempoUltimoClic = tiempoActual

            // 3. Comprobamos si ha hecho el triple clic
            if (holder.contadorClics == 3) {
                // ¡BINGO! Reseteamos el contador por si quiere volver a hacerlo más tarde
                holder.contadorClics = 0

                // Llamamos a la MainActivity para que ponga el vídeo
                val mainActivity = holder.itemView.context as? MainActivity
                mainActivity?.reproducirVideo(R.raw.spyro)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    class WorldsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val imageImageView: ImageView = itemView.findViewById(R.id.image)

        // Variables para controlar el Easter Egg guardadas en cada tarjeta de mundo
        var contadorClics = 0
        var tiempoUltimoClic = 0L
    }
}



