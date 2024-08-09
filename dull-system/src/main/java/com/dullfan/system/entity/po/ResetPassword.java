package com.dullfan.system.entity.po;

import com.dullfan.common.constant.UserConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPassword {
    /**
     * 旧密码
     */
    @NotBlank(message = "用户密码不能为空")
    @Size(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "密码长度必须在5到20个字符之间")
    private String oldPassword;
    /**
     * 新密码
     */
    @NotBlank(message = "用户密码不能为空")
    @Size(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "密码长度必须在5到20个字符之间")
    private String newPassword;
}
