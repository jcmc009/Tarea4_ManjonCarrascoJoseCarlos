package dam.pmdm.spyrothedragon

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private var mediaPlayer: android.media.MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.navHostFragment)

        navHostFragment?.let {
            navController = NavHostFragment.findNavController(it)
            NavigationUI.setupWithNavController(binding.navView, navController!!)
            NavigationUI.setupActionBarWithNavController(this, navController!!)
        }

        binding.navView.setOnItemSelectedListener { menuItem ->
            selectedBottomMenu(menuItem)
        }

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_characters, R.id.navigation_worlds, R.id.navigation_collectibles -> {
                    // En las pantallas de los tabs no mostramos la flecha atrás
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }

                else -> {
                    // En el resto de pantallas sí
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
            }
        }
// --- COMPROBAR SharedPreferences (MOSTRADO ÚNICO) ---
        val sharedPref = getSharedPreferences("SpyroAppPrefs", MODE_PRIVATE)
        val guiaVista = sharedPref.getBoolean("guia_vista", false) // false es el valor por defecto

        if (!guiaVista) {
            // Si NO se ha visto (false), encendemos la guía
            binding.guideLayout.visibility = View.VISIBLE
            reproducirSonido(R.raw.sonido_opening)
        }
        reproducirSonido(R.raw.sonido_opening)
        // Si ya se ha visto (true), no hacemos nada porque en los XML ya le pusimos visibility="gone" a todas


        // 2. Buscamos el botón "Comenzar" dentro de la vista inflada
        var btnComenzar = binding.root.findViewById<Button>(R.id.btn_comenzar)

        // 3. Le damos la acción de clic al botón
        btnComenzar?.setOnClickListener {

            binding.guideLayout.animate().alpha(0f).setDuration(400).withEndAction {
                binding.guideLayout.visibility = View.GONE

                // AHORA MOSTRAMOS LA PANTALLA 2
                binding.guidePersonajesLayout.visibility = View.VISIBLE

                // Animación del bocadillo
                val bocadillo = binding.guidePersonajesLayout.findViewById<View>(R.id.tv_bocadillo)
                val animacionLatido = android.view.animation.AlphaAnimation(0.7f, 1.0f)
                animacionLatido.duration = 800
                animacionLatido.repeatMode = android.view.animation.Animation.REVERSE
                animacionLatido.repeatCount = android.view.animation.Animation.INFINITE
                bocadillo.startAnimation(animacionLatido)
            }.start()
        }// 4. Acción para cerrar Pantalla 2 y pasar a la Pantalla 3
        binding.guidePersonajesLayout.setOnClickListener {
            // Ocultamos la pantalla 2
            binding.guidePersonajesLayout.visibility = View.GONE

            // Hacemos que la app cambie sola a la pestaña "Mundos" de fondo
            binding.navView.selectedItemId = R.id.nav_worlds

            // Mostramos la Pantalla 3
            binding.guideMundosLayout.visibility = View.VISIBLE

            // ANIMACIÓN PANTALLA 3: Hacemos que el bocadillo flote (arriba y abajo)
            val bocadilloMundos: View? =
                binding.guideMundosLayout.findViewById(R.id.tv_bocadillo_mundos)
            if (bocadilloMundos != null) {
                // TranslateAnimation mueve el elemento (de X a X, de Y a Y)
                val animacionFlotar = android.view.animation.TranslateAnimation(
                    0f, 0f, // No se mueve en horizontal
                    0f, -25f // Sube 25 píxeles hacia arriba
                )
                animacionFlotar.duration = 1000 // Tarda 1 segundo en subir
                animacionFlotar.repeatMode =
                    android.view.animation.Animation.REVERSE // Baja de vuelta
                animacionFlotar.repeatCount =
                    android.view.animation.Animation.INFINITE // Bucle infinito
                bocadilloMundos.startAnimation(animacionFlotar)
            }
        }

        // 5. Acción para cerrar Pantalla 3 y pasar a la Pantalla 4
        binding.guideMundosLayout.setOnClickListener {
            // Ocultamos la pantalla 3
            binding.guideMundosLayout.visibility = View.GONE

            // Obtenemos el ID del tercer elemento del menú directamente para no fallar
            val tercerTabId = binding.navView.menu.getItem(2).itemId
            binding.navView.selectedItemId = tercerTabId

            // Mostramos la Pantalla 4
            binding.guideColeccionablesLayout.visibility = View.VISIBLE

            // ANIMACIÓN PANTALLA 4: Zoom
            val bocadilloColeccionables: View? =
                binding.guideColeccionablesLayout.findViewById(R.id.tv_bocadillo_coleccionables)
            if (bocadilloColeccionables != null) {
                val animacionZoom = android.view.animation.ScaleAnimation(
                    0.95f, 1.05f, // Cambia tamaño en X (ancho)
                    0.95f, 1.05f, // Cambia tamaño en Y (alto)
                    android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f, // Centro X
                    android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f  // Centro Y
                )
                animacionZoom.duration = 600 // Medio segundo
                animacionZoom.repeatMode = android.view.animation.Animation.REVERSE
                animacionZoom.repeatCount = android.view.animation.Animation.INFINITE
                bocadilloColeccionables.startAnimation(animacionZoom)
            }
        }

        // 6. Acción para cerrar Pantalla 4 y pasar a la Pantalla 5 (Info)
        binding.guideColeccionablesLayout.setOnClickListener {
            // Ocultamos la pantalla 4
            binding.guideColeccionablesLayout.visibility = View.GONE

            // Mostramos la Pantalla 5
            binding.guideInfoLayout.visibility = View.VISIBLE

            // ANIMACIÓN PANTALLA 5: Balanceo
            val bocadilloInfo: View? = binding.guideInfoLayout.findViewById(R.id.tv_bocadillo_info)
            if (bocadilloInfo != null) {
                val animacionBalanceo = android.view.animation.RotateAnimation(
                    -3f,
                    3f, // Rota ligeramente de izquierda a derecha (-3 a 3 grados)
                    android.view.animation.Animation.RELATIVE_TO_SELF,
                    0.5f, // Eje X en el centro
                    android.view.animation.Animation.RELATIVE_TO_SELF,
                    0.0f  // Eje Y arriba (efecto péndulo)
                )
                animacionBalanceo.duration = 400
                animacionBalanceo.repeatMode = android.view.animation.Animation.REVERSE
                animacionBalanceo.repeatCount = android.view.animation.Animation.INFINITE
                bocadilloInfo.startAnimation(animacionBalanceo)
            }
        }

        // 7. Acción para FINALIZAR la guía desde la Pantalla 5
        binding.guideInfoLayout.setOnClickListener {
            // Ocultamos la última pantalla con un desvanecimiento
            binding.guideInfoLayout.animate().alpha(0f).setDuration(400).withEndAction {
                binding.guideInfoLayout.visibility = View.GONE

                // Restauramos el alpha por buenas prácticas
                binding.guideInfoLayout.alpha = 1f

                // Mensaje de fin de tutorial
                Toast.makeText(
                    this, "¡Estás listo para explorar el mundo de Spyro!", Toast.LENGTH_LONG
                ).show()
            }.start()
        }
