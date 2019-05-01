package chat.rocket.core.internal

import chat.rocket.common.util.PlatformLogger
import chat.rocket.core.RocketChatClient
import chat.rocket.core.TokenRepository
import chat.rocket.core.model.Reactions
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.hamcrest.CoreMatchers.`is` as isEqualTo

const val REACTIONS_JSON_PAYLOAD = "{\"reactions\":{\":croissant:\":{\"usernames\":[\"test.user\",\"test.user2\"],\"names\":[\"Test User\",\"Test User 2\"]}, \":thumbsup:\":{\"usernames\":[\"test.user\",\"test.user2\"],\"names\":[\"Test User\",\"Test User 2\"]}}}"
//const val REACTIONS_JSON_PAYLOAD = "{\"reactions\":{\":croissant:\":{\"usernames\":[\"test.user\",\"test.user2\"],\"names\":[\"Test User\",\"Test User 2\"]}}}"

const val REACTIONS_EMPTY_JSON_PAYLOAD = "[]"

class ReactionsAdapterTest {
    lateinit var moshi: Moshi

    @Mock
    private lateinit var tokenProvider: TokenRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        // Just to initialize Moshi
        val client = OkHttpClient()
        val rocket = RocketChatClient.create {
            httpClient = client
            restUrl = "http://8.8.8.8"
            userAgent = "Rocket.Chat.Kotlin.SDK"
            tokenRepository = tokenProvider
            platformLogger = PlatformLogger.NoOpLogger()
        }

        moshi = rocket.moshi
    }

    @Test
    fun `should deserialize JSON with reactions`() {
        val adapter = moshi.adapter<Reactions>(Reactions::class.java)
        adapter.fromJson(REACTIONS_JSON_PAYLOAD)?.let { reactions ->
            assertThat(reactions.size, isEqualTo(2))
            assertThat(reactions[":croissant:"]?.first?.size, isEqualTo(2))
            assertThat(reactions[":croissant:"]?.second?.size, isEqualTo(2))
            assertThat(reactions[":croissant:"]?.first?.get(0), isEqualTo("test.user"))
            assertThat(reactions[":croissant:"]?.second?.get(0), isEqualTo("Test User"))
        }
    }

    @Test
    fun `should deserialize empty reactions JSON`() {
        val adapter = moshi.adapter<Reactions>(Reactions::class.java)
        adapter.fromJson(REACTIONS_EMPTY_JSON_PAYLOAD)?.let { reactions ->
            assertThat(reactions.size, isEqualTo(0))
        }
    }

    @Test
    fun `should serialize back to JSON string`() {
        val adapter = moshi.adapter<Reactions>(Reactions::class.java)
        val reactionsFromJson = adapter.fromJson(REACTIONS_JSON_PAYLOAD)
        val reactionsToJson = adapter.toJson(reactionsFromJson)
        val reactions = adapter.fromJson(reactionsToJson)
        assertThat(reactions, isEqualTo(reactionsFromJson))
    }
}