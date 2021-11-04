package com.example.diary;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class diary_update extends AppCompatActivity {
    private TextView d_author;
    private TextView d_date;
    private EditText d_title;
    private EditText d_content;
    private ImageView img=null;
    private FloatingActionButton back;
    private FloatingActionButton bt_image;
    private diaryDao dao;
    private Intent intent;
    private  String iname="";   //图片名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);
        d_author=findViewById(R.id.d_author);
        d_content=findViewById(R.id.d_content);
        d_date=findViewById(R.id.d_date);
        d_title=findViewById(R.id.d_title);
        img=findViewById(R.id.img);
        back=findViewById(R.id.back);
        bt_image=findViewById(R.id.image);
        img = findViewById(R.id.img);
        DbUtil();
        intent = getIntent();
        String title = intent.getStringExtra("d_title");
        String content = intent.getStringExtra("d_content");
        String author = intent.getStringExtra("d_author");
        String date = intent.getStringExtra("d_date");
        iname =intent.getStringExtra("d_image");
        d_author.setText(author);
        d_date.setText(date);
        d_title.setText(title);
        d_content.setText(content);
        if(!iname.equals("")){
            Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/"+iname);
            img.setImageBitmap(bitmap);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取到两个输入框的值
                String t = d_title.getText().toString();
                String c = d_content.getText().toString();
                if(!t.equals("")){
                    intent = new Intent();
                    intent.putExtra("title",t);
                    intent.putExtra("content",c);
                    intent.putExtra("image",iname);
                    setResult(RESULT_OK,intent);
                    finish();
                } else if(t.equals("")){
                    finish();
                }
            }
        });
        bt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框，选择要直接拍照还是从相册中选择照片
                showPicturePicker(diary_update.this);
            }
        });
    }
    //图片
    private static final int TAKE_PICTURE = 0;  //拍照
    private static final int CHOOSE_PICTURE = 1;  //从相册中选择照片
    private static final int SCALE = 5;//照片缩小比例
    private String mImageName = null;
    private  File mImageFile;
    private Uri mImageUri;
    private String mImagePath;
    //弹出Dialog选择窗口
    public void showPicturePicker(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //设置弹出框标题
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照","相册"}, new DialogInterface.OnClickListener() {
            //类型码
            int REQUEST_CODE;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PICTURE:
                        //启动相机，设置存储路径
                        startCamera();
                        break;
                    case CHOOSE_PICTURE:
                        //发送打开相册程序器请求
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        REQUEST_CODE = CHOOSE_PICTURE;
                        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(openAlbumIntent, REQUEST_CODE);
                        break;

                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * 启动相机
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startCamera() {
        Intent intent = new Intent();
        //指定动作，启动相机
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //创建文件
        createImageFile();
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //获取uri
        mImageUri = FileProvider.getUriForFile(this, "com.example.diary.provider", mImageFile);
        //将uri加入到额外数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        //启动相机并要求返回结果
        startActivityForResult(intent, TAKE_PICTURE);
    }

    /**
     * 创建图片文件
     */
    private void createImageFile(){
        //设置图片文件名（含后缀），以当前时间的毫秒值为名称
        mImageName = Calendar.getInstance().getTimeInMillis() + ".jpg";
        //创建图片文件
        mImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + "Pictures" +"/", mImageName);
        //将图片的绝对路径设置给mImagePath，后面会用到
        mImagePath = mImageFile.getAbsolutePath();
        //按设置好的目录层级创建
        mImageFile.getParentFile().mkdirs();
        //不加这句会报Read-only警告。且无法写入SD
        mImageFile.setWritable(true);
    }

    /**
     * 处理返回数据，保存图片到本地并显示图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //将保存在SD的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
                    //然后删除该文件
                    ImageTools.delFile(mImagePath);
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    //将处理过的图片显示在界面上，并保存到本地
                    img.setImageBitmap(newBitmap);
                    String s=String.valueOf(System.currentTimeMillis());
                    ImageTools.savePhotoToSDCard(newBitmap,getFilesDir().getAbsolutePath(), s);
                    iname=s+".png";
                    break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();

                            //将处理过的图片显示在界面上，并保存到本地
                            img.setImageBitmap(smallBitmap);
                            String s1=String.valueOf(System.currentTimeMillis());
                            ImageTools.savePhotoToSDCard(smallBitmap,getFilesDir().getAbsolutePath(), s1);
                            iname=s1+".png";
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void DbUtil(){
        dao = ((MyApplication)this.getApplication()).getDao();
    }

}