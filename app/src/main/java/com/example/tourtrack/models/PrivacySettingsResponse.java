package com.example.tourtrack.models;

public class PrivacySettingsResponse {
    private boolean LocationSharing;
    private boolean ShowOnlineStatus;
    private boolean AllowPhoneCalls;
    private boolean EnableWhatsapp;
    private boolean ShowEmailToOthers;
    private boolean emailNotifications;
    private boolean pushNotifications;

    public PrivacySettingsResponse(boolean locationSharing, boolean showOnlineStatus, boolean allowPhoneCalls,
                                    boolean enableWhatsapp, boolean showEmailToOthers, boolean emailNotifications,
                                    boolean pushNotifications) {
        LocationSharing = locationSharing;
        ShowOnlineStatus = showOnlineStatus;
        AllowPhoneCalls = allowPhoneCalls;
        EnableWhatsapp = enableWhatsapp;
        ShowEmailToOthers = showEmailToOthers;
        this.emailNotifications = emailNotifications;
        this.pushNotifications = pushNotifications;
    }
    public boolean isLocationSharing() {
        return LocationSharing;
    }
    public boolean isShowOnlineStatus() {
        return ShowOnlineStatus;
    }
    public boolean isAllowPhoneCalls() {
        return AllowPhoneCalls;
    }
    public boolean isEnableWhatsapp() {
        return EnableWhatsapp;
    }
    public boolean isShowEmailToOthers() {
        return ShowEmailToOthers;
    }
    public boolean isEmailNotifications() {
        return emailNotifications;
    }
    public boolean isPushNotifications() {
        return pushNotifications;
    }

}
