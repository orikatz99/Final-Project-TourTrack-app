package com.example.tourtrack.models;

public class PrivacyResponseWrapper {
    private PrivacyData data;

    public PrivacyData getData() {
        return data;
    }

    public static class PrivacyData {
        private PrivacySettings privacySettings;
        private NotificationSettings notificationsSettings;

        public PrivacySettings getPrivacySettings() {
            return privacySettings;
        }

        public NotificationSettings getNotificationsSettings() {
            return notificationsSettings;
        }
    }

    public static class PrivacySettings {
        private boolean LocationSharing;
        private boolean ShowOnlineStatus;
        private boolean AllowPhoneCalls;
        private boolean EnableWhatsapp;
        private boolean ShowEmailToOthers;

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
    }

    public static class NotificationSettings {
        private boolean emailNotifications;
        private boolean pushNotifications;

        public boolean isEmailNotifications() {
            return emailNotifications;
        }

        public boolean isPushNotifications() {
            return pushNotifications;
        }
    }
}
