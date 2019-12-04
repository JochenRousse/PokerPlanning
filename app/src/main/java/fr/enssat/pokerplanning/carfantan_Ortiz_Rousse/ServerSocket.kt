package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.IOException
import java.net.*
import java.net.ServerSocket
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class ServerSocket(val context: Context, val listener: (String) -> Unit) {

    companion object {
        val PORT = 6791
    }

    private lateinit var serverSocket: ServerSocket
    private val IPADDRESS = NetworkUtils.getIpAddress(context)
    private val MAX_CLIENT_BACK_LOG = 50
    private var loop = true

    //liste des clients connectés au serveur
    private val allClientsConnected = mutableListOf<Reply>()

    fun startListening() {
        try {
            val socketAddress: SocketAddress = InetSocketAddress(IPADDRESS, PORT)
            serverSocket = ServerSocket()
            serverSocket.setReuseAddress(true)
            serverSocket.bind(socketAddress, MAX_CLIENT_BACK_LOG)

            // écoute toutes les nouvelles demandes de connections clientes
            // et crée une socket locale au serveur, reply dédiée a ce nouveau client.
            Executors.newSingleThreadExecutor().execute {
                try {
                    while (loop) {
                        val newSocket = serverSocket.accept()
                        allClientsConnected.add(Reply(newSocket, listener))
                    }
                } catch (e: SocketException) {
                    Log.d("TAG", "Server stopped")
                }
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stopListening() {
        loop = false
        allClientsConnected.forEach { it.stop() }
        serverSocket.close()
    }

    fun stopVote(message: String, code: Int) {
        allClientsConnected.forEach {
            it.sendStopMessage(message, code)
        }
    }


    class Reply(val socket: Socket, val listener: (String) -> Unit) {
        private val TAG = this.javaClass.simpleName
        private var loop = true
        private val address = socket.inetAddress.address.toString()

        private val mainThread = object : Executor {
            val handler = Handler(Looper.getMainLooper())
            override fun execute(command: Runnable) {
                handler.post(command)
            }
        }

        init {
            Executors.newSingleThreadExecutor().execute {
                try {
                    while (loop) {
                        val buffer = ByteArray(2048)
                        val len = socket.getInputStream().read(buffer)
                        if (len > -1) {
                            val msg = String(buffer, 0, len, StandardCharsets.UTF_8)

                            //affiche le message reçu dans l'ui
                            mainThread.execute { listener(msg) }

                            val messageType = "1".toByteArray(StandardCharsets.UTF_8)
                            socket.getOutputStream().write(messageType)
                            socket.getOutputStream().flush()

                            //répond avec message
                            val data =
                                Message.toJson(SimpleMessage("Vote received, wait for results."))
                                    .toByteArray(StandardCharsets.UTF_8)
                            socket.getOutputStream().write(data)
                            socket.getOutputStream().flush()
                        }

                    }
                } catch (e: IOException) {
                    Log.d(TAG, "Client $address down")
                }
            }
        }

        fun stop() {
            loop = false
            socket.close()
        }

        fun sendStopMessage(message: String, code: Int) {
            Executors.newSingleThreadExecutor().execute {
                try {
                    val messageType = if(code == 1) "1".toByteArray(StandardCharsets.UTF_8) else if(code == 2) "2".toByteArray(StandardCharsets.UTF_8) else "3".toByteArray(StandardCharsets.UTF_8)
                    socket.getOutputStream().write(messageType)
                    socket.getOutputStream().flush()

                    //répond avec message
                    val data = message.toByteArray(StandardCharsets.UTF_8)
                    socket.getOutputStream().write(data)
                    socket.getOutputStream().flush()
                } catch (e: IOException) {
                    Log.d(TAG, "Client $address down")
                }
            }
        }
    }
}
