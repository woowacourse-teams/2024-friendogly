package com.happy.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.happy.friendogly.club.controller.ClubController;
import com.happy.friendogly.club.domain.FilterCondition;
import com.happy.friendogly.club.domain.Status;
import com.happy.friendogly.club.dto.request.FindClubByFilterRequest;
import com.happy.friendogly.club.dto.request.SaveClubMemberRequest;
import com.happy.friendogly.club.dto.request.SaveClubRequest;
import com.happy.friendogly.club.dto.request.UpdateClubRequest;
import com.happy.friendogly.club.dto.response.AddressDetailResponse;
import com.happy.friendogly.club.dto.response.ClubMemberDetailResponse;
import com.happy.friendogly.club.dto.response.ClubPetDetailResponse;
import com.happy.friendogly.club.dto.response.FindClubByFilterResponse;
import com.happy.friendogly.club.dto.response.FindClubResponse;
import com.happy.friendogly.club.dto.response.SaveClubMemberResponse;
import com.happy.friendogly.club.dto.response.SaveClubResponse;
import com.happy.friendogly.club.dto.response.UpdateClubResponse;
import com.happy.friendogly.club.service.ClubCommandService;
import com.happy.friendogly.club.service.ClubQueryService;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.SizeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.multipart.MultipartFile;


public class ClubApiDocsTest extends RestDocsTest {

    @Mock
    private ClubCommandService clubCommandService;

    @Mock
    private ClubQueryService clubQueryService;

