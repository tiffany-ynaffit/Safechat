package com.example.safechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private ListView listView;

    private DatabaseReference databaseReference;

    private String stringMessage;

    private byte encryptionKey[] = {2, 11, 6, 12, 49, 33, 4, 19, 55, 32, 39, 63, -23, -80, 15, 94};

    private Cipher cipher, decipher;

    private SecretKeySpec secretKeySpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        listView = findViewById(R.id.listview);


        databaseReference = FirebaseDatabase.getInstance().getReference("Message");

        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //..........
                try
                {

                    stringMessage = dataSnapshot.getValue().toString();

                    stringMessage = stringMessage.substring(1, stringMessage.length()-1);

                    String[] stringMessageArray = stringMessage.split(", ");

                    //String[] stringFinal = new String[stringMessageArray.length*2];

                /*
                Arrays.sort(stringMessageArray);
                String[] stringFinal = new String[stringMessageArray.length*2];
                try {
                    for (int i = 0; i<stringMessageArray.length; i++) {
                        String[] stringKeyValue = stringMessageArray[i].split("=", 2);
                        stringFinal[2 * i] = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss", Long.parseLong(stringKeyValue[0]));
                        stringFinal[2 * i + 1] = AESDecryptionMethod(stringKeyValue[1]);
                    }
                    listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stringFinal));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
        catch (Exception e){
        e.printStackTrace();
    }
}
*/
                    Arrays.sort(stringMessageArray);
                    String[] stringFinal = new String[stringMessageArray.length*2];
                    try {
                        for (int i = 0; i<stringMessageArray.length; i++) {
                            String[] stringKeyValue = stringMessageArray[i].split("=", 2);
                            //stringFinal[2 * i] = (String) android.text.format.DateFormat.format("dd-MM-YYYY hh:mm:ss", Long.parseLong(stringKeyValue[0]));
                            stringFinal[2 * i] = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss", Long.parseLong(stringKeyValue [0]));
                            stringFinal[2 * i + 1] = AESDecryptionMethod(stringKeyValue[1]);
                        }
                        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stringFinal));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (NullPointerException ignored)
                {

                }
                //.....

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }

    public void sendButton(View view) {

        Date date = new Date();
        databaseReference.child(Long.toString(date.getTime())).setValue(AESEncryptionMethod(editText.getText().toString()));
        editText.setText("");
    }


    //ENCRYPTION METHOD

    private String AESEncryptionMethod(String string)
    {
        //convert the string to byte format
        byte [] stringByte = string.getBytes();
        //the output of encrypted byte stored here
        byte[] encryptedByte = new byte[stringByte.length];

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedByte = cipher.doFinal(stringByte);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }


        //CONVERT ENCRYPTED BYTE BACK TO STRING

        String returnString = null;
        try {
            returnString = new String(encryptedByte, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return returnString;

    }

    //DECRYPTION METHOD
    private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {

//get the string that had been converted to byte
        byte [] EncryptedByte = string.getBytes("ISO-8859-1");
        String decryptedString = string;

        byte[] decryption;

        try {
            decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decryption = decipher.doFinal(EncryptedByte);
            decryptedString =new String(decryption);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return decryptedString;


    }


}

/* databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stringMessage = dataSnapshot.getValue().toString();
                stringMessage = stringMessage.substring(1,stringMessage.length()-1);
                String[] stringMessageArray = stringMessage.split(", ");
                Arrays.sort(stringMessageArray);
                String[] stringFinal = new String[stringMessageArray.length*2];
                try {
                    for (int i = 0; i<stringMessageArray.length; i++) {
                        String[] stringKeyValue = stringMessageArray[i].split("=", 2);
                        stringFinal[2 * i] = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss", Long.parseLong(stringKeyValue[0]));
                        stringFinal[2 * i + 1] = AESDecryptionMethod(stringKeyValue[1]);
                    }
                    listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, stringFinal));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
        catch (Exception e){
        e.printStackTrace();
    }
}

*/
