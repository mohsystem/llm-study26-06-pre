package com.um.springbootprojstructure.dto;

public class ResetConfirmRequest {
    private String resetToken;
    private String newPassword;

    public ResetConfirmRequest() {}

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
