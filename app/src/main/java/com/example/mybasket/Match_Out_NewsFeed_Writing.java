package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 박지훈 on 2016-06-21.
 */
public class Match_Out_NewsFeed_Writing extends Activity {


    Spinner NewsFeed_Writing_addDoSpinner;
    Spinner NewsFeed_Writing_addSiSpinner;
    Spinner NewsFeed_Writing_addCourtSpinner;
    ArrayList arr;
    Button NewsFeed_Writing_Button;
    EditText NewsFeed_Writing_TextEditText;
    EditText NewsFeed_Writing_PersonEditText;


    Button NewsFeed_Writing_CameraButton;
    ImageView NewsFeed_Camera_Image;
    Intent CameraIntent=null;

    ArrayAdapter<CharSequence> adspin1, adspin2, adspin3;
    static int spinnum1, spinnum2;
    String[][] parsedData;
    static String Do, Si, Court, ImageURL = null,ImageFile=null;
    byte[] ImageData=null;
    private static boolean flag=false;
    private static final String TAG = "HelloCamera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_match_out_newsfeed_writing);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        NewsFeed_Writing_CameraButton = (Button) findViewById(R.id.NewsFeed_Writing_CameraButton);
        NewsFeed_Camera_Image = (ImageView) findViewById(R.id.NewsFeed_Camera_Image);


        NewsFeed_Writing_addDoSpinner = (Spinner) findViewById(R.id.NewsFeed_Writing_addDoSpinner);
        NewsFeed_Writing_addSiSpinner = (Spinner) findViewById(R.id.NewsFeed_Writing_addSiSpinner);
        NewsFeed_Writing_addCourtSpinner = (Spinner) findViewById(R.id.NewsFeed_Writing_addCourtSpinner);
        NewsFeed_Writing_Button = (Button) findViewById(R.id.NewsFeed_Writing_Button);
        NewsFeed_Writing_TextEditText = (EditText) findViewById(R.id.NewsFeed_Writing_TextEditText);
        NewsFeed_Writing_PersonEditText = (EditText) findViewById(R.id.NewsFeed_Writing_PersonEditText);
        adspin1 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Writing.this, R.array.spinner_do, R.layout.zfile_spinner_test);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NewsFeed_Writing_addDoSpinner.setAdapter(adspin1);
        NewsFeed_Writing_addDoSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        spinnum1 = i;
                        Do = adspin1.getItem(i).toString();
                        if (adspin1.getItem(i).equals("서울")) {
                            adspin2 = ArrayAdapter.createFromResource(Match_Out_NewsFeed_Writing.this, R.array.spinner_do_seoul, R.layout.zfile_spinner_test);
                            adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            NewsFeed_Writing_addSiSpinner.setAdapter(adspin2);
                            NewsFeed_Writing_addSiSpinner.setOnItemSelectedListener(
                                    new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            spinnum2 = i;
                                            Si = adspin2.getItem(i).toString();
                                            String result = "";
                                            try {
                                                HttpClient client = new DefaultHttpClient();
                                                String postURL = "http://210.122.7.195:8080/gg/CourtInformation.jsp";
                                                HttpPost post = new HttpPost(postURL);
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                params.add(new BasicNameValuePair("NewsFeed_Do", (String) adspin1.getItem(spinnum1)));
                                                params.add(new BasicNameValuePair("NewsFeed_Si", (String) adspin2.getItem(spinnum2)));
                                                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                                                post.setEntity(ent);
                                                HttpResponse response = client.execute(post);
                                                BufferedReader bufreader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                                                String line = null;
                                                while ((line = bufreader.readLine()) != null) {
                                                    result += line;
                                                }
                                                parsedData = jsonParserList(result);
                                                arr = new ArrayList();
                                                for (int a = 0; a < parsedData.length; a++) {
                                                    arr.add(parsedData[a][0]);
                                                }
                                                adspin3 = new ArrayAdapter<CharSequence>(Match_Out_NewsFeed_Writing.this, android.R.layout.simple_spinner_item, arr);
                                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                NewsFeed_Writing_addCourtSpinner.setAdapter(adspin3);
                                                NewsFeed_Writing_addCourtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        Court = adspin3.getItem(i).toString();
                                                        NewsFeed_Writing_Button.setEnabled(true);
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    }
                            );
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );


        NewsFeed_Writing_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HttpClient client = new DefaultHttpClient();
                    String postURL = "http://210.122.7.195:8080/gg/newsfeed_data_upload.jsp";
                    HttpPost post = new HttpPost(postURL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("NewsFeed_Do", Do));
                    params.add(new BasicNameValuePair("NewsFeed_Si", Si));
                    params.add(new BasicNameValuePair("NewsFeed_Court", Court));
                    params.add(new BasicNameValuePair("NewsFeed_UserCount", NewsFeed_Writing_PersonEditText.getText().toString()));
                    params.add(new BasicNameValuePair("NewsFeed_Data", NewsFeed_Writing_TextEditText.getText().toString()));
                    params.add(new BasicNameValuePair("NewsFeed_Month", new SimpleDateFormat("MM").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Day", new SimpleDateFormat("dd").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Hour", new SimpleDateFormat("kk").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Minute", new SimpleDateFormat("mm").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Image", ImageFile));

