package db.zeffect.cn.contentprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

    public Uri Au;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Au = Uri.parse("content://" + getPackageName() + ".cn.zeffect.db.provider?table=test");
        findViewById(R.id.selectTest).setOnClickListener(this);
        findViewById(R.id.insertTest).setOnClickListener(this);
        findViewById(R.id.updateTest).setOnClickListener(this);
        findViewById(R.id.deleteTest).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.selectTest) {
            Cursor cursor = getContentResolver().query(Au, new String[]{"id", "name"}, "id=? and name=?", new String[]{"1", "2"}, null);
            if (cursor != null) {
                while (cursor.moveToFirst()) {
                }
                cursor.close();
            }
        } else if (v.getId() == R.id.deleteTest) {
            getContentResolver().delete(Au, "id=?", new String[]{"2"});
        } else if (v.getId() == R.id.insertTest) {
            ContentValues values = new ContentValues();
            values.put("id", 1);
            getContentResolver().insert(Au, values);
        } else if (v.getId() == R.id.updateTest) {
            ContentValues values = new ContentValues();
            values.put("id", 2);
            getContentResolver().update(Au, values, "id=?", new String[]{"1"});
        }
    }
}
