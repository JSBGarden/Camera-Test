package com.example.venomaddiction.cameratest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class OCRResult extends AppCompatActivity {
    //TextView result;
    ListView lstVWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrresult);


        //result=(TextView) findViewById(R.id.txtOCRResult);


        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            String value=extras.get("result").toString();
            value="{\"data\":"+value+"}";
            final ArrayList<String>word=new ArrayList<String>();
            try {
                JSONObject jobj=new JSONObject(value);
                JSONArray jarray=jobj.getJSONArray("data");
                final int n = jarray.length();
                for (int i = 0; i < n; ++i) {
                    final JSONObject Aword = jarray.getJSONObject(i);
                    word.add(Aword.getString("word"));

                }
                Myadapter data= new Myadapter(getApplicationContext(),R.layout.textviewadaptor,word);
                lstVWord=(ListView)findViewById(R.id.lstVWords);
                lstVWord.setAdapter(data);




                } catch (JSONException e) {
                e.printStackTrace();
            }





            /////////////////////////////////////////
            lstVWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Intent i=new Intent(OCRResult.this,DictionarySearch.class);
                    //Toast.makeText(getApplicationContext(),""+,Toast.LENGTH_LONG).show();
                    i.putExtra("result",word.get(position));
                    startActivity(i);
                }
            });



            //result.setText(value);
        }
    }











    public class Myadapter extends ArrayAdapter<String>
    {
        Context con;
        ArrayList<String> cur_all_list;
        public Myadapter(Context context, int resource,ArrayList<String> all_list) {
            super(context, resource);
            this.con = context;
            this.cur_all_list = all_list;
            // TODO Auto-generated constructor stub
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater my_new_inf = getLayoutInflater();
            View my_view=my_new_inf.inflate(R.layout.textviewadaptor,parent,false);
            TextView my_word_row = (TextView) my_view.findViewById(R.id.atxtWord);

            String cur_word =  cur_all_list.get(position);
            my_word_row.setText(cur_word);
            //my_layout.addView(my_view);
            return my_view;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cur_all_list.size();
        }

    }
}
