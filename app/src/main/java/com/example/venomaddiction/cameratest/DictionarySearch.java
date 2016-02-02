package com.example.venomaddiction.cameratest;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DictionarySearch extends AppCompatActivity {
    public String search_word;
    Button Search;
    EditText word;
    public static TextView meanings;

    protected static final int SELECT_IMAGE = 100;
    private static final String TAG = "GalleryActivity";
    private int imgCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_search);
        Search=(Button) findViewById(R.id.btnDictionarySearch);
        word=(EditText) findViewById(R.id.etDictionarySearch);
        meanings=(TextView) findViewById(R.id.txtResult);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i=new Intent(DictionarySearch.this,DictionaryDisplay.class);
                //startActivity(i);
                //imageFromGallery();

                search_word = word.getText().toString();
                new Test().execute(search_word);



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







    public static class Test extends AsyncTask<String, Void, String> {
        String host="192.168.1.111";
        String result="";

        private Exception exception;

        protected String doInBackground(String... search_word) {

            HashMap<String, String > post_data_params= new HashMap<String,String>();
            post_data_params.put("word",search_word[0]);
            String url="http://"+Constants.IP+":8080/word_meaning";
            String resultData=performPostCall(url,post_data_params);
            //Log.d("DATA",resultData);
            /*
            try {
                URL url = new URL(" http://192.168.1.111:8080/word_meaning");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                StringBuilder sb = new StringBuilder();

                String templine;
                while ((templine = br.readLine()) != null) {
                    sb.append(templine + "\n");
                    // output=output+templine;//{each line ko object banaune bhako
                    // le we don't use this method.
                }

                String responseMsg = con.getResponseMessage();
                int response = con.getResponseCode();
                Log.d("MSGMSG", sb.toString());

            } catch (Exception e) {
                this.exception = e;

            }
            */
            Log.d("DATA", resultData);
            return resultData;
        }

        protected void onPostExecute(String feed) {
            DictionarySearch.meanings.setText(feed);
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


}
