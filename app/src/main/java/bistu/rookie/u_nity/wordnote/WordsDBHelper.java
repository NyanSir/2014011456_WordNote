package bistu.rookie.u_nity.wordnote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordsDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "wordsdb";
    private final static int DATABASE_VERSION = 1;
    private final static String SQL_CREATE_DATABASE = "CREATE TABLE " +
            WordsDB.Word.TABLE_NAME + " (" +
            WordsDB.Word._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            WordsDB.Word.COLUMN_NAME_WORD + " TEXT" + "," +
            WordsDB.Word.COLUMN_NAME_MEANING + " TEXT" + ","+
            WordsDB.Word.COLUMN_NAME_SAMPLE + " TEXT" + " )";
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + WordsDB.Word.TABLE_NAME;

    public WordsDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }
}
