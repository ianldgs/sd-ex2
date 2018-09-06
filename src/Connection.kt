import java.net.*
import java.io.*

class Connection internal constructor(private val socket: Socket) {
    fun send(txt: String) {
        val out: OutputStream
        try {
            out = socket.getOutputStream()
            out.write(txt.toByteArray())

        } catch (e: Exception) {
            println("Deu pau no OutputStream")
        }

    }

    fun receive(): String {
        val `in`: InputStream
        val bt: Int
        val btxt: ByteArray
        var txt = ""
        btxt = ByteArray(79)
        try {
            `in` = socket.getInputStream()
            bt = `in`.read(btxt)

            if (bt > 0) {
                txt = String(btxt)
                txt = txt.substring(0, bt)
            }
        } catch (e: Exception) {
            println("Excecao na leitura do InputStream: $e")
        }

        return txt
    }

}
