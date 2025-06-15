package com.example.demo.domain.user.domain.repository;

import com.example.demo.domain.user.domain.dto.UpdateUserRequestDto;
import com.example.demo.domain.user.domain.model.QUser;
import com.example.demo.global.dto.AuthUserDto;
import com.example.demo.global.enums.CustomErrorCode;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.util.PasswordEncoder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final PasswordEncoder passwordEncoder;

    public UserRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory, PasswordEncoder passwordEncoder) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public long updateUser(UpdateUserRequestDto requestDto, AuthUserDto userDto) {
        QUser qUser = QUser.user;
        Map<String, String> updateFields = requestDto.getUpdateMap();

        JPAUpdateClause jpaUpdateClause = jpaQueryFactory.update(qUser).where(qUser.email.eq(userDto.getEmail()));

        for(String key : updateFields.keySet()) {
            switch (key) {
                case "username" : {
                    jpaUpdateClause.set(qUser.username, updateFields.get(key));
                    break;
                }
                case "password" : {
                    String password = updateFields.get(key).describeConstable()
                            .orElseThrow(() -> new CustomException(CustomErrorCode.PASSWORD_INVALID_FORMAT));
                    String confirmPassword = updateFields.get("confirmPassword")
                            .describeConstable().orElseThrow(() -> new CustomException(CustomErrorCode.PASSWORD_INVALID_FORMAT));
                    if(!Objects.equals(password, confirmPassword)) {
                        throw new CustomException(CustomErrorCode.PASSWORD_WRONG);
                    }

                    String encodedPassword = passwordEncoder.encode(password);
                    jpaUpdateClause.set(qUser.password, encodedPassword);
                    break;
                }
                case "confirmPassword" : {
                    break;
                }
                default : {
                    throw new CustomException(CustomErrorCode.CANNOT_CHANGE_VALUE);
                }
            }
        }

        jpaUpdateClause.set(qUser.modifiedAt, LocalDateTime.now());
        return jpaUpdateClause.execute();
    }
}
