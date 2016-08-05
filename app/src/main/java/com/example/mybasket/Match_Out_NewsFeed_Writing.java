package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 박지훈 on 2016-06-21.
 */
public class Match_Out_NewsFeed_Writing extends Activity {


    Intent dataIntent;


    Spinner NewsFeed_Writing_addDoSpinner;
    Spinner NewsFeed_Writing_addSiSpinner;
    Spinner NewsFeed_Writing_addCourtSpinner;
    ArrayList arr;
    Button NewsFeed_Writing_Button;
    EditText NewsFeed_Writing_TextEditText;
    EditText NewsFeed_Writing_PersonEditText;


    Button NewsFeed_Writing_CameraButton;
    ImageView NewsFeed_Camera_Image;
    Intent CameraIntent = null;

    ArrayAdapter<CharSequence> adspin1, adspin2, adspin3;
    static int spinnum1, spinnum2;
    String[][] parsedData;
    static String Do, Si, Court, ImageURL = null, ImageFile = null;
    static String Id = "";
    private static boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_match_out_newsfeed_writing);

        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");



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
                    Log.i("아이디", Id);

                    params.add(new BasicNameValuePair("NewsFeed_User",Id));
                    params.add(new BasicNameValuePair("NewsFeed_Data", NewsFeed_Writing_TextEditText.getText().toString()));
                    params.add(new BasicNameValuePair("NewsFeed_Month", new SimpleDateFormat("MM").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Day", new SimpleDateFormat("dd").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Hour", new SimpleDateFormat("kk").format(new java.sql.Date(System.currentTimeMillis()))));
                    params.add(new BasicNameValuePair("NewsFeed_Minute", new SimpleDateFormat("mm").format(new java.sql.Date(System.currentTimeMillis()))));


                    if (flag) {
                        params.add(new BasicNameValuePair("NewsFeed_Image", ImageFile));
                        String urlString = "http://210.122.7.195:8080/gg/newsfeed_Image_upload.jsp";
                        //파일 업로드 시작!
                        HttpFileUpload(urlString, "", ImageURL);
                    }else{
                        params.add(new BasicNameValuePair("NewsFeed_Image", "."));
                    }
                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    post.setEntity(ent);
                    HttpResponse response = client.execute(post);

                    flag = false;
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

                startActivityForResult(CameraIntent, 1);
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.dataIntent = data;
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                ImageURL = data.getStringExtra("ImageURL");
                ImageFile = data.getStringExtra("ImageFile");
                ImageDownload();
                flag = true;
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
        try {
            FileInputStream in;
            BufferedInputStream buf;
            in = new FileInputStream(String.valueOf(ImageURL));
            buf = new BufferedInputStream(in);
            Bitmap orgImage = BitmapFactory.decodeFile(String.valueOf(ImageURL));
            Bitmap resize = Bitmap.createScaledBitmap(orgImage, 1080, 1080, true);


            NewsFeed_Camera_Image.setVisibility(View.VISIBLE);
            NewsFeed_Camera_Image.setImageBitmap(resize);
//            NewsFeed_Camera_Image.setRotation(90);

        } catch (Exception e) {
        }
    }


    public void HttpFileUpload(String urlString, String params, String fileName) {


        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        // fileName=TeamName;
        try {
            //선택한 파일의 절대 경로를 이용해서 파일 입력 스트림 객체를 얻어온다.
            FileInputStream mFileInputStream = new FileInputStream(fileName);
            //파일을 업로드할 서버의 url 주소를이용해서 URL 객체 생성하기.
            URL connectUrl = new URL(urlString);
            //Connection 객체 얻어오기.
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);//입력할수 있도록
            conn.setDoOutput(true); //출력할수 있도록
            conn.setUseCaches(false);  //캐쉬 사용하지 않음

            //post 전송
            conn.setRequestMethod("POST");
            //파일 업로드 할수 있도록 설정하기.
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            //DataOutputStream 객체 생성하기.
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            //전송할 데이터의 시작임을 알린다.

            //String En_TeamName = URLEncoder.encode(TeamName, "utf-8");
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + URLEncoder.encode(ImageFile, "utf-8") + ".jpg" + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            //한번에 읽어들일수있는 스트림의 크기를 얻어온다.
            int bytesAvailable = mFileInputStream.available();
            //byte단위로 읽어오기 위하여 byte 배열 객체를 준비한다.
            byte[] buffer = new byte[bytesAvailable];
            int bytesRead = 0;
            // read image
            while (bytesRead != -1) {
                //파일에서 바이트단위로 읽어온다.
                bytesRead = mFileInputStream.read(buffer);
                if (bytesRead == -1) break; //더이상 읽을 데이터가 없다면 빠저나온다.
                Log.d("Test", "image byte is " + bytesRead);
                //읽은만큼 출력한다.
                dos.write(buffer, 0, bytesRead);
                //출력한 데이터 밀어내기
                dos.flush();
            }
            //전송할 데이터의 끝임을 알린다.
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            //flush() 타이밍??
            //dos.flush();
            dos.close();//스트림 닫아주기
            mFileInputStream.close();//스트림 닫아주기.
            // get response
            int ch;
            //입력 스트림 객체를 얻어온다.
            InputStream is = conn.getInputStream();
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            Log.e("Test", "result = " + s);

        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            Toast.makeText(this, "업로드중 에러발생!", Toast.LENGTH_SHORT).show();
        }
    }
}
