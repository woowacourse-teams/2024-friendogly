package com.happy.friendogly.remote.api

import com.happy.friendogly.local.di.LocalModule
import com.happy.friendogly.remote.model.request.ChatMessageRequest
import com.happy.friendogly.remote.model.response.ChatMessageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders


class WebSocketService(
    private val client: StompClient,
    private val baseUrl: BaseUrl,
    private val localModule: LocalModule
) {

    private suspend fun stompSession(): StompSessionWithKxSerialization =
        client.connect(baseUrl.url).withJsonConversions()

    suspend fun publishInvite(chatRoomId: Long) {
        stompSession().send(
            StompSendHeaders(
                destination = ApiClient.WebSocket.publishEnter(chatRoomId = chatRoomId),
                customHeaders = mapOf("Authorization" to localModule.accessToken.first())
            ),
            null
        )
    }

    suspend fun publishSend(chatRoomId: Long, content: String) {
        stompSession().convertAndSend(
            StompSendHeaders(
                destination = ApiClient.WebSocket.publishMessage(chatRoomId),
                customHeaders = mapOf("Authorization" to localModule.accessToken.first())
            ),
            ChatMessageRequest(content),
            ChatMessageRequest.serializer()
        )
    }

    suspend fun publishLeave(chatRoomId: Long) {
        stompSession().send(
            StompSendHeaders(
                destination = ApiClient.WebSocket.publishLeave(chatRoomId) + chatRoomId.toString(),
                customHeaders = mapOf("Authorization" to localModule.accessToken.first())
            ),
            null
        )
    }

    suspend fun subscribeMessage(chatRoomId: Long): Flow<ChatMessageResponse> {
        return stompSession().subscribe(
            headers = StompSubscribeHeaders(
                destination = ApiClient.WebSocket.subscribeChat(chatRoomId),
                customHeaders = mapOf("Authorization" to localModule.accessToken.first())
            ),
            ChatMessageResponse.serializer()
        )
    }

}
