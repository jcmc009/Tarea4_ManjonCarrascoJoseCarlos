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

        binding.guideLayout.visibility = View.VISIBLE

        // 2. Buscamos el botón "Comenzar" dentro de la vista inflada
        val btnComenzar = binding.root.findViewById<Button>(R.id.btn_comenzar)

        // 3. Le damos la acción de clic al botón
        btnComenzar?.setOnClickListener {

            binding.guideLayout.animate().alpha(0f).setDuration(400).withEndAction {
                binding.guideLayout.visibility = View.GONE

                // AHORA MOSTRAMOS LA PANTALLA 2
                binding.guidePersonajesLayout.visibility = View.VISIBLE

                // Animación del bocadillo
                val bocadillo =
                    binding.guidePersonajesLayout.findViewById<View>(R.id.tv_bocadillo)
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
            binding.guideInfoLayout.animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction {
                    binding.guideInfoLayout.visibility = View.GONE

                    // Restauramos el alpha por buenas prácticas
                    binding.guideInfoLayout.alpha = 1f

                    // Mensaje de fin de tutorial
                    Toast.makeText(
                        this,
                        "¡Estás listo para explorar el mundo de Spyro!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .start()
        }

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
}
