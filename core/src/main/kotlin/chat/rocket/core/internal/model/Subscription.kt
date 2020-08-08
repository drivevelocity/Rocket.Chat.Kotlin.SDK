package chat.rocket.core.internal.model

import chat.rocket.common.internal.ISO8601Date
import chat.rocket.common.model.BaseRoom
import chat.rocket.common.model.RoomType
import chat.rocket.common.model.SimpleUser
import chat.rocket.core.model.LastMessage
import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class Subscription(
    @Json(name = "rid") val roomId: String,
    @Json(name = "prid") val parentId: String?, // Not empty if it is a discussion
    @Json(name = "_id") override val id: String,
    @Json(name = "t") override val type: RoomType,
    @Json(name = "u") override val user: SimpleUser?,
    val name: String?,
    @Json(name = "fname") override val fullName: String?,
    @Json(name = "ro") override val readonly: Boolean? = false,
    @Json(name = "ts") @ISO8601Date val timestamp: Long?,
    @Json(name = "ls") @ISO8601Date val lastSeen: Long?,
    @Json(name = "_updatedAt") @ISO8601Date override val updatedAt: Long?,
    val roles: List<String>?,
    @Json(name = "default")
    val isDefault: Boolean?,
    @Json(name = "f")
    val isFavorite: Boolean?,
    val open: Boolean?,
    val alert: Boolean?,
    val archived: Boolean?,
    val unread: Long?,
    val userMentions: Long?,
    val groupMentions: Long?,
    val topic: String?,
    val description: String?,
    val announcement: String?,
    val lastMessage: LastMessage?,
    val broadcast: Boolean?,
    @JvmField val muted: List<String>? = null
) : BaseRoom {
    val lastModified: Long?
        get() = lastSeen
}
