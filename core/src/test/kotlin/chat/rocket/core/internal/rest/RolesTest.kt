package chat.rocket.core.internal.rest

import chat.rocket.common.model.RoomType
import chat.rocket.common.model.Token
import chat.rocket.common.util.PlatformLogger
import chat.rocket.core.RocketChatClient
import chat.rocket.core.TokenRepository
import chat.rocket.core.createRocketChatClient
import io.fabric8.mockwebserver.DefaultMockServer
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.hamcrest.CoreMatchers.`is` as isEqualTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RolesTest {
    private val ROLES_OK = """
    {
        "roles": [
            {
                "rid": "BaE62jfDLXK3Xo6BA",
                "u": {
                    "_id": "XLH8FHfZfrTodM7k9",
                    "username": "matheus.cardoso",
                    "name": null
                },
                "roles": [
                    "owner"
                ],
                "_id": "GA5msx4eyYPGxn3cT"
            },
            {
                "rid": "BaE62jfDLXK3Xo6BA",
                "u": {
                    "_id": "BkNkw3iKgNyhMbPyW",
                    "username": "ronnie.dio",
                    "name": "Ronnie James Dio"
                },
                "roles": [
                    "moderator"
                ],
                "_id": "ehPuGyZBedznJsQHp"
            }
        ],
        "success": true
    }
    """.trimIndent()

    private lateinit var mockServer: DefaultMockServer

    private lateinit var sut: RocketChatClient

    @Mock
    private lateinit var tokenProvider: TokenRepository

    private val authToken = Token("userId", "authToken")

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        mockServer = DefaultMockServer()
        mockServer.start()

        val client = OkHttpClient()
        sut = createRocketChatClient {
            httpClient = client
            restUrl = mockServer.url("/")
            userAgent = "Rocket.Chat.Kotlin.SDK"
            tokenRepository = this@RolesTest.tokenProvider
            platformLogger = PlatformLogger.NoOpLogger()
        }

        Mockito.`when`(tokenProvider.get(sut.url)).thenReturn(authToken)
    }
}
