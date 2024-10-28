package com.dangdang.tb_mong.auth.service;

import com.dangdang.tb_mong.auth.dto.KakaoUserInfoResponse;
import com.dangdang.tb_mong.common.entity.User;
import com.dangdang.tb_mong.common.enumType.ErrorCode;
import com.dangdang.tb_mong.common.exception.CustomException;
import com.dangdang.tb_mong.common.repository.UserRepository;
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


@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final UserRepository userRepository;

    public String handleKakaoAuth(String token) {
        KakaoUserInfoResponse userInfo = getKakaoUserInfo(token);

        User user = userRepository.findByKakaoUuid(userInfo.getUuid())
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));

        return "none";
    }

    public String signUpUser(String token, String locationName) {
        return "none";
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
