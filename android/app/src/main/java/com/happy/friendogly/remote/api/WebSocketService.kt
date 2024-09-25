package com.happy.friendogly.remote.api

import com.happy.friendogly.application.di.Websocket
import com.happy.friendogly.local.di.TokenManager
import com.happy.friendogly.remote.model.request.ChatMessageRequest
import com.happy.friendogly.remote.model.response.ChatMessageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import javax.inject.Inject

class WebSocketService @Inject constructor(
    private val client: StompClient,
    @Websocket
    private val baseUrl: BaseUrl,
    private val tokenManager: TokenManager,
) {
    private lateinit var websocket: StompSessionWithKxSerialization

    suspend fun connect() {
        websocket = client.connect(baseUrl.url).withJsonConversions()
    }

    suspend fun disconnect() = websocket.disconnect()

    suspend fun publishInvite(chatRoomId: Long) {
        websocket.send(
            StompSendHeaders(
                destination = ApiClient.WebSocket.publishEnter(chatRoomId = chatRoomId),
                customHeaders = mapOf("Authorization" to tokenManager.accessToken.first()),
            ),
            null,
        )
    }

    suspend fun publishSend(
        chatRoomId: Long,
        content: String,
    ) {
        websocket.convertAndSend(
            StompSendHeaders(
                destination = ApiClient.WebSocket.publishMessage(chatRoomId),
                customHeaders = mapOf("Authorization" to tokenManager.accessToken.first()),
            ),
            ChatMessageRequest(content),
            ChatMessageRequest.serializer(),
        )
    }

    suspend fun publishLeave(chatRoomId: Long) {
        websocket.send(
            StompSendHeaders(
                destination = ApiClient.WebSocket.publishLeave(chatRoomId) + chatRoomId.toString(),
                customHeaders = mapOf("Authorization" to tokenManager.accessToken.first()),
            ),
            null,
        )
    }

    suspend fun subscribeMessage(chatRoomId: Long): Flow<ChatMessageResponse> {
        return websocket.subscribe(
            headers =
                StompSubscribeHeaders(
                    destination = ApiClient.WebSocket.subscribeChat(chatRoomId),
                    customHeaders = mapOf("Authorization" to tokenManager.accessToken.first()),
                ),
            ChatMessageResponse.serializer(),
        )
    }
}
