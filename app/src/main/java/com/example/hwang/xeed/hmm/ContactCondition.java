package com.example.hwang.xeed.hmm;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import cooker.core.annotations.CookerCondition;
import cooker.core.annotations.CookerSerializable;
import cooker.core.annotations.CookingIngredient;

/**
 * Created by Hwang on 2016-08-18.
 */
@CookingIngredient
public class ContactCondition {

    @CookerSerializable(key = "number")
    String phoneNumber;
    @CookerSerializable(key = "paramName")
    String paramName;
    @CookerSerializable(key = "activity")
    Activity activity;

    Cursor cursor;
    int counter;

    @CookerCondition
    public boolean getContacts() {

        String phoneNumber = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        ContentResolver contentResolver = activity.getContentResolver();
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
                            this.phoneNumber = phoneNumber;
                            return true;
                        }
                    }
                    phoneCursor.close();
                }
            }
        }
        return false;
    }

    private boolean isMatchUserName(String name, String paramName) {
        return name.toLowerCase().indexOf(paramName.toLowerCase()) >= 0 ? true : false;
    }

}
