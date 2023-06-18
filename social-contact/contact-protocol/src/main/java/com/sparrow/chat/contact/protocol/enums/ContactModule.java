package com.sparrow.chat.contact.protocol.enums;

import com.sparrow.protocol.ModuleSupport;

public class ContactModule {
    public static final ModuleSupport IM_CONTACT = new ModuleSupport() {
        @Override
        public String code() {
            return "40";
        }

        @Override
        public String name() {
            return "IM_CONTACT";
        }
    };

    public static final ModuleSupport IM_QUN = new ModuleSupport() {
        @Override
        public String code() {
            return "50";
        }

        @Override
        public String name() {
            return "IM_QUN";
        }
    };
}
