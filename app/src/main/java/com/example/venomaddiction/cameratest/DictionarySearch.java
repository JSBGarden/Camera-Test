package com.example.venomaddiction.cameratest;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DictionarySearch extends AppCompatActivity {
    public String search_word;
    Button Search;
    public static EditText word;
    public TextView meanings;
    public ListView lstVMeaning;

    ArrayList<MeaningList> all_list = new ArrayList<MeaningList>();




    protected static final int SELECT_IMAGE = 100;
    private static final String TAG = "GalleryActivity";
    private int imgCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_search);
        Search=(Button) findViewById(R.id.btnDictionarySearch);
        word=(EditText) findViewById(R.id.etDictionarySearch);




        //meanings=(TextView) findViewById(R.id.txtResult);
        lstVMeaning=(ListView)findViewById(R.id.lstVMeaning);


        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            word.setText(extras.get("result").toString());
            perferm_search();

        }

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i=new Intent(DictionarySearch.this,DictionaryDisplay.class);
                //startActivity(i);
                //imageFromGallery();
                perferm_search();



            }
        });
    }
    public boolean OnKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        finish();
        return true;
    }

    public void imageFromGallery() {
        Intent getImageFromGalleryIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getImageFromGalleryIntent.setType("image/*");
        startActivityForResult(getImageFromGalleryIntent, SELECT_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == -1 && data != null) {
            this.imgCount++;
            String mSelectedImagePath = getPath(data.getData());
            Intent intent = new Intent(this, DictionaryDYMT.class);
            intent.setFlags(67108864);
            Bundle bundle = new Bundle();
            bundle.putParcelable("uri", data.getData());
            intent.putExtra("imageUri", bundle);
            intent.putExtra("image-path", mSelectedImagePath);
            intent.putExtra("scale", true);
            startActivity(intent);
            finish();
        } else if (resultCode == 0) {
            finish();
        } else {
            finish();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = new String[]{"_data"};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        String picture_path = cursor.getString(cursor.getColumnIndex(projection[0]));
        cursor.close();
        return picture_path;
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }


    public class MyParsingThread extends AsyncTask<String, Void, String> {
        //String host="192.168.1.111";
        String result="";

        private Exception exception;

        protected String doInBackground(String... search_word) {

            HashMap<String, String > post_data_params= new HashMap<String,String>();
            post_data_params.put("word",search_word[0]);
            String url="http://"+Constants.IP+":8080/word_meaning";
            String resultData=performPostCall(url,post_data_params);
            String type="";


            String test=resultData.substring(1,resultData.length()-2);//
            //String test="{ \"type\": \"n\", \"meaning\": \"the 26th letter of the Roman alphabet; \\\"the British call Z zed and the Scots call it ezed but Americans call it zee\\\"; \\\"he doesn''t know A from izzard\\\"\" }, { \"type\": \"n\", \"meaning\": \"the ending of a series or sequence; \\\"the Alpha and the Omega, the first and the last, the beginning and the end\\\"--Revelation\" }";
            //String test="{ \"type\": \"n\", \"meaning\": \"the 26th let\" }";
            test="{\"data\":[{"+test+"}]}";
            //String test="{\"test\":\"this is a test\"}";
            String data="";
            //Toast.makeText(getApplicationContext(),test,Toast.LENGTH_SHORT).show();
            try {

                JSONObject obj= new JSONObject(test);
                JSONArray meaningdata = obj.getJSONArray("data");
                final int n = meaningdata.length();
                all_list.clear();
                for (int i = 0; i < n; ++i) {
                    final JSONObject person = meaningdata.getJSONObject(i);
                    MeaningList meaningList=new MeaningList();
                    String meaning=person.getString("meaning");
                    String  wtype=person.getString("type");
                    //String  similar=person.getString("similar");
                    //String  antonym=person.getString("antonym");
                    //String  see_also=person.getString("see_also");

                    meaningList.setMeaning(meaning);
                    meaningList.setWord_type(wtype);
                    /*if (!similar.equals("None"))
                        meaningList.setSimilar(person.getString("similar"));
                    if (!antonym.equals("None"))
                    meaningList.setAntonym(person.getString("antonym"));
                    if (!see_also.equals("None"))
                    meaningList.setSee_also(person.getString("see_also"));
                    */
                    all_list.add(meaningList);

                }
                Log.d("DATA",data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }

        protected void onPostExecute(String feed) {
            if(all_list.size()>0){
                Myadapter listadp = new Myadapter(getApplicationContext(),R.id.lstVMeaning,all_list);
                lstVMeaning.setAdapter(listadp);



            }
            //DictionarySearch.word.setText(feed);
            // TODO: check this.exception
            // TODO: do something with the feed
        }





        ///////////////////////////
        public String  performPostCall(String requestURL,
                                       HashMap<String, String> postDataParams) {


            URL url;
            String response = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        //////////////
        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

    }












    public class Myadapter extends ArrayAdapter<MeaningList>
    {
        Context con;
        ArrayList<MeaningList> cur_all_list;
        public Myadapter(Context context, int resource,ArrayList<MeaningList> all_list) {
            super(context, resource);
            this.con = context;
            this.cur_all_list = all_list;
            // TODO Auto-generated constructor stub
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater my_new_inf = getLayoutInflater();
            View my_view=my_new_inf.inflate(R.layout.my_meaning_adpter_view, parent, false);
            TextView my_meaning_row = (TextView) my_view.findViewById(R.id.my_meaning_row);
            TextView my_similar_row = (TextView) my_view.findViewById(R.id.my_similar_row);

            MeaningList cur_class =  cur_all_list.get(position);
            my_meaning_row.setText(cur_class.getMeaning());
            my_similar_row.setText(cur_class.getWord_type());
            //my_layout.addView(my_view);
            return my_view;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cur_all_list.size();
        }

    }




    public void perferm_search(){
        search_word = word.getText().toString();
        MyParsingThread parseThread= new MyParsingThread();
        parseThread.execute(search_word);

    }


}
