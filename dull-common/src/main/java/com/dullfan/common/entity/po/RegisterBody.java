package com.dullfan.common.entity.po;

import com.dullfan.common.constant.UserConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterBody {
    @NotBlank(message = "用户名不能为空")
    @Size(min = UserConstants.USERNAME_MIN_LENGTH, max = UserConstants.USERNAME_MAX_LENGTH, message = "账户长度必须在2到20个字符之间")
    private String username;

    @NotBlank(message = "用户密码不能为空")
    @Size(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "密码长度必须在5到20个字符之间")
    private String password;
}