// ========================================================
        // --- NAVEGACIÓN DE LA GUÍA INTERACTIVA (APARTADO B) ---
        // ========================================================

        // 1. Botón "Comenzar" (Pantalla 1 -> Pantalla 2)
        btnComenzar = binding.guideLayout.findViewById<Button>(R.id.btn_comenzar)
        btnComenzar?.setOnClickListener {
            binding.guideLayout.animate().alpha(0f).setDuration(400).withEndAction {
                binding.guideLayout.visibility = View.GONE
                binding.guidePersonajesLayout.visibility = View.VISIBLE

                // Animación bocadillo 2 (Latido)
                val bocadillo = binding.guidePersonajesLayout.findViewById<View>(R.id.tv_bocadillo)
                if (bocadillo != null) {
                    val animacionLatido = android.view.animation.AlphaAnimation(0.7f, 1.0f)
                    animacionLatido.duration = 800
                    animacionLatido.repeatMode = android.view.animation.Animation.REVERSE
                    animacionLatido.repeatCount = android.view.animation.Animation.INFINITE
                    bocadillo.startAnimation(animacionLatido)
                }
            }.start()
        }

        // 2. Botón "Siguiente" de Personajes (Pantalla 2 -> Pantalla 3)
        val btnNextPersonajes =
            binding.guidePersonajesLayout.findViewById<Button>(R.id.btn_next_personajes)
        btnNextPersonajes?.setOnClickListener {
            binding.guidePersonajesLayout.visibility = View.GONE
            binding.navView.selectedItemId = R.id.nav_worlds // Navega en el menú
            binding.guideMundosLayout.visibility = View.VISIBLE
            reproducirSonido(R.raw.sonido_avanzar)
            // Animación bocadillo 3 (Flotar)
            val bocadilloMundos =
                binding.guideMundosLayout.findViewById<View>(R.id.tv_bocadillo_mundos)
            if (bocadilloMundos != null) {
                val animacionFlotar = android.view.animation.TranslateAnimation(0f, 0f, 0f, -25f)
                animacionFlotar.duration = 1000
                animacionFlotar.repeatMode = android.view.animation.Animation.REVERSE
                animacionFlotar.repeatCount = android.view.animation.Animation.INFINITE
                bocadilloMundos.startAnimation(animacionFlotar)
            }
        }

        // 3. Botón "Siguiente" de Mundos (Pantalla 3 -> Pantalla 4)
        val btnNextMundos = binding.guideMundosLayout.findViewById<Button>(R.id.btn_next_mundos)
        btnNextMundos?.setOnClickListener {
            binding.guideMundosLayout.visibility = View.GONE
            binding.navView.selectedItemId =
                binding.navView.menu.getItem(2).itemId // Navega en el menú
            binding.guideColeccionablesLayout.visibility = View.VISIBLE
            reproducirSonido(R.raw.sonido_avanzar)
            // Animación bocadillo 4 (Zoom)
            val bocadilloColeccionables =
                binding.guideColeccionablesLayout.findViewById<View>(R.id.tv_bocadillo_coleccionables)
            if (bocadilloColeccionables != null) {
                val animacionZoom = android.view.animation.ScaleAnimation(
                    0.95f,
                    1.05f,
                    0.95f,
                    1.05f,
                    android.view.animation.Animation.RELATIVE_TO_SELF,
                    0.5f,
                    android.view.animation.Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                animacionZoom.duration = 600
                animacionZoom.repeatMode = android.view.animation.Animation.REVERSE
                animacionZoom.repeatCount = android.view.animation.Animation.INFINITE
                bocadilloColeccionables.startAnimation(animacionZoom)
            }
        }
        // 7. Acción para pasar de la Pantalla 5 a la Pantalla 6 (Resumen final)
        binding.guideInfoLayout.setOnClickListener {
            reproducirSonido(R.raw.sonido_avanzar)
            // Ocultamos la pantalla 5
            binding.guideInfoLayout.visibility = View.GONE

            // Mostramos la Pantalla 6 final
            binding.guideEndLayout.visibility = View.VISIBLE
        }

        // 8. ACCIÓN FINAL: Botón de "¡A JUGAR!" en la Pantalla 6
        var btnFinalizar = binding.guideEndLayout.findViewById<Button>(R.id.btn_finalizar)
        btnFinalizar?.setOnClickListener {
            // Desvanecemos la última capa para revelar la app al completo
            binding.guideEndLayout.animate().alpha(0f).setDuration(500).withEndAction {
                binding.guideEndLayout.visibility = View.GONE
                binding.guideEndLayout.alpha = 1f // Restauramos por si acaso
                marcarGuiaCompletada()
                reproducirSonido(R.raw.sonido_completar_guia)
                Toast.makeText(this, "¡Aventura iniciada!", Toast.LENGTH_SHORT).show()
            }.start()
        }

        // 4. Botón "Siguiente" de Coleccionables (Pantalla 4 -> Pantalla 5)
        val btnNextColeccionables =
            binding.guideColeccionablesLayout.findViewById<Button>(R.id.btn_next_coleccionables)
        btnNextColeccionables?.setOnClickListener {
            binding.guideColeccionablesLayout.visibility = View.GONE
            binding.guideInfoLayout.visibility = View.VISIBLE
            reproducirSonido(R.raw.sonido_avanzar)
            // Animación bocadillo 5 (Balanceo)
            val bocadilloInfo = binding.guideInfoLayout.findViewById<View>(R.id.tv_bocadillo_info)
            if (bocadilloInfo != null) {
                val animacionBalanceo = android.view.animation.RotateAnimation(
                    -3f,
                    3f,
                    android.view.animation.Animation.RELATIVE_TO_SELF,
                    0.5f,
                    android.view.animation.Animation.RELATIVE_TO_SELF,
                    0.0f
                )
                animacionBalanceo.duration = 400
                animacionBalanceo.repeatMode = android.view.animation.Animation.REVERSE
                animacionBalanceo.repeatCount = android.view.animation.Animation.INFINITE
                bocadilloInfo.startAnimation(animacionBalanceo)
            }
        }

        // 5. Botón "Siguiente" de Info (Pantalla 5 -> Pantalla 6)
        val btnNextInfo = binding.guideInfoLayout.findViewById<Button>(R.id.btn_next_info)
        btnNextInfo?.setOnClickListener {
            binding.guideInfoLayout.visibility = View.GONE
            binding.guideEndLayout.visibility = View.VISIBLE
        }

        // 6. Botón "¡A JUGAR!" de la Pantalla Final (Pantalla 6 -> Cierre)
        btnFinalizar = binding.guideEndLayout.findViewById<Button>(R.id.btn_finalizar)
        btnFinalizar?.setOnClickListener {
            binding.guideEndLayout.animate().alpha(0f).setDuration(500).withEndAction {
                binding.guideEndLayout.visibility = View.GONE
                binding.guideEndLayout.alpha = 1f
                marcarGuiaCompletada()
                Toast.makeText(this, "¡Aventura iniciada!", Toast.LENGTH_SHORT).show()
            }.start()
        }
        // ========================================================
        // --- BOTONES DE OMITIR (APARTADO B) ---
        // ========================================================
        // Usamos binding.root para buscar el botón en TODA la pantalla sin fallos
        binding.root.findViewById<Button>(R.id.btn_omitir_1)?.setOnClickListener { omitirGuia() }
        binding.root.findViewById<Button>(R.id.btn_omitir_2)?.setOnClickListener { omitirGuia() }
        binding.root.findViewById<Button>(R.id.btn_omitir_3)?.setOnClickListener { omitirGuia() }
        binding.root.findViewById<Button>(R.id.btn_omitir_4)?.setOnClickListener { omitirGuia() }
    }

    // Función que guarda en la memoria del móvil que ya hemos visto la guía
    private fun marcarGuiaCompletada() {
        val sharedPref = getSharedPreferences("SpyroAppPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("guia_vista", true)
        editor.apply() // Guarda los cambios
    }

    private fun selectedBottomMenu(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_characters -> navController?.navigate(R.id.navigation_characters)

            R.id.nav_worlds -> navController?.navigate(R.id.navigation_worlds)

            else -> navController?.navigate(R.id.navigation_collectibles)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_info) {
            showInfoDialog()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showInfoDialog() {
        AlertDialog.Builder(this).setTitle(R.string.title_about).setMessage(R.string.text_about)
            .setPositiveButton(R.string.accept, null).show()
    }

    // Función para saltarse la guía desde cualquier pantalla
    private fun omitirGuia() {
        binding.guideLayout.visibility = View.GONE
        binding.guidePersonajesLayout.visibility = View.GONE
        binding.guideMundosLayout.visibility = View.GONE
        binding.guideColeccionablesLayout.visibility = View.GONE
        binding.guideInfoLayout.visibility = View.GONE
        binding.guideEndLayout.visibility = View.GONE
        marcarGuiaCompletada()
        Toast.makeText(this, "Guía omitida", Toast.LENGTH_SHORT).show()
    }

    // FUNCIÓN PARA REPRODUCIR SONIDOS (APARTADO D)

    private fun reproducirSonido(sonidoId: Int) {
        // 1. Si ya había un sonido sonando de antes, lo paramos y lo borramos
        mediaPlayer?.release()

        // 2. Creamos el nuevo sonido y lo guardamos en nuestra variable global
        mediaPlayer = android.media.MediaPlayer.create(this, sonidoId)
        mediaPlayer?.start()

        // 3. Cuando termine de sonar, liberamos la memoria correctamente
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }
}