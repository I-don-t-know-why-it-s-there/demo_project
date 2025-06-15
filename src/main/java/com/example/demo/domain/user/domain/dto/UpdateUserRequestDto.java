package com.example.demo.domain.user.domain.dto;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Map;

import static com.example.demo.global.constant.ValidationMessage.*;

@Getter
public class UpdateUserRequestDto {

    private Map<String, String> updateMap;

    @AssertFalse(message = UPDATE)
    public boolean isValidUpdateMap() {
        return this.updateMap.isEmpty();
    }
}
