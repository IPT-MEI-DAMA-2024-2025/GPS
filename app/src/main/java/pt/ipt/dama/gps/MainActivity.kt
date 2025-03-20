package pt.ipt.dama.gps

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), LocationListener {

    // adição de variáveis a usar no meu projeto
    // a opção 'lateinit' indica que a variável será instanciada mais tarde
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // procurar o botão, e interagir com o 'evento' CLICK
        findViewById<Button>(R.id.btGPS).setOnClickListener {
            getLocation()
        }
    }

    /**
     * avalia se se pode pedir a localização do GPS e se autorizado
     * requer a localização
     */
    private fun getLocation() {

        if ((ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        } else {
            readLocation()
        }
    }

    /**
     * obtém a localização (coordenadas GPS) do telemóvel
     */
    private fun readLocation() {
        // Instanciar a variável 'locationManager' definida no início da classe
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        /*  Inicia o GPS, que vai autilizar a posição de 5 em 5 segundos,
                se a nova localização estiver pelo menos a 5 metros da última
                localização  */
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,  // Atualizar a cada 5 segundos
                5f,   // Se mover pelo menos 5 metros
                this
            )
        }
    }

    /**
     * Ler os dados do GPS e atribuí-los à textview na interface
     */
    override fun onLocationChanged(location: Location) {
        tvGpsLocation = findViewById(R.id.txtGPS)
        tvGpsLocation.text = "${getString(R.string.latitude)}: ${location.latitude} ,\n${getString(R.string.longitude)}: ${location.longitude}"
    }

    /**
     * avaliar as condições de uso do GPS
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Uso do GPS permitido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Uso do GPS negado", Toast.LENGTH_LONG).show()
            }
        }
    }

}