//                    if(flag){
////                        ImageUpload();
//                    }
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse response = client.execute(post);

                    flag=false;
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        NewsFeed_Writing_CameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Match_Out_NewsFeed_Camera camera = new Match_Out_NewsFeed_Camera();
                camera.camera_ImageView(NewsFeed_Camera_Image);
                CameraIntent = new Intent(Match_Out_NewsFeed_Writing.this, Match_Out_NewsFeed_Camera.class);

                startActivityForResult(CameraIntent,1);
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_OK) {
            if(requestCode==1) {
//                ImageData = data.getByteArrayExtra("ImageData");
                ImageURL = data.getStringExtra("ImageURL");
                ImageFile = data.getStringExtra("ImageFile");
                    ImageDownload();
                flag=true;
            }
        }
    }

    public String[][] jsonParserList(String pRecvServerPage) {
        try {
            JSONObject json = new JSONObject(pRecvServerPage);
            JSONArray jArr = json.getJSONArray("List");
            String[] jsonName = {"NewsFeed_Court"};
            String[][] parseredData = new String[jArr.length()][jsonName.length];
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                for (int j = 0; j < jsonName.length; j++) {
                    parseredData[i][j] = json.getString(jsonName[j]);
                }
            }
            return parseredData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void ImageDownload() {
            try{
            FileInputStream in;
            BufferedInputStream buf;
            in = new FileInputStream(String.valueOf(ImageURL));
            buf = new BufferedInputStream(in);
            Bitmap orgImage = BitmapFactory.decodeFile(String.valueOf(ImageURL));
            Bitmap resize = Bitmap.createScaledBitmap(orgImage, 1080, 852, true);
            NewsFeed_Camera_Image.setVisibility(View.VISIBLE);
            NewsFeed_Camera_Image.setImageBitmap(resize);
        }catch (Exception e){
        }
    }

    public void ImageUpload() {
        try {
            URL url = new URL("http://210.122.7.195:8080/gg/newsfeed_image_upload.jsp");
            Log.i(TAG, "http://210.122.7.195:8080/gg/newsfeed_image_upload.jsp" );
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            // open connection
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true); //input 허용
            con.setDoOutput(true);  // output 허용
            con.setUseCaches(false);   // cache copy를 허용하지 않는다.
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Connection", "Keep-Alive");
//            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // write data
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            Log.i(TAG, "Open OutputStream" );
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            // 파일 전송시 파라메터명은 file1 파일명은 camera.jpg로 설정하여 전송
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + ImageURL +".jpg"+"\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.write(ImageData,0,ImageData.length);

            Log.i(TAG, ImageData.length+"bytes written" );
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            dos.flush(); // finish upload...
            dos.close();
        } catch (Exception e) {
            Log.i(TAG, "exception " + e.getMessage());
            // TODO: handle exception
        }
//        Log.i(TAG, ImageData.length+"bytes written successed ... finish!!" );
//        try { dos.close(); } catch(Exception e){}

    }



}
