package com.example.mybasket;

/**
 * Created by 박지훈 on 2016-07-22.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 박지훈 on 2016-06-23.
 */
public class Match_Out_NewsFeed_Camera extends Activity implements SurfaceHolder.Callback {
    @SuppressWarnings("deprecation")
    android.hardware.Camera camera;
    SurfaceView NewsFeed_Camera_SurfaceView;
    SurfaceHolder surfaceHolder;
    Button NewsFeed_Camera_Button;
    ImageView NewsFeed_Camera_Image;

    static String ImageURL;
    @SuppressWarnings("deprecation")
    android.hardware.Camera.PictureCallback jpegCallback;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_match_out_newsfeed_camera);

        NewsFeed_Camera_Button = (Button) findViewById(R.id.NewsFeed_Camera_Button);
        NewsFeed_Camera_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, jpegCallback);
            }
        });
        getWindow().setFormat(PixelFormat.UNKNOWN);
        Intent IntentURL = new Intent(getApplicationContext(), Match_Out_NewsFeed_Writing.class);
        NewsFeed_Camera_SurfaceView = (SurfaceView) findViewById(R.id.NewsFeed_Camera_SurfaceView);
        surfaceHolder = NewsFeed_Camera_SurfaceView.getHolder();
        surfaceHolder.addCallback(this);

        jpegCallback = new android.hardware.Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
                File pictureFile = getOutputMediaFile();

                try {
                    if(pictureFile == null){
                        Toast.makeText(getApplicationContext(), "Error camera image saving", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                    Intent IntentURL = getIntent();
                    IntentURL.putExtra("ImageURL",ImageURL );
                    setResult(RESULT_OK,IntentURL);
                }catch (Exception e){
                    Log.e("사진저장","사진실패",e);
                }
                finish();
            }
        };
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        try {
            camera.stopPreview();
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    private static File getOutputMediaFile(){
        //SD 카드가 마운트 되어있는지 먼저 확인
        // Environment.getExternalStorageState() 로 마운트 상태 확인 가능합니다
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // 없는 경로라면 따로 생성
        if(!mediaStorageDir.exists()){
            if(! mediaStorageDir.mkdirs()){
                Log.d("MyCamera", "failed to create directory");
                return null;
            }
        }

        // 파일명을 적당히 생성, 여기선 시간으로 파일명 중복을 피한다
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timestamp + ".jpg");
        ImageURL=timestamp;
        Log.i("MyCamera", "Saved at"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        Log.i("경로",mediaFile.getPath());
        return mediaFile;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = android.hardware.Camera.open();
        camera.stopPreview();
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder,int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void camera_ImageView(ImageView NewsFeed_Camera_Image){
        this.NewsFeed_Camera_Image = NewsFeed_Camera_Image;
    }
}
