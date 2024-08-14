package com.happy.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.happy.friendogly.notification.controller.DeviceTokenController;
import com.happy.friendogly.notification.dto.request.UpdateDeviceTokenRequest;
import com.happy.friendogly.notification.dto.response.UpdateDeviceTokenResponse;
import com.happy.friendogly.notification.service.DeviceTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;

public class DeviceTokenApiDocsTest extends RestDocsTest{

    @Mock
    private DeviceTokenService deviceTokenService;

    @DisplayName("디바이스 토큰 저장")
    @Test
    void save() throws Exception {
        UpdateDeviceTokenRequest request = new UpdateDeviceTokenRequest("deviceToken");
        UpdateDeviceTokenResponse response = new UpdateDeviceTokenResponse("deviceToken");

        given(deviceTokenService.update(any(),any()))
                .willReturn(response);

        mockMvc
                .perform(patch("/device-tokens")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, getMemberToken()))
                .andDo(print())
                .andDo(document("device-tokens",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("DeviceToken API")
                                .summary("디바이스 토큰 저장 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .requestFields(
                                        fieldWithPath("deviceToken").description("디바이스 토큰")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.deviceToken").description("저장된 디바이스토큰")
                                )
                                .requestSchema(Schema.schema("UpdateDeviceTokenRequest"))
                                .responseSchema(Schema.schema("UpdateDeviceTokenResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @Override
    protected Object controller() {
        return new DeviceTokenController(deviceTokenService);
    }
}
