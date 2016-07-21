package com.example.mybasket;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AccountActivity extends Activity {
	private static final String TAG = "CameraTestActivity";
	// 카메라 제어
	private Camera camera;
	// 촬영된  사진보기
	private ImageView imageView;
	// 처리중
	private boolean inProgress;
	//카메라에 찍힌 이미지 데이터
	byte[] data;
	DataOutputStream dos;
	ImageView view;
	SurfaceView surfaceView;


	// 카메라 SurfaceView의 리스너
	private SurfaceHolder.Callback surfaceListener =
			new SurfaceHolder.Callback() {

				// SurfaceView가 생성되었을때 화면에 보여주기위한 초기화 작업을 수행
				// 카메라를 오픈하고, 프리뷰의 위치를 설정한다.

				public void surfaceCreated(SurfaceHolder holder) {
					camera = Camera.open();
					try {
						camera.setPreviewDisplay(holder);  //카메라의 preview 설정
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// SurfaceView가 화면에 표시되지않을때(액티비티가 비활성화 될때)호출한다.
				public void surfaceDestroyed(SurfaceHolder holder) {
					camera.release();
					camera = null;
				}
				// surfaceCreate()가 호출된 다음 호출된다.
				// 프리뷰의 크기를 설정하고 프리뷰 영상을 표시한다.
				public void surfaceChanged(SurfaceHolder holder,
										   int format,
										   int width,
										   int height) {
					// 카메라의 파라메터 값을 가져와서 미리보기 크기를 설정 하고
					// 프리뷰를 보여준다.
					Camera.Parameters parameters =
							camera.getParameters();
					parameters.setPreviewSize(width, height);
					camera.setParameters(parameters);
					camera.startPreview();
				}
			};


	// 카메라 셔트가 눌러질때
	private Camera.ShutterCallback shutterListener =
			new Camera.ShutterCallback() {

				public void onShutter() {

					if (camera != null && inProgress == false) {

						camera.takePicture(
								shutterListener,        // 셔터 후
								null,                       // Raw 이미지 생성 후
								picutureListener);    // JPE 이미지 생성 후
						inProgress = true;
					}
				}
			};


	Bitmap bitmap;
	// JPEG 이미지를 생성 후 호출
	private Camera.PictureCallback picutureListener =
			new Camera.PictureCallback() {
				public void onPictureTaken(byte[] data, Camera camera) {
					if (data != null) {
						//  적용할 옵션이 있는 경우 BitmapFactory클래스의 Options()
						//  메서드로 옵션객체를 만들어 값을 설정하며
						//  이렇게 만들어진 옵션을 Bitmap 객체를 만들때 네번째
						//  아규먼트로 사용한다.
						//
						//  처리하는 이미지의 크기를 축소
						//  BitmapFactory.Options options =
						//      new BitmapFactory.Options();
						//  options.inSampleSize = IN_SAMPLE_SIZE;
						AccountActivity.this.data=data;
						bitmap = BitmapFactory.decodeByteArray(data,
								0,
								data.length,
								null);
						//이미지 뷰 이미지 설정
						imageView.setImageBitmap(bitmap);
						doSaveFile();                 // sdcard에 파일 저장
						doFileUpload();              //서버에 이미지를 전송하는 메서드 호출
						Toast.makeText(AccountActivity.this, "서버에 파일을 성공적으로 전송하였습니다",
								Toast.LENGTH_LONG).show();

						camera.startPreview();   // 정지된 프리뷰를 재개
						inProgress = false;        // 처리중 플래그를 끔

					}
				}
			};

	File saveFile=new File("/mnt/sdcard/image01.jpg");   // 파일이 저장되는 경로 지정

	public void doSaveFile() {
//ggg
		OutputStream out = null;
		try {
			saveFile.createNewFile();
			out = new FileOutputStream(saveFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { out.close();  } catch (IOException e) {e.printStackTrace();}
		}
	}


	public void doFileUpload() {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			String url = "http://121.142.206.249:8080/web1/file.jsp";
			HttpPost post = new HttpPost(url);
			FileBody bin = new FileBody(saveFile);
			MultipartEntity multipart =
					new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipart.addPart("images", bin);

			post.setEntity(multipart);
			HttpResponse response = httpClient.execute(post);
			HttpEntity resEntity = response.getEntity();
		}catch(Exception e){e.printStackTrace();}
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 액티비티의 타이틀이 안보이도록 설정한다.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.camera);
		imageView = (ImageView) findViewById(R.id.ImageView01);
		surfaceView =
				(SurfaceView) findViewById(R.id.SurfaceView01);

		// surface를 제어하는 SurfaceHolder
		SurfaceHolder holder = surfaceView.getHolder();

		// SurfaceView 리스너를 등록
		holder.addCallback(surfaceListener);

		// 버퍼 사용하지 않음
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	// 키가 눌러졌을때 카메라 셔트가 눌러졌다고 이벤트 처리설정
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			switch(keyCode) {
				case KeyEvent.KEYCODE_CAMERA:
					shutterListener.onShutter();
					return true;
			}
		}
		return super.onKeyDown(keyCode,event);
	}

}