package dam.pmdm.spyrothedragon

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.sin

class RiptoMagicView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Pinceles para dibujar en el Canvas
    private val diamondPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeWidth = 10f }

    private val path = Path()
    private val innerPath = Path()

    private var animationProgress = 0f
    private var animator: ValueAnimator? = null

    fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1500 // 1.5 segundos por latido mágico
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                animationProgress = it.animatedValue as Float
                invalidate() // Obliga a redibujar el Canvas
            }
            start()
        }
    }

    fun stopAnimation() {
        animator?.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f

        // 1. Cambio de color dinámico (Del rojo oscuro al magenta y violeta eléctrico)
        val hue = (animationProgress * 60 + 280) % 360
        val mainColor = Color.HSVToColor(floatArrayOf(hue, 1f, 1f))

        // 2. Ondas luminosas que se expanden (Pulsaciones mágicas)
        val waveRadius1 = 50f + (animationProgress * 600f)
        wavePaint.color = mainColor
        wavePaint.alpha = (255 * (1f - animationProgress)).toInt()
        canvas.drawCircle(cx, cy, waveRadius1, wavePaint)

        val prog2 = (animationProgress + 0.5f) % 1f
        val waveRadius2 = 50f + (prog2 * 600f)
        wavePaint.alpha = (255 * (1f - prog2)).toInt()
        canvas.drawCircle(cx, cy, waveRadius2, wavePaint)

        // 3. Resplandor brillante detrás del diamante
        val glowRadius = 180f + (sin(animationProgress * Math.PI * 2).toFloat() * 40f)
        glowPaint.color = mainColor
        glowPaint.alpha = 90
        canvas.drawCircle(cx, cy, glowRadius, glowPaint)

        // 4. Dibujar el Diamante de Ripto
        diamondPaint.color = Color.WHITE
        path.reset()
        path.moveTo(cx, cy - 120f) // Punta superior
        path.lineTo(cx + 80f, cy)  // Punta derecha
        path.lineTo(cx, cy + 140f) // Punta inferior
        path.lineTo(cx - 80f, cy)  // Punta izquierda
        path.close()
        canvas.drawPath(path, diamondPaint)

        // Núcleo del diamante (Cambia de color)
        diamondPaint.color = mainColor
        diamondPaint.alpha = 200
        innerPath.reset()
        innerPath.moveTo(cx, cy - 90f)
        innerPath.lineTo(cx + 50f, cy)
        innerPath.lineTo(cx, cy + 110f)
        innerPath.lineTo(cx - 50f, cy)
        innerPath.close()
        canvas.drawPath(innerPath, diamondPaint)

        // 5. Destellos mágicos orbitando el cetro
        val numSparkles = 8
        glowPaint.alpha = 255
        for (i in 0 until numSparkles) {
            val angle = (i * (Math.PI * 2) / numSparkles) + (animationProgress * Math.PI * 2)
            val dist = 140f + (sin(animationProgress * Math.PI * 2 + i).toFloat() * 60f)
            val sx = cx + (cos(angle) * dist).toFloat()
            val sy = cy + (sin(angle) * dist).toFloat()
            canvas.drawCircle(sx, sy, 10f, glowPaint)
        }
    }
}