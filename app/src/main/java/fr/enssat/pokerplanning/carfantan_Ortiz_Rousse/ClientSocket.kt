package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class ClientSocket(
    val context: Context,
    val messageListener: (String) -> Unit,
    val resultListener: (String) -> Unit
) {
    private val TAG = this.javaClass.simpleName

    private lateinit var socket: Socket
    private var loop = true

    private val mainThread = object : Executor {
        val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }

    fun stop() {
        loop = false
        socket.close()
    }

    fun connect(serverIp: String, serverPort: Int?) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val address = InetAddress.getByName(serverIp)
                val port = serverPort ?: 6791
                Log.d(TAG, "try connecting to $address:$port")
                socket = Socket(address, port)
                receive()
            } catch (ex: Exception) {
                val msg = ex.message ?: "unable to connect server"
                Log.d(TAG, msg)
            }
        }
    }

    fun sendAndReceive(msg: String) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val data = msg.toByteArray(StandardCharsets.UTF_8)
                socket.getOutputStream().write(data)
                socket.getOutputStream().flush()
            } catch (e: IOException) {
                val msg = e.message ?: "unable to send to or receive from server"
                Log.d(TAG, msg)
                stop()
            }
        }
    }

    fun receive() {
        Executors.newSingleThreadExecutor().execute {
            try {
                while (loop) {
                    val buffer1 = ByteArray(1)
                    val len1 = socket.getInputStream().read(buffer1)
                    val messageType = String(buffer1, 0, len1, StandardCharsets.UTF_8)

                    val buffer = ByteArray(2048)
                    val len = socket.getInputStream().read(buffer)
                    val str = String(buffer, 0, len, StandardCharsets.UTF_8)

                    if (messageType == "1") {
                        //affiche le message reçu dans l'ui
                        mainThread.execute { messageListener(str) }
                    } else if (messageType == "2") {
                        //fin du vote
                        mainThread.execute { resultListener(str) }
                    }
                }
            } catch (e: IOException) {
                val msg = e.message ?: "unable to send to or receive from server"
                Log.d(TAG, msg)
                stop()
            }
        }
    }
}
