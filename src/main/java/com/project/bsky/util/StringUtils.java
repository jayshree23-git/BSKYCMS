package com.project.bsky.util;

import java.util.ResourceBundle;

 
public class StringUtils {
    private StringUtils(){}
    public static final ResourceBundle BSKY_RESOURCE_BUNDLE = ResourceBundle.getBundle("application");

    public static final String WINDOWS_ROOT_FOLDER = BSKY_RESOURCE_BUNDLE.getString("file.upload.directory.windows");
    public static final String LINUX_ROOT_FOLDER = BSKY_RESOURCE_BUNDLE.getString("file.upload.directory.linux");
    public static final String OPERATING_SYSTEM = System.getProperty("os.name").toLowerCase().trim();

    public static final String HOST = EncryptionUtils.decryptCode(BSKY_RESOURCE_BUNDLE.getString("spring.mail.host"));
    public static final String PORT = BSKY_RESOURCE_BUNDLE.getString("spring.mail.port");
    public static final String AUTH = BSKY_RESOURCE_BUNDLE.getString("spring.mail.properties.mail.smtp.auth");
    public static final String STARTTLS = BSKY_RESOURCE_BUNDLE.getString("spring.mail.properties.mail.smtp.starttls.enable");
    public static final String USERNAME = EncryptionUtils.decryptCode(BSKY_RESOURCE_BUNDLE.getString("spring.mail.username"));
    public static final String PASSWORD = EncryptionUtils.decryptCode(BSKY_RESOURCE_BUNDLE.getString("spring.mail.password"));
    public static final String WHATSAPP_SENDER_URL = BSKY_RESOURCE_BUNDLE.getString("whatsapp.sender.url");
    public static final String WHATSAPP_ACTION = BSKY_RESOURCE_BUNDLE.getString("whatsapp.action");
}
