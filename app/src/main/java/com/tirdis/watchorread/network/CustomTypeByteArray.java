package com.tirdis.watchorread.network;

import java.io.IOException;
import java.io.OutputStream;

import retrofit.mime.TypedByteArray;

/**
 * Created by admin on 5/22/2015.
 */
public class CustomTypeByteArray  extends TypedByteArray {
        private final byte[] bytes;
        /**
         * Constructs a new typed byte array.  Sets mimeType to {@code application/unknown} if absent.
         *
         * @param mimeType
         * @param bytes
         * @throws NullPointerException if bytes are null
         */
        public CustomTypeByteArray(String mimeType, byte[] bytes) {
            super(mimeType, bytes);
            this.bytes = bytes;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            out.write(this.bytes);
        }
    }


