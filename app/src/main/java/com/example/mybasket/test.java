package com.example.mybasket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;


public class test extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teammanager_teammake2);
        Button button =(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shouldOverrideUrlLoading("ww");
            }
        });
    }
    public boolean shouldOverrideUrlLoading(String url) {
        final int REQ_SELECT = 0;
        final String items[] = {"갤러리에서 가져오기", "카메라로 촬영하기"};
        if (url.startsWith("custom://")) {
            new AlertDialog.Builder(test.this)
                    .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Toast.makeText(getApplicationContext(), Integer.toString(item), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            if (item == 0) {
//갤러리 호출
                                Uri uri = Uri.parse("content://media/external/images/media");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, REQ_SELECT);
                            } else if (item == 1) {
//카메라로 찍기
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    })
                    .show();
        }
        return true;
    }
    //////////////////////////// 선택 하면 리턴값 받기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            if(!intent.getData().equals(null)){
                Bitmap selPhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
                selPhoto = Bitmap.createScaledBitmap(selPhoto, 100, 100, true);
//      image_bt.setImageBitmap(selPhoto);//썸네일
                Log.e("선택 된 이미지 ", "selPhoto : " + selPhoto);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}