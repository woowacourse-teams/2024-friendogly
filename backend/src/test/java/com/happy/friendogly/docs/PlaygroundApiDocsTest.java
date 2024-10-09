package com.happy.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.playground.controller.PlaygroundController;
import com.happy.friendogly.playground.domain.Location;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.playground.dto.request.UpdatePlaygroundArrivalRequest;
import com.happy.friendogly.playground.dto.response.FindPlaygroundDetailResponse;
import com.happy.friendogly.playground.dto.response.FindPlaygroundLocationResponse;
import com.happy.friendogly.playground.dto.response.SaveJoinPlaygroundMemberResponse;
import com.happy.friendogly.playground.dto.response.SavePlaygroundResponse;
import com.happy.friendogly.playground.dto.response.detail.PlaygroundPetDetail;
import com.happy.friendogly.playground.service.PlaygroundCommandService;
import com.happy.friendogly.playground.service.PlaygroundQueryService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;

public class PlaygroundApiDocsTest extends RestDocsTest {

    @Mock
    private PlaygroundQueryService playgroundQueryService;

    @Mock
    private PlaygroundCommandService playgroundCommandService;

    @DisplayName("놀이터를 저장")
    @Test
    void save() throws Exception {
        SavePlaygroundRequest request = new SavePlaygroundRequest(37.5173316, 127.1011661);

        SavePlaygroundResponse response = new SavePlaygroundResponse(
                1L,
                request.latitude(),
                request.longitude()
        );

        when(playgroundCommandService.save(any(), anyLong()))
                .thenReturn(response);

        mockMvc
                .perform(post("/playgrounds")
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("playgrounds/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Playground API")
                                .summary("놀이터 저장 API")
                                .requestFields(
                                        fieldWithPath("latitude").description("놀이터의 위도"),
                                        fieldWithPath("longitude").description("놀이터의 경도")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.id").description("놀이터의 ID"),
                                        fieldWithPath("data.latitude").description("놀이터의 위도"),
                                        fieldWithPath("data.longitude").description("놀이터의 경도")
                                )
                                .responseSchema(Schema.schema("SavePlaygroundResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isCreated());
    }

    @DisplayName("놀이터도착 유무 업데이트")
    @Test
    void updateArrival() throws Exception {
        UpdatePlaygroundArrivalRequest request = new UpdatePlaygroundArrivalRequest(37.5173316, 127.1011661);
        mockMvc
                .perform(patch("/playgrounds/arrival")
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("playgrounds/arrival",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Playground API")
                                .summary("놀이터 도착 유무 API")
                                .requestFields(
                                        fieldWithPath("latitude").description("멤버 위도"),
                                        fieldWithPath("longitude").description("멤버 경도")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.isArrived").description("도착 유무")
                                )
                                .responseSchema(Schema.schema("PlaygroundArrivalResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("놀이터의 정보를 조회")
    @Test
    void find() throws Exception {

        Pet dummyPet1 = new Pet(
                new Member("김도선", "tag1", "imgaeUrl"),
                "초코",
                "뛰어놀기 좋아해요",
                LocalDate.of(2024, 9, 27),
                SizeType.LARGE,
                Gender.FEMALE,
                "https://i.pinimg.com/564x/d1/62/cb/d162cb12dfa0011a7bd67188a14d661c.jpg"
        );
        FindPlaygroundDetailResponse response = new FindPlaygroundDetailResponse(
                1L,
                3,
                0,
                false,
                List.of(
                        new PlaygroundPetDetail(
                                1L,
                                dummyPet1,
                                null,
                                true,
                                false
                        )
                )
        );

        when(playgroundQueryService.findDetail(anyLong(), anyLong()))
                .thenReturn(response);

        mockMvc
                .perform(get("/playgrounds/{id}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken()))
                .andDo(document("playgrounds/find",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Playground API")
                                .summary("놀이터 상세 조회 API")
                                .pathParameters(
                                        parameterWithName("id").description("조회할 놀이터의 ID")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.id").description("놀이터의 ID"),
                                        fieldWithPath("data.totalPetCount").description("놀이터에 참여한 전체 강아지 수"),
                                        fieldWithPath("data.arrivedPetCount").description("놀이터에 도착한 강아지 수"),
                                        fieldWithPath("data.isParticipating").description("놀이터에 참여했는지 여부"),
                                        fieldWithPath("data.playgroundPetDetails.[].memberId")
                                                .description("강아지 주인 멤버ID"),
                                        fieldWithPath("data.playgroundPetDetails.[].petId")
                                                .description("강아지 ID"),
                                        fieldWithPath("data.playgroundPetDetails.[].name")
                                                .description("강아지 이름"),
                                        fieldWithPath("data.playgroundPetDetails.[].birthDate")
                                                .description("강아지 생년월"),
                                        fieldWithPath("data.playgroundPetDetails.[].sizeType")
                                                .description("강아지 사이즈"),
                                        fieldWithPath("data.playgroundPetDetails.[].gender")
                                                .description("강아지 성별"),
                                        fieldWithPath("data.playgroundPetDetails.[].imageUrl")
                                                .description("강아지 imageUrl"),
                                        fieldWithPath("data.playgroundPetDetails.[].message").optional()
                                                .description("강아지 상태메세지"),
                                        fieldWithPath("data.playgroundPetDetails.[].isArrival")
                                                .description("강아지 도착 유무"),
                                        fieldWithPath("data.playgroundPetDetails.[].isMine")
                                                .description("강아지의 주인인지 여부")
                                )
                                .responseSchema(Schema.schema("PlaygroundDetailResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("모든 놀이터의 위치 정보를 조회")
    @Test
    void findAllLocation() throws Exception {

        List<FindPlaygroundLocationResponse> response = List.of(
                new FindPlaygroundLocationResponse(1L, 37.5173316, 127.1011661, true),
                new FindPlaygroundLocationResponse(2L, 37.5185122, 127.098778, false),
                new FindPlaygroundLocationResponse(3L, 37.5136533, 127.0983182, false),
                new FindPlaygroundLocationResponse(4L, 37.5131474, 127.1042528, false)
        );

        when(playgroundQueryService.findLocations(anyLong()))
                .thenReturn(response);

        mockMvc
                .perform(get("/playgrounds/locations")
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken())
                )
                .andDo(document("playgrounds/findLocations",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Playground API")
                                .summary("전체 놀이터 위치 조회 API")
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.[].id").description("놀이터의 ID"),
                                        fieldWithPath("data.[].latitude").description("놀이터의 위도"),
                                        fieldWithPath("data.[].longitude").description("놀이터의 경도"),
                                        fieldWithPath("data.[].isParticipating").description("놀이터 참여 유무")
                                )
                                .responseSchema(Schema.schema("PlaygroundLocationResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("놀이터의 참여 현황 요약 정보를 조회")
    @Test
    void findSummary() throws Exception {
        mockMvc
                .perform(get("/playgrounds/{id}/summary", 1L))
                .andDo(document("playgrounds/summary",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Playground API")
                                .summary("놀이터 참여 현황 요약 조회 API")
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.id").description("놀이터의 ID"),
                                        fieldWithPath("data.totalPetCount").description("전체 참여 강아지 수"),
                                        fieldWithPath("data.arrivedPetCount").description("현재 홯성화 강아지 수")
                                )
                                .responseSchema(Schema.schema("PlaygroundSummaryResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("놀이터 참여합니다.")
    @Test
    void joinPlayground() throws Exception {

        PlaygroundMember playgroundMember = new PlaygroundMember(
                new Playground(new Location(37.5173316, 127.1011661)),
                new Member("name", "tag", "imageUrl")
        );
        SaveJoinPlaygroundMemberResponse response = SaveJoinPlaygroundMemberResponse.from(playgroundMember);

        when(playgroundCommandService.joinPlayground(anyLong(), anyLong()))
                .thenReturn(response);

        mockMvc
                .perform(post("/playgrounds/{playgroundId}/join", 1)
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken()))
                .andExpect(status().isOk())
                .andDo(document("playgrounds/saveJoinPlaygroundMember",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Playground API")
                                .summary("놀이터 참여 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.playgroundId").description("놀이터의 ID"),
                                        fieldWithPath("data.memberId").description("멤버의 ID"),
                                        fieldWithPath("data.message").description("멤버의 상태 메세지"),
                                        fieldWithPath("data.isArrived").description("멤버의 놀이터 도착 유무"),
                                        fieldWithPath("data.exitTime").description("멤버의 놀이터 나간 시간")
                                )
                                .responseSchema(Schema.schema("SavePlaygroundResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("놀이터를 나간다")
    @Test
    void leavePlayground() throws Exception {
        doNothing().when(playgroundCommandService).leavePlayground(anyLong());

        mockMvc
                .perform(delete("/playgrounds/leave")
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken()))
                .andExpect(status().isNoContent())
                .andDo(document("playgrounds/leavePlayground",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Playground API")
                                .summary("놀이터 나가기 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .build()
                        )
                ))
                .andExpect(status().isNoContent());
    }

    @Override
    protected Object controller() {
        return new PlaygroundController(playgroundCommandService, playgroundQueryService);
    }
}
