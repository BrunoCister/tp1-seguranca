package br.edu.infnet.tp1seguranca

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), LocationListener {

    val FINE_REQUEST = 2444

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnLocal = findViewById<Button>(R.id.btnLocal)
        btnLocal.setOnClickListener {

            buscarLocalizacaoGPS()

        }

        val btnGravar = findViewById<Button>(R.id.btnGravar)
        btnGravar.setOnClickListener {

            val arquivo = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "nomeArquivo.txt")
            val fos = FileOutputStream(arquivo)
            val lblLat = findViewById<TextView>(R.id.lblLat)
            val lblLong = findViewById<TextView>(R.id.lblLong)
            val texto = lblLat.text.toString() + " " + lblLong.text.toString()
            fos.write(texto.toByteArray())
            fos.close()

        }

        val btnLer = findViewById<Button>(R.id.btnLer)
        btnLer.setOnClickListener {

            val arquivo = File(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "nomeArquivo.txt")
            val fis = FileInputStream(arquivo)
            val bytes = fis.readBytes()
            fis.close()
            val lblLer = findViewById<TextView>(R.id.lblLer)
            lblLer.setText(String(bytes))

        }
    }

    override fun onLocationChanged(location: Location) {
    }

    override fun onRequestPermissionsResult(

        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray

    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            buscarLocalizacaoGPS()

        } else {

            Toast.makeText(this, "Permissão negada.", Toast.LENGTH_LONG).show()

        }

    }

    private fun buscarLocalizacaoGPS() {

        val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val isGPSLigado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGPSLigado) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 0f, this)
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val lblLat = findViewById<TextView>(R.id.lblLat)
                lblLat.setText(location!!.latitude.toString())
                val lblLong = findViewById<TextView>(R.id.lblLong)
                lblLong.setText(location!!.longitude.toString())

            } else {

                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_REQUEST)

            }

        }

    }

}