package com.example.zhaocore.compat.tacz;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tacz.guns.api.item.attachment.AttachmentType;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class AttachmentTypeAdapter extends TypeAdapter<AttachmentType> {
    private final Map<String, AttachmentType> byName = new HashMap<>();
    private final Map<AttachmentType, String> names = new EnumMap<>(AttachmentType.class);

    public AttachmentTypeAdapter() {
        for (AttachmentType type : AttachmentType.values()) {
            String name = type.name();
            if (ApexAttachments.isApex(type)) {
                name = ApexAttachments.TYPE_ID;
            } else {
                try {
                    SerializedName annotation = AttachmentType.class.getField(type.name())
                            .getAnnotation(SerializedName.class);
                    if (annotation != null) {
                        name = annotation.value();
                        for (String alternate : annotation.alternate()) {
                            byName.put(alternate, type);
                        }
                    }
                } catch (NoSuchFieldException exception) {
                    throw new IllegalStateException("Missing attachment enum field: " + type.name(), exception);
                }
            }
            byName.put(type.toString(), type);
            byName.put(name, type);
            names.put(type, name);
        }
    }

    @Override
    public void write(JsonWriter out, AttachmentType value) throws IOException {
        out.value(value == null ? null : names.get(value));
    }

    @Override
    public AttachmentType read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return byName.get(in.nextString());
    }
}
