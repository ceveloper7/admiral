package com.admiral.kernel.util.secure;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Secure implements SecureInterface{
    private static final Logger LOGGER = Logger.getLogger(Secure.class.getName());

    // Admiral Cipher
    private Cipher m_cipher = null;
    private SecretKey m_key = null;

    private synchronized void initCipher() {
        if (m_cipher != null) {
            return;
        }
        Cipher cc = null;
        try{
            cc = Cipher.getInstance("DES/ECB/PKCS5Padding");
            m_key = new SecretKeySpec(new byte[]{100,25,28,-122,-26,94,-3,-26}, "DES");
        }
        catch(Exception e){
            LOGGER.severe(e.getMessage());
        }
        m_cipher = cc;
    }

    public Secure(){initCipher();}

    public static byte[] convertHexStringToByteArray(String hexString) {
        if(hexString == null || hexString.isEmpty()){
            return null;
        }
        int size = hexString.length() / 2;
        byte[] retValue = new byte[size];
        String inString = hexString.toLowerCase();
        try{
            for (int i = 0; i < size; i++) {
                int index = i * 2;
                int ii = Integer.parseInt(inString.substring(index, index + 2), 16);
                retValue[i] = (byte) ii;
            }
            return retValue;
        }
        catch (Exception e){
            LOGGER.log(Level.SEVERE, hexString + " - " + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public String encrypt(String value) {
        return "";
    }

    @Override
    public String decrypt(String value) {
        if (value == null || value.length() == 0)
            return value;
        boolean isEncrypted = value.startsWith(ENCRYPTEDVALUE_START) && value.endsWith(ENCRYPTEDVALUE_END);
        if (isEncrypted)
            value = value.substring(ENCRYPTEDVALUE_START.length(), value.length()-ENCRYPTEDVALUE_END.length());
        byte[] data = convertHexStringToByteArray(value);
        if (data == null)	//	cannot decrypt
        {
            if (isEncrypted)
            {
                // log.info("Failed: " + value);
                LOGGER.info("Failed");
                return null;
            }
            //	assume not encrypted
            return value;
        }
        //	Init
        if (m_cipher == null)
            initCipher();

        //	Encrypt
        if (m_cipher != null && value != null && value.length() > 0){
            try
            {
                AlgorithmParameters ap = m_cipher.getParameters();
                m_cipher.init(Cipher.DECRYPT_MODE, m_key, ap);
                byte[] out = m_cipher.doFinal(data);
                String retValue = new String(out, "UTF8");
                return retValue;
            }
            catch (Exception ex)
            {
                LOGGER.info("Failed decrypting " + ex.toString());
            }
        }
        return null;
    }
}
