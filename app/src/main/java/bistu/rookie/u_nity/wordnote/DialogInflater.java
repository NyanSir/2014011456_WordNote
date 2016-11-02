package bistu.rookie.u_nity.wordnote;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DialogInflater {

    private Context context;

    private String word_search;

    public DialogInflater(Context context){
        this.context = context;
    }

    public void inflateEditDialog(MenuItem item){
        View itemView;
        final String str_word;
        Words word;
        String str_meaning;
        String str_sample;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        itemView = info.targetView;
        str_word = ((TextView)itemView.findViewById(R.id.tv_fm_words_details_word)).getText().toString();
        word = MainActivity.dbOperator.query(str_word).get(0);
        str_meaning = word.getMeaning();
        str_sample = word.getSample();
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.words_add,null);
        final EditText et_word = (EditText) view.findViewById(R.id.et_word);
        final EditText et_meaning = (EditText) view.findViewById(R.id.et_meaning);
        final EditText et_sample = (EditText) view.findViewById(R.id.et_sample);
        et_word.setText(str_word);
        et_meaning.setText(str_meaning);
        et_sample.setText(str_sample);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("更改单词").setView(view).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("更改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String str_new_word = "";
                String str_new_meaning = "";
                String str_new_sample = "";
                str_new_word = et_word.getText().toString();
                str_new_meaning = et_meaning.getText().toString();
                str_new_sample = et_sample.getText().toString();
                MainActivity.dbOperator.updateWords(str_word,str_new_word,str_new_meaning,str_new_sample);
                if (context.getResources().getConfiguration().orientation == 2){
                    inflateDetailsFragment(MainActivity.dbOperator.query(str_new_word));
                }
            }
        }).show();
    }

    private void inflateDetailsFragment(List data){
        Bundle argument = new Bundle();
        argument.putSerializable("data", (Serializable) data);
        WordDetailFragment fragment = new WordDetailFragment();
        fragment.setArguments(argument);
        ((Activity)context).getFragmentManager().beginTransaction().replace(R.id.fm_land_word_details, fragment).commit();
    }

    public void inflateAddDialog(){
        View add_view = ((Activity)context).getLayoutInflater().inflate(R.layout.words_add,null);
        final EditText et_name = (EditText) add_view.findViewById(R.id.et_word);
        final EditText et_meaning = (EditText) add_view.findViewById(R.id.et_meaning);
        final EditText et_sample = (EditText) add_view.findViewById(R.id.et_sample);
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
        alert_builder.setView(add_view).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String word = et_name.getText().toString();
                String meaning = et_meaning.getText().toString();
                String sample = et_sample.getText().toString();
                MainActivity.dbOperator.insertWords(word,meaning,sample);
            }
        }).show();

    }

    public void inflateSearchDialog() {
        View search_view = ((Activity)context).getLayoutInflater().inflate(R.layout.words_search,null);
        final EditText et_word_search = (EditText) search_view.findViewById(R.id.et_word_search);
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
        alert_builder.setView(search_view).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                word_search = et_word_search.getText().toString();
                new Thread(new Task()).start();
            }
        }).show();
    }

    public void inflateTranslationDialog(final Map<String,String> content){
        //将查询的数据信息拼接在一个String中
        StringBuilder builder = new StringBuilder();
        builder.append(content.get(WordsDB.Word.COLUMN_NAME_WORD));
        builder.append(content.get(WordsDB.Word.COLUMN_NAME_MEANING));
        builder.append(content.get(WordsDB.Word.COLUMN_NAME_SAMPLE));
        View translation_view = ((Activity)context).getLayoutInflater().inflate(R.layout.words_online,null);
        final TextView tv_translation = (TextView) translation_view.findViewById(R.id.tv_dialog_translation);
        tv_translation.setText(builder.toString());
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(context);
        alert_builder.setView(translation_view).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("添加至单词本", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.dbOperator.insertWords(content.get(WordsDB.Word.COLUMN_NAME_WORD),
                        content.get(WordsDB.Word.COLUMN_NAME_MEANING),
                        content.get(WordsDB.Word.COLUMN_NAME_SAMPLE));
            }
        }).show();

    }

    private class Task implements Runnable{
        @Override
        public void run() {
            try {
                Message msg = MainActivity.handler_main.obtainMessage();
                msg.obj = WordsSearchOnline.analyseJSON(word_search,
                        WordsSearchOnline.getReply(word_search));
                MainActivity.handler_main.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}