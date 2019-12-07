package com.example.safetravels2;

import android.content.Context;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESDecrypt {

    static void Decrypt(String file_format, String file_encrypted, String password, Context ctx) throws Exception {

        try {
            // get the length of the encrypted filename
            String[] arrOfStr = file_encrypted.split("/",20);
            String minus_file = arrOfStr[arrOfStr.length-1];
            int sub = minus_file.length();
            // Create the filepath of the encrypted files
            String enc_path = file_encrypted.substring(0,file_encrypted.length()-sub);

            // reading the salt
            // user should have secure mechanism to transfer the
            // salt, iv and password to the recipient
            String path = ctx.getFilesDir().getAbsolutePath();
            File file_s = new File(enc_path + "salt.enc");
            FileInputStream saltFis = new FileInputStream(file_s);
            byte[] salt = new byte[8];
            saltFis.read(salt);
            saltFis.close();

            // reading the iv
            File file_i = new File(enc_path + "iv.enc");
            FileInputStream ivFis = new FileInputStream(file_i);
            byte[] iv = new byte[16];
            ivFis.read(iv);
            ivFis.close();

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            // file decryption
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            File file_e = new File(enc_path + "encryptedfile.des");
            FileInputStream fis = new FileInputStream(file_encrypted);

            FileOutputStream fos;
            File decryptfile = new File(enc_path + "decrypted_file");
            fos = new FileOutputStream(decryptfile + file_format);
            byte[] in = new byte[64];
            int read;
            while ((read = fis.read(in)) != -1) {
                byte[] output = cipher.update(in, 0, read);
                if (output != null)
                    fos.write(output);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                fos.write(output);
            fis.close();
            fos.flush();
            fos.close();

            String mydecPath = enc_path + "decrypted_file" + file_format;
            Toast.makeText(ctx, "Decrypted to " + mydecPath, Toast.LENGTH_LONG).show();

            // delete salt file
            boolean deleted_s = file_s.delete();
            // delete iv file
            boolean deleted_i = file_i.delete();
            // delete encrypted file
            boolean deleted_d = file_e.delete();



            // Error handling for user to see
        } catch(Exception e) {
            e.printStackTrace();
            // Notifies user that the incorrect key was used for decryption
            if (e instanceof BadPaddingException)
                Toast.makeText(ctx, "Incorrect key used for decryption", Toast.LENGTH_LONG).show();
            // Notifies user that the wrong file was selected
            if (e instanceof IllegalBlockSizeException)
                Toast.makeText(ctx, "Incorrect file selected", Toast.LENGTH_LONG).show();
            // Notifies user that they did not enter a key
            if (e instanceof InvalidKeySpecException)
                Toast.makeText(ctx, "Password key was not entered ", Toast.LENGTH_LONG).show();
            // Notifies user that a incorrect file was used or file was not selected
//            else
//                Toast.makeText(ctx, "Incorrect file for decryption ", Toast.LENGTH_LONG).show();
        }
    }
}
