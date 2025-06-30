package mimi.study.mimiapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class InicioActivity : AppCompatActivity() {

    // Variables a nivel de clase
    private lateinit var planetImage: ImageView
    private lateinit var timerText: TextView
    private lateinit var startButton: Button
    private lateinit var minutesPicker: NumberPicker
    private lateinit var secondsPicker: NumberPicker

    private var timer: CountDownTimer? = null
    private var rotationAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio_mimi)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.currentItem = 1


        // Referencias a vistas
        planetImage = findViewById(R.id.nave_carga)
        timerText = findViewById(R.id.timer_text)
        startButton = findViewById(R.id.start_button)
        minutesPicker = findViewById(R.id.minutes_picker)
        secondsPicker = findViewById(R.id.seconds_picker)

        // Configurar pickers
        minutesPicker.minValue = 0
        minutesPicker.maxValue = 60
        secondsPicker.minValue = 0
        secondsPicker.maxValue = 59

        startButton.setOnClickListener {
            val minutos = minutesPicker.value
            val segundos = secondsPicker.value

            val duracionTotal = (minutos * 60 + segundos) * 1000L
            if (duracionTotal > 0) {
                iniciarTemporizadorYRotacion(duracionTotal)
            } else {
                Toast.makeText(this, "Selecciona una duración mayor a 0", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.inicio)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    // Función para iniciar temporizador y animación
    private fun iniciarTemporizadorYRotacion(duracion: Long) {
        // Cancelar animaciones previas
        timer?.cancel()
        rotationAnimator?.cancel()

        // Iniciar temporizador
        timer = object : CountDownTimer(duracion, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutos = millisUntilFinished / 1000 / 60
                val segundos = (millisUntilFinished / 1000) % 60
                timerText.text = String.format("%02d:%02d", minutos, segundos)
            }

            override fun onFinish() {
                timerText.text = "00:00"
                rotationAnimator?.cancel()
            }
        }.start()

        // Iniciar animación de rotación
        rotationAnimator = ObjectAnimator.ofFloat(planetImage, "rotation", 0f, 360f).apply {
            duration = duracion
            repeatCount = ObjectAnimator.INFINITE
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        rotationAnimator?.cancel()
    }
}
