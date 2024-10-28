package com.dangdang.tb_mong.common.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400
    UNKNOWN(400, "알 수 없는 에러가 발생했습니다."),
    CONTENT_IS_NULL(400, "입력되지 않은 정보가 있습니다."),
    DUPLICATED_USERID(400, "중복된 아이디입니다."),
    NOT_FOUND_PARK_TYPE(400, "유효하지 않은 값입니다."),

    // 401
    NOT_FOUND_USER(401, "등록된 사용자가 없습니다."),
    NOT_FOUND_CHARACTER(401, "등록된 캐릭터가 없습니다."),
    NOT_MATCHING_INFO(401, "아이디 또는 비밀번호를 잘못 입력했습니다."),
    NOT_TOKEN(401, "토큰이 없습니다."),
    NOT_VALID_TOKEN(401, "토큰이 유효하지 않습니다."),
    FILE_NOT_FOUND(401, "파일을 찾을 수 없습니다."),
    CHARACTER_NOT_UNLOCKED(401, "캐릭터가 공개되지 않았습니다."),
    NOT_FOUND_USER_FOR_SIGNUP(401, "사용자 정보를 찾을 수 없습니다. 회원가입이 필요합니다."),
    NOT_FOUND_LOCATION(401, "등록된 동네가 없습니다."),


    // 403
    ACESS_DENIED_EMAIL(403, "접근 권한이 없는 사용자 요청입니다."),

    // 500
    INTERNAL_SERVER_ERROR(500, "서버 내부 문제입니다."),
    ;

    private final int code;
    private final String msg;
}
