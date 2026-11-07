package com.example.zhaocore.compat.tacz;

import com.tacz.guns.api.item.attachment.AttachmentType;

public final class ApexAttachments {
    public static final String TYPE_ID = "apex_attachments";
    public static final String ENUM_NAME = "APEX_ATTACHMENTS";

    private ApexAttachments() {
    }

    public static AttachmentType type() {
        return AttachmentType.valueOf(ENUM_NAME);
    }

    public static boolean isApex(AttachmentType type) {
        return type != null && ENUM_NAME.equals(type.name());
    }
}
