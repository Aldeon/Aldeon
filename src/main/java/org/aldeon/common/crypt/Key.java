package org.aldeon.common.crypt;

import org.aldeon.common.model.ByteSource;
import java.nio.ByteBuffer;

/**
 * Todo: write the RSA wrapper
 */
public interface Key extends ByteSource {
    public ByteBuffer process(ByteBuffer buffer);
}
