package graduation.whatziroom.network;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import graduation.whatziroom.activity.base.BaseActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by mapl0 on 2017-08-01.
 */

public class DBSI extends SQLiteOpenHelper {

//    private static DBSI db;

    SQLiteDatabase db;
    private String[][] result;

    public DBSI(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        result = null;

    }

    public DBSI() {
        super(BaseActivity.mContext, "twentyQuestions.db", null, 1);

        result = null;
    }

//    public static DBSI getInstance() {
//
//        if(db == null) {
//            db = new DBSI(BaseActivity.mContext, "TwentyQuestions.db", null, 1);
//        }
//
//        return db;
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */

        setDB(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void query(String query) {

        Log.d("query : ", query);

        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(query);

        db.close();

    }

    public String[][] selectQuery(String query) {
        SQLiteDatabase db = getWritableDatabase();

        Log.d("Query", query);

        Cursor cursor;

        try {
            cursor = db.rawQuery(query, null);

            if (cursor.getCount() != 0) {

                this.result = new String[cursor.getCount()][cursor.getColumnCount()];

//            Log.d("getCount", cursor.getCount() + "");
//            Log.d("getColumnCount", cursor.getColumnCount() + "");

                cursor.moveToFirst();

                int i = 0;

                do {

                    for (int j = 0; j < cursor.getColumnCount(); j++) {

                        this.result[i][j] = cursor.getString(j);
                        System.out.println("Select Result........" + cursor.getColumnName(j) + "... : " + cursor.getString(j));

                    }

                    System.out.println("========================================================================================================");

                    i++;
//                System.out.println("moveToNext...." + cursor.moveToNext());
                } while (cursor.moveToNext());

            } else {
                Log.d("SelectNULL", "NULL");
                result = null;
            }

        } catch (SQLiteException e) {
            this.result = null;
        }


        db.close();
        return result;

    }

    public void update(String item, String price) {
        SQLiteDatabase db = getWritableDatabase();
    }

    public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
    }

    private void setDB(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS User (PKey integer(8) NOT NULL, Name varchar(20), ID varchar(20), PW varchar(60), Email varchar(30), " +
                "Status tinyint(3), Acount varchar(30), Longitude double(10), Latitude double(10), CreatedDate timestamp, UpdatedDate timestamp, UDID varchar(200), AutoLogin integer(1));");

    }

    public void dropTable() {

        db.execSQL("DROP TABLE IF EXISTS User;");

    }

    public void checkTable() {

        SQLiteDatabase db = getWritableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            if (c.moveToFirst()) {

                for (; ; ) {

                    Log.e(TAG, "table name : " + c.getString(0));

                    if (!c.moveToNext())

                        break;

                }

            }
        } catch (SQLiteException e) {

            Log.d("SearchTable", "table is nothing");
        }

    }

}
