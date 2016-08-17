package com.example.hwang.xeed;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends android.app.Activity {

    Button submitBtn = null;
    EditText nameText = null;
    private ProgressDialog pDialog;
    Cursor cursor;
    int counter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = (EditText) findViewById(R.id.nameText);
        submitBtn = (Button) findViewById(R.id.call);
    }


    public void onButtonClicked(View v) {
        String name = nameText.getText().toString();
        findNumberByName(name);
    }

    private void findNumberByName(final String name) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Finding " + name + "...");
        pDialog.setCancelable(false);
        pDialog.show();

        // Since reading contacts takes more time, let's run it on a separate thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                String phone = getContacts(name);

                pDialog.cancel();
                if (phone == null) {
                    Toast.makeText(getApplicationContext(), "sss", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + phone));
                    startActivity(myIntent);
                }
            }
        }).start();
    }

    public String getContacts(String paramName) {

        String phoneNumber = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        ContentResolver contentResolver = getContentResolver();
        cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {
            counter = 0;
            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                if (hasPhoneNumber > 0) {
                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                        if (isMatchUserName(name, paramName)) {
                            return phoneNumber;
                        }
                    }
                    phoneCursor.close();
                }
            }
        }
        return null;
    }

    private boolean isMatchUserName(String name, String paramName) {
        return name.toLowerCase().indexOf(paramName.toLowerCase()) >= 0 ? true : false;
    }
}