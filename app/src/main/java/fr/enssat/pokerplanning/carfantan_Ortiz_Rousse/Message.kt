package fr.enssat.pokerplanning.carfantan_Ortiz_Rousse

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.net.InetAddress

sealed class Message constructor(val type: String) {
    companion object {
        private val gson = GsonBuilder().registerTypeAdapter(Message::class.java, MessageSerializer()).create()
        fun toJson(src: Message) = gson.toJson(src)
        fun fromJson(str: String) = gson.fromJson<Message>(str, Message::class.java)
    }
}
    class UnknownMessage : Message("unknown")

    class VotesMessage(val roomId: String, val liste: List<Votant>): Message("votes")

    class SimpleMessage(val msg: String): Message("simple")

    class RoomMessage(val name: String, val owner: String, val ip: InetAddress?, val id: String): Message("room")

    data class Votant(val name: String, val note: Int)


class MessageSerializer: JsonDeserializer<Message> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext    ): Message {
        val type = json.asJsonObject["type"].asString
        return when (type) {
            "room" -> context.deserialize<RoomMessage>(json, RoomMessage::class.java)
            "simple" -> context.deserialize<SimpleMessage>(json, SimpleMessage::class.java)
            "votes" -> context.deserialize<VotesMessage>(json, VotesMessage::class.java)
            else -> UnknownMessage()
        }
    }

}