package com.dangdang.tb_mong.auth.service;

import com.dangdang.tb_mong.auth.dto.KakaoUserInfoResponse;
import com.dangdang.tb_mong.auth.dto.UserInfoDto;
import com.dangdang.tb_mong.common.entity.*;
import com.dangdang.tb_mong.common.entity.Character;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.enumType.Role;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.jwt.JwtTokenProvider;
import com.dangdang.tb_mong.common.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final LocationRepository locationRepository;
    private final CharacterRepository characterRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final RepreCharacterRepository repreCharacterRepository;
    private final UserLocationSummaryRepository userLocationSummaryRepository;

    public String handleKakaoAuth(String kakaoAccessToken) {
        KakaoUserInfoResponse userInfo = getKakaoUserInfo(kakaoAccessToken);

        User user = userRepository.findByKakaoUuid(userInfo.getUuid())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER_FOR_SIGNUP));

        UserInfoDto userInfoDto = new UserInfoDto(user.getId(), user.getKakaoEmail());

        String result = jwtTokenProvider.createToken(userInfoDto);

        return result;
    }

    public String signUpUser(String kakaoAccessToken, String locationCode) {
        KakaoUserInfoResponse userInfo = getKakaoUserInfo(kakaoAccessToken);

        // 유저가 이미 존재하는지 확인
        Optional<User> existingUser = userRepository.findByKakaoUuid(userInfo.getUuid());
        if (existingUser.isPresent()) {
            UserInfoDto userInfoDto = new UserInfoDto(existingUser.get().getId(), existingUser.get().getKakaoEmail());
            return jwtTokenProvider.createToken(userInfoDto);
        }

        Location location = locationRepository.findByCode(locationCode)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_LOCATION));

        User user = User.builder()
                .kakaoUuid(userInfo.getUuid())
                .nickname(userInfo.getNickname())
                .kakaoEmail(userInfo.getEmail())
                .level(1)
                .exp(0)
                .role(Role.USER)
                .total_count(0)
                .total_km(BigDecimal.valueOf(0.0))
                .location(location)
                .build();

        userRepository.save(user);

        UserLocationSummary userLocationSummary = UserLocationSummary.builder()
                .km(BigDecimal.valueOf(0.0))
                .count(0)
                .user(user)
                .location(location)
                .build();

        userLocationSummaryRepository.save(userLocationSummary);

        List<Character> characters = characterRepository.findAll();

        UserCharacter userCharacter = UserCharacter.builder()
                .unlocked(true)
                .isRepresentative(true)
                .character(characters.get(0))
                .user(user)
                .build();

        userCharacterRepository.save(userCharacter);

        RepreCharacter repreCharacter = RepreCharacter.builder()
                .userCharacter(userCharacter)
                .user(user)
                .build();

        repreCharacterRepository.save(repreCharacter);

        for (int i = 1; i<characters.size(); i++){
            UserCharacter uCharacter = UserCharacter.builder()
                    .unlocked(false)
                    .isRepresentative(false)
                    .character(characters.get(i))
                    .user(user)
                    .build();

            userCharacterRepository.save(uCharacter);
        }

        UserInfoDto userInfoDto = new UserInfoDto(user.getId(), user.getKakaoEmail());
        // JWT 토큰 발급
        String result = jwtTokenProvider.createToken(userInfoDto);

        return result;
    }

    public KakaoUserInfoResponse getKakaoUserInfo(String token) {
        final String RequestUrl = "https://kapi.kakao.com/v2/user/me";
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);

        // add header
        post.addHeader("Authorization", "Bearer " + token);

        JsonNode returnNode = null;

        try {
            final HttpResponse response = client.execute(post);
            final int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("\nSending 'POST' request to URL : " + RequestUrl);
            System.out.println("Response Code : " + responseCode);

            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

        JsonNode userInfo = returnNode;

        // Get id
        String id = userInfo.path("id").asText();
        String name = null;
        String email = null;

        // 유저정보 카카오에서 가져오기 Get properties
        JsonNode properties = userInfo.path("properties");
        JsonNode kakao_account = userInfo.path("kakao_account");

        name = properties.path("nickname").asText();
        email = kakao_account.path("email").asText();

        System.out.println("id : " + id);
        System.out.println("nickname : " + name);
        System.out.println("email : " + email);

        KakaoUserInfoResponse kakaoUserInfoResponse = new KakaoUserInfoResponse(id, name, email);

        return kakaoUserInfoResponse;
    }
}
