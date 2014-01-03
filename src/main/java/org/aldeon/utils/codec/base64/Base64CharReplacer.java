package org.aldeon.utils.codec.base64;

import org.aldeon.utils.codec.Codec;
import org.aldeon.utils.conversion.ConversionException;

import java.nio.ByteBuffer;

/**
 * Removes unsafe characters from the standard base64 alphabet.
 *
 * The replaced chars are:
 *
 *       '='    replaced with    '-'
 *       '+'    replaced with    '.'
 *       '/'    replaced with    '_'
 *
 *
 * RFC 1738, page 3:  (www.ietf.org/rfc/rfc1738.txt)
 *
 *      Thus, only alphanumerics, the special characters "$-_.+!*'(),", and
 *      reserved characters used for their reserved purposes may be used
 *      unencoded within a URL.
 *
 *
 * The '+' sign, while safe according to RFC, is used as a replacement for
 * a space character. This is why it was decided to treat it as an unsafe
 * character.
 *
 */
class Base64CharReplacer implements Codec {

    private final Codec codec;

    public Base64CharReplacer(Codec codec) {
        this.codec = codec;
    }

    @Override
    public String encode(ByteBuffer buffer) {
        return codec.encode(buffer).replace('=', '-').replace('+', '.').replace('/', '_');
    }

    @Override
    public ByteBuffer decode(String string) throws ConversionException {
        return codec.decode(string.replace('-', '=').replace('.', '+').replace('_', '/'));
    }
}
