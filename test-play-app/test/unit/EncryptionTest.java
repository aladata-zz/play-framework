package unit;

import org.junit.*;
import util.EncryptHelper;

import static org.fest.assertions.Assertions.assertThat;


/**
 * Created by aabramov on 12/6/14.
 */
public class EncryptionTest {

    @Test
    public void encryptDecryptSuccess() {
        String toEncrypt = "qwergddvbhh!@#$%^&*()_+|}{:?><,./;'[]\\=-'\"";
        String enc = EncryptHelper.encrypt(toEncrypt);
        //System.out.println(enc);
        String dec = EncryptHelper.decrypt(enc);
        //System.out.println(dec);
        assertThat(toEncrypt).isEqualTo(dec);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decryptBadStringException() {
        String toEncrypt = "qwergddvbhh!@#$%^&*()_+|}{:?><,./;'[]\\=-'\"";
        EncryptHelper.decrypt(toEncrypt);
    }

    @Test
    public void hashSuccess(){
        String toEncrypt = "qwergddvbhh!@#$%^&*()_+|}{:?><,./;'[]\\=-'\"";
        String key = "asd2@#$@g23#%GS_ASDF$#%";
        String hash = EncryptHelper.hash(toEncrypt, key);

        //System.out.println(hash);

    }

}
