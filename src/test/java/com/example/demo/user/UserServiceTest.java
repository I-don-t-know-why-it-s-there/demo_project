package com.example.demo.user;

import com.example.demo.TestConst;
import com.example.demo.domain.user.domain.dto.*;
import com.example.demo.domain.user.domain.model.User;
import com.example.demo.domain.user.domain.repository.UserRepository;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.dto.AuthUserDto;
import com.example.demo.global.enums.CustomErrorCode;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private CreateUserRequestDto createUserRequestDto;
    private LoginUserRequestDto loginUserRequestDto;
    private UpdateUserRequestDto updateUserRequestDto;
    private AuthUserDto authUserDto;
    private DeleteUserRequest deleteUserRequest;

    @BeforeEach
    void setUp() {
        testUser = new User(TestConst.TEST_EMAIL, TestConst.TEST_PASSWORD, TestConst.TEST_USERNAME, TestConst.TEST_ROLE);

        createUserRequestDto = new CreateUserRequestDto(
                TestConst.TEST_EMAIL,
                TestConst.TEST_PASSWORD,
                TestConst.TEST_PASSWORD,
                TestConst.TEST_USERNAME,
                TestConst.TEST_ROLE
        );

        loginUserRequestDto = new LoginUserRequestDto();
        ReflectionTestUtils.setField(loginUserRequestDto, "email", TestConst.TEST_EMAIL);
        ReflectionTestUtils.setField(loginUserRequestDto, "password", TestConst.TEST_PASSWORD);

        Map<String, String> updateMap = new HashMap<>();
        updateMap.put("username", TestConst.TEST_NEW_USERNAME);
        updateUserRequestDto = new UpdateUserRequestDto();
        ReflectionTestUtils.setField(updateUserRequestDto, "updateMap", updateMap);

        authUserDto = new AuthUserDto(TestConst.TEST_USER_ID, TestConst.TEST_EMAIL, TestConst.TEST_ROLE);

        deleteUserRequest = new DeleteUserRequest();
        ReflectionTestUtils.setField(deleteUserRequest, "password", TestConst.TEST_PASSWORD);
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void createUserSuccess() {
        // given
        given(userRepository.existsByEmail(TestConst.TEST_EMAIL)).willReturn(false);
        given(passwordEncoder.encode(TestConst.TEST_PASSWORD)).willReturn(TestConst.TEST_PASSWORD);
        given(userRepository.save(any(User.class))).willReturn(testUser);

        // when
        CreateUserResponseDto responseDto = userService.createUser(createUserRequestDto);

        // then
        assertThat(responseDto.getEmail()).isEqualTo(TestConst.TEST_EMAIL);
        assertThat(responseDto.getUsername()).isEqualTo(TestConst.TEST_USERNAME);
        verify(userRepository).existsByEmail(TestConst.TEST_EMAIL);
        verify(passwordEncoder).encode(TestConst.TEST_PASSWORD);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 시도시 실패")
    void createUserWithExistingEmail() {
        // given
        given(userRepository.existsByEmail(TestConst.TEST_EMAIL)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.createUser(createUserRequestDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", CustomErrorCode.EMAIL_ALREADY_EXIST);
        verify(userRepository).existsByEmail(TestConst.TEST_EMAIL);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("비밀번호와 비밀번호 확인이 일치하지 않을 때 회원가입 실패")
    void createUserWithPasswordMismatch() {
        // given
        ReflectionTestUtils.setField(createUserRequestDto, "confirmPassword", "differentPassword");

        // when & then
        assertThatThrownBy(() -> userService.createUser(createUserRequestDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", CustomErrorCode.PASSWORD_INVALID_FORMAT);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() {
        // given
        given(userRepository.findByEmailAndDeletedFalse(TestConst.TEST_EMAIL))
                .willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(TestConst.TEST_PASSWORD, TestConst.TEST_PASSWORD))
                .willReturn(true);

        // when
        LoginUserResponseDto responseDto = userService.loginUser(loginUserRequestDto);

        // then
        assertThat(responseDto.getEmail()).isEqualTo(TestConst.TEST_EMAIL);
        assertThat(responseDto.getUsername()).isEqualTo(TestConst.TEST_USERNAME);
        verify(userRepository).findByEmailAndDeletedFalse(TestConst.TEST_EMAIL);
        verify(passwordEncoder).matches(TestConst.TEST_PASSWORD, TestConst.TEST_PASSWORD);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시도시 실패")
    void loginWithNonExistingEmail() {
        // given
        given(userRepository.findByEmailAndDeletedFalse(TestConst.TEST_EMAIL))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.loginUser(loginUserRequestDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", CustomErrorCode.EMAIL_NOT_EXIST);
        verify(userRepository).findByEmailAndDeletedFalse(TestConst.TEST_EMAIL);
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도시 실패")
    void loginWithWrongPassword() {
        // given
        given(userRepository.findByEmailAndDeletedFalse(TestConst.TEST_EMAIL))
                .willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(TestConst.TEST_PASSWORD, TestConst.TEST_PASSWORD))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.loginUser(loginUserRequestDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", CustomErrorCode.PASSWORD_WRONG);
        verify(userRepository).findByEmailAndDeletedFalse(TestConst.TEST_EMAIL);
        verify(passwordEncoder).matches(TestConst.TEST_PASSWORD, TestConst.TEST_PASSWORD);
    }

    @Test
    @DisplayName("사용자 정보 조회 성공 테스트")
    void getUserSuccess() {
        // given
        given(userRepository.findById(TestConst.TEST_USER_ID)).willReturn(Optional.of(testUser));

        // when
        FindUserResponseDto responseDto = userService.getUser(authUserDto);

        // then
        assertThat(responseDto.getEmail()).isEqualTo(TestConst.TEST_EMAIL);
        assertThat(responseDto.getUsername()).isEqualTo(TestConst.TEST_USERNAME);
        verify(userRepository).findById(TestConst.TEST_USER_ID);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 정보 조회 실패")
    void getUserNotFound() {
        // given
        given(userRepository.findById(TestConst.TEST_USER_ID)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUser(authUserDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", CustomErrorCode.USER_NOT_EXIST);
        verify(userRepository).findById(TestConst.TEST_USER_ID);
    }

    @Test
    @DisplayName("사용자 정보 수정 성공 테스트")
    void updateUserSuccess() {
        // given
        given(userRepository.updateUser(updateUserRequestDto, authUserDto)).willReturn(1L);
        given(userRepository.findByEmailAndDeletedFalse(TestConst.TEST_EMAIL))
                .willReturn(Optional.of(testUser));

        // when
        UpdateUserResponseDto responseDto = userService.updateUser(updateUserRequestDto, authUserDto);

        // then
        assertThat(responseDto.getEmail()).isEqualTo(TestConst.TEST_EMAIL);
        assertThat(responseDto.getUserName()).isEqualTo(TestConst.TEST_USERNAME);
        verify(userRepository).updateUser(updateUserRequestDto, authUserDto);
        verify(userRepository).findByEmailAndDeletedFalse(TestConst.TEST_EMAIL);
    }

    @Test
    @DisplayName("사용자 정보 수정 실패 - 업데이트된 데이터 없음")
    void updateUserNoDataUpdated() {
        // given
        given(userRepository.updateUser(updateUserRequestDto, authUserDto)).willReturn(0L);

        // when & then
        assertThatThrownBy(() -> userService.updateUser(updateUserRequestDto, authUserDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", CustomErrorCode.INVALID_REQUEST);
        verify(userRepository).updateUser(updateUserRequestDto, authUserDto);
        verify(userRepository, never()).findByEmailAndDeletedFalse(any());
    }

    @Test
    @DisplayName("사용자 정보 수정 실패 - 업데이트된 데이터가 너무 많음")
    void updateUserTooManyDataUpdated() {
        // given
        given(userRepository.updateUser(updateUserRequestDto, authUserDto)).willReturn(2L);

        // when & then
        assertThatThrownBy(() -> userService.updateUser(updateUserRequestDto, authUserDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", CustomErrorCode.INVALID_REQUEST);
        verify(userRepository).updateUser(updateUserRequestDto, authUserDto);
        verify(userRepository, never()).findByEmailAndDeletedFalse(any());
    }

    @Test
    @DisplayName("사용자 삭제 성공 테스트")
    void deleteUserSuccess() {
        // given
        given(userRepository.findById(TestConst.TEST_USER_ID)).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(TestConst.TEST_PASSWORD, TestConst.TEST_PASSWORD)).willReturn(true);
        given(userRepository.save(any(User.class))).willReturn(testUser);

        // when
        DeleteUserResponse responseDto = userService.deleteUser(deleteUserRequest, authUserDto);

        // then
        assertThat(responseDto.getUsername()).isEqualTo(TestConst.TEST_USERNAME);
        verify(userRepository).findById(TestConst.TEST_USER_ID);
        verify(passwordEncoder).matches(TestConst.TEST_PASSWORD, TestConst.TEST_PASSWORD);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 삭제 실패 - 존재하지 않는 사용자")
    void deleteUserNotFound() {
        // given
        given(userRepository.findById(TestConst.TEST_USER_ID)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.deleteUser(deleteUserRequest, authUserDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", CustomErrorCode.USER_NOT_EXIST);
        verify(userRepository).findById(TestConst.TEST_USER_ID);
        verify(passwordEncoder, never()).matches(any(), any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 삭제 실패 - 비밀번호 불일치")
    void deleteUserWrongPassword() {
        // given
        given(userRepository.findById(TestConst.TEST_USER_ID)).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(TestConst.TEST_PASSWORD, TestConst.TEST_PASSWORD)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.deleteUser(deleteUserRequest, authUserDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", CustomErrorCode.PASSWORD_WRONG);
        verify(userRepository).findById(TestConst.TEST_USER_ID);
        verify(passwordEncoder).matches(TestConst.TEST_PASSWORD, TestConst.TEST_PASSWORD);
        verify(userRepository, never()).save(any(User.class));
    }
} 