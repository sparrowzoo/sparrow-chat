package com.sparrow.chat.protocol.error;

import com.sparrow.protocol.ModuleSupport;

public class CharModule {
    public static final ModuleSupport AUDIT = new ModuleSupport() {
        @Override
        public String code() {
            return "01";
        }

        @Override
        public String name() {
            return "Audit";
        }
    };
}
