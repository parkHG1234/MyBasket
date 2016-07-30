package com.example.mybasket;

/**
 * Created by 박지훈 on 2016-07-22.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by 박지훈 on 2016-06-23.
 */
public class Match_Out_NewsFeed_Camera extends Activity implements SurfaceHolder.Callback {
    @SuppressWarnings("deprecation")
    android.hardware.Camera camera;
    SurfaceView NewsFeed_Camera_SurfaceView;
    SurfaceHolder surfaceHolder;
    Button NewsFeed_Camera_Button;
    String str;
    ImageView NewsFeed_Camera_Image;
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


        NewsFeed_Camera_SurfaceView = (SurfaceView) findViewById(R.id.NewsFeed_Camera_SurfaceView);
        surfaceHolder = NewsFeed_Camera_SurfaceView.getHolder();
        surfaceHolder.addCallback(this);
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new android.hardware.Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, android.hardware.Camera camera) {

//                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                Bitmap bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.ball);

                Log.i("데이터2", String.valueOf(bitmap));
//                str = String.format(MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"사진저장","저장되었습니다"));
//
//                Uri uri = Uri.parse(str);
//                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));

                try {

//                    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 450, 200, true);
//                    NewsFeed_Camera_Image.setImageBitmap(resized);
//                    NewsFeed_Camera_Image.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 레이아웃 크기에 이미지를 맞춘다
//                    NewsFeed_Camera_Image.setPadding(3, 3, 3, 3);




//                    Toast.makeText(getApplicationContext(),"Capture",Toast.LENGTH_SHORT).show();

                    BitmapFactory.Options options = new BitmapFactory.Options();

                    //이미지 뷰 이미지 설정
                    NewsFeed_Camera_Image.setImageBitmap(bitmap);

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
