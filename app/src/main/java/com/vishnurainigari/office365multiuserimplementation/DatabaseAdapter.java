package com.vishnurainigari.office365multiuserimplementation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by vishnurainigari on 10/26/15.
 */
public class DatabaseAdapter {
    Context mContext;
    private static final String LOG_TAG = "Database Adapter";

    DatabaseHelper helper;

    public DatabaseAdapter(Context context)
    {
        this.mContext = context;
        helper = new DatabaseHelper(context);
    }

    public long insertData(String emailID,String userID,String token,String refreshToken)

    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.EMAILID,emailID);
        contentValues.put(DatabaseHelper.USERID,userID);
        contentValues.put(DatabaseHelper.TOKEN,token);
        contentValues.put(DatabaseHelper.REFRESH_TOKEN,refreshToken);
        long id = db.insert(DatabaseHelper.TABLE_NAME,null,contentValues);
        db.close();
        return id;
    }

    public ArrayList getAllData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<String> details = new ArrayList<>();
        String[] column = {DatabaseHelper.EMAILID,DatabaseHelper.USERID,
                DatabaseHelper.TOKEN,DatabaseHelper.REFRESH_TOKEN};
        Cursor emailcursor = db.query(DatabaseHelper.TABLE_NAME, column,null, null, null, null, null);
        while(emailcursor.moveToNext()){
            for (String aColumn : column) {
                int index = emailcursor.getColumnIndex(aColumn);
                details.add(emailcursor.getString(index));
            }
        }
        emailcursor.close();
        db.close();
        Log.d(LOG_TAG,"Details from database "+details.toString());
        return details;
     }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        Context context;

        private static final String DATABASE_NAME = "ACCOUNT_DB";
        private static final int DATABASE_VERSION = 1;
        public static final String COLUMN_ID = "_id";
        private static final String EMAILID = "emailID";
        private static final String USERID = "userID";
        private static final String TOKEN ="token";
        private static final String REFRESH_TOKEN = "refreshToken";
        private static final String TABLE_NAME = "ACCOUNTDETAILS" ;

        private static final String DATABASE_CREATE = "create table "
                + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, "
                + EMAILID + " varchar(255),"
                + USERID + " varchar(255),"
                + TOKEN + " varchar(255),"
                + REFRESH_TOKEN +" varchar(255));";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;

        public DatabaseHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG,"on Create called");
           try
           {
               db.execSQL(DATABASE_CREATE);
           }
           catch (Exception e)
           {
              e.printStackTrace();
           }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.d(LOG_TAG,"on upgrade called");
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