    @DisplayName("필터링 조건을 통해 모임 리스트를 조회한다.")
    @Test
    void findSearching_200() throws Exception {
        FindClubByFilterRequest request = new FindClubByFilterRequest(
                FilterCondition.ALL.name(),
                "서울특별시",
                "송파구",
                "신천동",
                Set.of(Gender.FEMALE.name(), Gender.FEMALE_NEUTERED.name()),
                Set.of(SizeType.SMALL.name())
        );

        List<FindClubByFilterResponse> responses = List.of(new FindClubByFilterResponse(
                        1L,
                        "모임 제목1",
                        "모임 본문 내용1",
                        "브라운",
                        new AddressDetailResponse("서울특별시", "송파구", "신천동"),
                        Status.OPEN,
                        LocalDateTime.now(),
                        Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                        Set.of(SizeType.SMALL),
                        4,
                        1,
                        "https:/clubImage1.com",
                        List.of("https://petImage1.com")
                ),
                new FindClubByFilterResponse(
                        2L,
                        "모임 제목2",
                        "모임 본문 내용2",
                        "레아",
                        new AddressDetailResponse("서울특별시", "송파구", "신천동"),
                        Status.OPEN,
                        LocalDateTime.now(),
                        Set.of(Gender.MALE, Gender.FEMALE_NEUTERED, Gender.MALE_NEUTERED),
                        Set.of(SizeType.MEDIUM),
                        3,
                        1,
                        "https:/clubImage2.com",
                        List.of("https://petImage1.com")
                )
        );

        when(clubQueryService.findFindByFilter(anyLong(), any()))
                .thenReturn(responses);

        mockMvc.perform(get("/clubs/searching")
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken())
                        .param("filterCondition", request.filterCondition())
                        .param("province", request.province())
                        .param("city", request.city())
                        .param("village", request.village())
                        .param("genderParams", request.genderParams().toArray(String[]::new))
                        .param("sizeParams", request.sizeParams().toArray(String[]::new)))
                .andExpect(status().isOk())
                .andDo(document("clubs/findSearching/200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 검색 조회 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .queryParameters(
                                        parameterWithName("filterCondition").description("모임의 필터 조건 (ALL, OPEN, ABLE_TO_JOIN)"),
                                        parameterWithName("province").description("모임의 도/광역시/특별시 주소"),
                                        parameterWithName("city").description("모임의 시/군/구 주소"),
                                        parameterWithName("village").description("모임의 읍/면/동주소"),
                                        parameterWithName("genderParams").description("모임에 참여가능한 팻 성별"),
                                        parameterWithName("sizeParams").description("모임에 참여가능한 팻 크기"))
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("모임 식별자"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("모임 제목"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("모임 본문"),
                                        fieldWithPath("data.[].ownerMemberName").type(JsonFieldType.STRING).description("모임 방장 이름"),
                                        fieldWithPath("data.[].address.province").type(JsonFieldType.STRING).description("모임의 도/광역시/특별시 주소"),
                                        fieldWithPath("data.[].address.city").type(JsonFieldType.STRING).description("모임의 시/군/구 주소"),
                                        fieldWithPath("data.[].address.village").type(JsonFieldType.STRING).description("모임의 읍/면/동 주소"),
                                        fieldWithPath("data.[].status").type(JsonFieldType.STRING).description("모임 상태(OPEN , CLOSED, FULL)"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("모임 생성 시간(LocalDateTime)"),
                                        fieldWithPath("data.[].allowedGender").type(JsonFieldType.ARRAY).description("허용되는 팻 성별(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)"),
                                        fieldWithPath("data.[].allowedSize").type(JsonFieldType.ARRAY).description("허용되는 팻 크기(SMALL,MEDIUM,LARGE)"),
                                        fieldWithPath("data.[].memberCapacity").type(JsonFieldType.NUMBER).description("모임 최대 인원"),
                                        fieldWithPath("data.[].currentMemberCount").type(JsonFieldType.NUMBER).description("모임 현재 인원"),
                                        fieldWithPath("data.[].imageUrl").type(JsonFieldType.STRING).description("모임 프로필 사진"),
                                        fieldWithPath("data.[].petImageUrls").type(JsonFieldType.ARRAY).description("모임 리스트에 나오는 팻 사진 url 리스트"))
                                .responseSchema(Schema.schema("FindSearchingClubResponse"))
                                .build()))
                );
    }

    @DisplayName("ID를 통해 모임의 상세 정보를 조회한다.")
    @Test
    void findById_200() throws Exception {
        FindClubResponse response = new FindClubResponse(
                1L,
                "모임 제목1",
                "모임 본문 내용1",
                "브라운",
                new AddressDetailResponse("서울특별시", "송파구", "신천동"),
                Status.OPEN,
                LocalDateTime.now(),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL),
                4,
                1,
                "https://clubImage1.com",
                "https://OwnerProfileImage.com",
                true,
                true,
                false,
                List.of(new ClubMemberDetailResponse(2L, "정지호", "https://땡이 영공때 사진.com"),
                        new ClubMemberDetailResponse(3L, "쪙찌효", "https://땡이 근공때 사진.com")),
                List.of(new ClubPetDetailResponse(1L, "땡이", "https://땡이의 귀여운 이미지.com", true),
                        new ClubPetDetailResponse(2L, "땡이 동생", "https://땡이 동생의 귀여운 이미지.com", true))
        );

        when(clubQueryService.findById(anyLong(), anyLong()))
                .thenReturn(response);

        mockMvc.perform(get("/clubs/{id}",1L)
                .header(HttpHeaders.AUTHORIZATION, getMemberToken()))
                .andExpect(status().isOk())
                .andDo(document("clubs/findById/200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 상세 조회 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("모임 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("모임 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("모임 본문"),
                                        fieldWithPath("data.ownerMemberName").type(JsonFieldType.STRING).description("모임 방장 이름"),
                                        fieldWithPath("data.address.province").type(JsonFieldType.STRING).description("모임의 도/광역시/특별시 주소"),
                                        fieldWithPath("data.address.city").type(JsonFieldType.STRING).description("모임의 시/군/구 주소"),
                                        fieldWithPath("data.address.village").type(JsonFieldType.STRING).description("모임의 읍/면/동 주소"),
                                        fieldWithPath("data.status").type(JsonFieldType.STRING).description("모임 상태(OPEN , CLOSED, FULL)"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("모임 생성 시간(LocalDateTime)"),
                                        fieldWithPath("data.allowedGender").type(JsonFieldType.ARRAY).description("허용되는 팻 성별(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)"),
                                        fieldWithPath("data.allowedSize").type(JsonFieldType.ARRAY).description("허용되는 팻 크기(SMALL,MEDIUM,LARGE)"),
                                        fieldWithPath("data.memberCapacity").type(JsonFieldType.NUMBER).description("모임 최대 인원"),
                                        fieldWithPath("data.currentMemberCount").type(JsonFieldType.NUMBER).description("모임 현재 인원"),
                                        fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("모임 프로필 사진"),
                                        fieldWithPath("data.ownerImageUrl").type(JsonFieldType.STRING).description("모임 방장 프로필 사진"),
                                        fieldWithPath("data.isMine").type(JsonFieldType.BOOLEAN).description("현재 로그인한 사용자가 방장인지 여부"),
                                        fieldWithPath("data.alreadyParticipate").type(JsonFieldType.BOOLEAN).description("현재 로그인한 사용자가 모임에 참여 중인지 여부"),
                                        fieldWithPath("data.canParticipate").type(JsonFieldType.BOOLEAN).description("현재 로그인한 사용자가 모임에 참여 가능한지 여부"),
                                        fieldWithPath("data.memberDetails[].id").type(JsonFieldType.NUMBER).description("모임에 참여한 사용자 id"),
                                        fieldWithPath("data.memberDetails[].name").type(JsonFieldType.STRING).description("모임에 참여한 사용자 이름"),
                                        fieldWithPath("data.memberDetails[].imageUrl").type(JsonFieldType.STRING).description("모임에 참여한 사용자 이미지 URL"),
                                        fieldWithPath("data.petDetails[].id").type(JsonFieldType.NUMBER).description("모임에 참여한 반려견 id"),
                                        fieldWithPath("data.petDetails[].name").type(JsonFieldType.STRING).description("모임에 참여한 반려견 이름"),
                                        fieldWithPath("data.petDetails[].imageUrl").type(JsonFieldType.STRING).description("모임에 참여한 반려견 이미지 URL"),
                                        fieldWithPath("data.petDetails[].isMine").type(JsonFieldType.BOOLEAN).description("로그인한 사용자의 반려견인지 여부"))
                                .responseSchema(Schema.schema("FindClubResponse"))
                                .build()))
                );
    }

    @DisplayName("모임을 생성한다.")
    @Test
    void save_201() throws Exception {
        SaveClubRequest requestDto = new SaveClubRequest(
                "모임 제목",
                "모임 내용",
                "서울특별시",
                "송파구",
                "신천동",
                Set.of(Gender.FEMALE.name(), Gender.FEMALE_NEUTERED.name()),
                Set.of(SizeType.SMALL.name()),
                5,
                List.of(1L)
        );
        MockMultipartFile image = new MockMultipartFile("image", "image", MediaType.MULTIPART_FORM_DATA.toString(),
                "asdf".getBytes());
        MockMultipartFile request = new MockMultipartFile("request", "request", "application/json",
                objectMapper.writeValueAsBytes(requestDto));

        SaveClubResponse response = new SaveClubResponse(
                1L,
                "모임 제목",
                "모임 내용",
                "브라운",
                new AddressDetailResponse("서울특별시", "송파구", "신천동"),
                Status.OPEN,
                LocalDateTime.of(2024, 7, 23, 11, 5),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL),
                5,
                1,
                "https://clubImage.com",
                List.of("https://pet1ImageUrl.com", "http://pet2ImageUrl"),
                true,
                4L
        );

        when(clubCommandService.save(any(), any(MultipartFile.class), any(SaveClubRequest.class)))
                .thenReturn(response);

        mockMvc.perform(multipart("/clubs")
                        .file(image)
                        .file(request)
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken()))
                .andExpect(status().isCreated())
                .andDo(document("clubs/post/201",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("image").description("모임 이미지 파일"),
                                partWithName("request").description("모임 등록 정보")
                        ),
                        requestPartFields(
                                "request",
                                fieldWithPath("title").type(JsonFieldType.STRING).description("모임 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("모임 내용"),
                                fieldWithPath("province").type(JsonFieldType.STRING).description("모임의 도/광역시/특별시 주소"),
                                fieldWithPath("city").type(JsonFieldType.STRING).description("모임의 시/군/구 주소"),
                                fieldWithPath("village").type(JsonFieldType.STRING).description("모임의 읍/면/동 주소"),
                                fieldWithPath("allowedGenders").type(JsonFieldType.ARRAY).description("참여가능한 강아지 성별"),
                                fieldWithPath("allowedSizes").type(JsonFieldType.ARRAY).description("참여가능한 강아지 사이즈"),
                                fieldWithPath("memberCapacity").type(JsonFieldType.NUMBER).description("모임 최대 인원"),
                                fieldWithPath("participatingPetsId").type(JsonFieldType.ARRAY).description("모임에 참여하는 방장 강아지의 ID 리스트")
                        ),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 생성 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).description("생성된 모임 리소스 Location"))
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("모임 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("모임 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("모임 본문"),
                                        fieldWithPath("data.ownerMemberName").type(JsonFieldType.STRING).description("모임 방장 이름"),
                                        fieldWithPath("data.address.province").type(JsonFieldType.STRING).description("모임의 도/광역시/특별시 주소"),
                                        fieldWithPath("data.address.city").type(JsonFieldType.STRING).description("모임의 시/군/구 주소"),
                                        fieldWithPath("data.address.village").type(JsonFieldType.STRING).description("모임의 읍/면/동 주소"),
                                        fieldWithPath("data.status").type(JsonFieldType.STRING).description("모임 상태(OPEN , CLOSED)"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("모임 생성 시간(LocalDateTime)"),
                                        fieldWithPath("data.allowedSize").type(JsonFieldType.ARRAY).description("허용되는 팻 크기(SMALL,MEDIUM,LARGE)"),
                                        fieldWithPath("data.allowedGender").type(JsonFieldType.ARRAY).description("허용되는 팻 성별(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)"),
                                        fieldWithPath("data.memberCapacity").type(JsonFieldType.NUMBER).description("모임 최대 인원"),
                                        fieldWithPath("data.currentMemberCount").type(JsonFieldType.NUMBER).description("모임 현재 인원(대부분의 경우 1)"),
                                        fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("모임 프로필 사진"),
                                        fieldWithPath("data.petImageUrls").type(JsonFieldType.ARRAY).description("모임 리스트에 참여하는 팻 프로필 url"),
                                        fieldWithPath("data.isMine").type(JsonFieldType.BOOLEAN).description("현재 회원의 글인지 판단하는 값(대부분의 경우 true)"),
                                        fieldWithPath("data.chatRoomId").type(JsonFieldType.NUMBER).description("생성된 채팅방 ID"))
                                .requestSchema(Schema.schema("saveClubRequest"))
                                .responseSchema(Schema.schema("saveClubResponse"))
                                .build()))
                );
    }

    @DisplayName("모임에 참여한다.")
    @Test
    void saveClubMember_201() throws Exception {
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(1L));
        SaveClubMemberResponse response = new SaveClubMemberResponse(1L, 3L);
        when(clubCommandService.joinClub(any(), any(), any()))
                .thenReturn(response);

        mockMvc.perform(post("/clubs/{clubId}/members", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("clubs/members/201",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 참여 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("참여하는 모임의 ID"))
                                .requestFields(
                                        fieldWithPath("participatingPetsId").type(JsonFieldType.ARRAY).description("참여하는 팻 ID 리스트")
                                )
                                .responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).type(SimpleType.STRING).description("모임-회원 연관관계 리소스 Location")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("멤버 ID(추후 채팅 생기면 변경)"),
                                        fieldWithPath("data.chatRoomId").type(JsonFieldType.NUMBER).description("모임 채팅방 ID")
                                )
                                .responseSchema(Schema.schema("SaveClubMemberResponse"))
                                .build())
                ));
    }

    @DisplayName("모임 참여에 실패한 경우(이미 참여 중 또는 참여할 수 없는 모임 등)")
    @Test
    void saveClubMember_400() throws Exception {
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(1L));

        when(clubCommandService.joinClub(any(), any(), any()))
                .thenThrow(new FriendoglyException("예외 메세지"));

        mockMvc.perform(post("/clubs/{clubId}/members", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("clubs/members/400",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 참여 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("참여하는 모임의 ID"))
                                .requestFields(
                                        fieldWithPath("participatingPetsId").type(JsonFieldType.ARRAY).description("참여하는 팻 ID 리스트")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.errorCode").type(JsonFieldType.STRING).description("에러 코드"),
                                        fieldWithPath("data.errorMessage").type(JsonFieldType.STRING).description("에러메세지"),
                                        fieldWithPath("data.detail").type(JsonFieldType.ARRAY).description("에러 디테일")
                                )
                                .build())
                ));
    }

    @DisplayName("모임에서 빠지면 204를 반환한다.")
    @Test
    void deleteClubMember_204() throws Exception {

        doNothing()
                .when(clubCommandService)
                .deleteClubMember(any(), any());

        mockMvc.perform(delete("/clubs/{clubId}/members", 1L)
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken()))
                .andExpect(status().isNoContent())
                .andDo(document("clubs/{clubId}/members/400",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 탈퇴 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("탈퇴하는 모임의 ID")
                                )
                                .build())
                ));
    }

    @DisplayName("모임에서 탈퇴 중 예외가 발생한다. 400")
    @Test
    void deleteClubMember_400() throws Exception {

        doThrow(new FriendoglyException("예외 메세지"))
                .when(clubCommandService)
                .deleteClubMember(any(), any());

        mockMvc.perform(delete("/clubs/{clubId}/members", 1L)
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken()))
                .andExpect(status().isBadRequest())
                .andDo(document("clubs/{clubId}/members/400",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 탈퇴 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("탈퇴하는 모임의 ID")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.errorCode").type(JsonFieldType.STRING).description("에러 코드"),
                                        fieldWithPath("data.errorMessage").type(JsonFieldType.STRING).description("에러메세지"),
                                        fieldWithPath("data.detail").type(JsonFieldType.ARRAY).description("에러 디테일")
                                )
                                .build())
                ));
    }

    @DisplayName("모임을 수정한다.")
    @Test
    void update_200() throws Exception{
        UpdateClubRequest request = new UpdateClubRequest("모집완료입니다.!", "추후에 사람이 빠지면 다시 모집하겠습니다.", "CLOSED");
        UpdateClubResponse response = new UpdateClubResponse("모집완료입니다.!", "추후에 사람이 빠지면 다시 모집하겠습니다.", Status.CLOSED);

        when(clubCommandService.update(anyLong(),anyLong(),any()))
                .thenReturn(response);

        mockMvc.perform(patch("/clubs/{clubId}",1L)
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("club/update/200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 수정 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("탈퇴하는 모임의 ID")
                                )
                                .responseFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목 수정 내용"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용 수정 내용"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태 수정 내용(OPEN,CLOSED)")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("수정 후 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("수정 후 내용"),
                                        fieldWithPath("data.status").type(JsonFieldType.STRING).description("수정 후 상태")
                                )
                                .requestSchema(Schema.schema("UpdateClubRequest"))
                                .responseSchema(Schema.schema("UpdateClubResponse"))
                                .build())
                        ));
    }

    @DisplayName("수정 권한이 없는 모임을 수정한다.")
    @Test
    void update_403() throws Exception{
        UpdateClubRequest request = new UpdateClubRequest("모집완료입니다.!", "추후에 사람이 빠지면 다시 모집하겠습니다.", "CLOSED");
        UpdateClubResponse response = new UpdateClubResponse("모집완료입니다.!", "추후에 사람이 빠지면 다시 모집하겠습니다.", Status.CLOSED);

        when(clubCommandService.update(anyLong(),anyLong(),any()))
                .thenThrow(new FriendoglyException("예외 메세지", HttpStatus.FORBIDDEN));

        mockMvc.perform(patch("/clubs/{clubId}",1L)
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andDo(document("club/update/403",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 수정 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("탈퇴하는 모임의 ID")
                                )
                                .responseFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목 수정 내용"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용 수정 내용"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태 수정 내용(OPEN,CLOSED)")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.errorCode").type(JsonFieldType.STRING).description("에러 코드"),
                                        fieldWithPath("data.errorMessage").type(JsonFieldType.STRING).description("에러메세지"),
                                        fieldWithPath("data.detail").type(JsonFieldType.ARRAY).description("에러 디테일")
                                )
                                .requestSchema(Schema.schema("UpdateClubRequest"))
                                .responseSchema(Schema.schema("UpdateClubResponse"))
                                .build())
                ));
    }

    @Override
    protected Object controller() {
        return new ClubController(clubCommandService, clubQueryService);
    }
}
