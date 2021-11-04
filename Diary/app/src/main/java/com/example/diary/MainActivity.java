package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private myadapter adapter;
    private Button write;
    private Button close;
    private diaryDao dao;
    private ArrayList<diary> list = new ArrayList<>();//用来存放数据的数组
    private ArrayList<diary> newList = new ArrayList<>();//用来存放数据的数组
    private int i=0;  //索引
    String author="wkp";//作者，默认为wkp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        write=findViewById(R.id.write);
        close=findViewById(R.id.close);
        DbUtil();   //初始化dao
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        if(!sharedPreferences.getString("author", "").equals("")){
            author =sharedPreferences.getString("author", "wkp");
        }
        list=dao.findall(author);   //从数据库得到日记列表
        adapter = new myadapter(MainActivity.this, R.layout.list_item, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //点击事件，查看日记
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, diary_update.class);
                i=position;
                intent.putExtra("d_author",list.get(i).getAuthor());
                intent.putExtra("d_date",list.get(i).getDate());
                intent.putExtra("d_title",list.get(i).getTitle());
                intent.putExtra("d_content",list.get(i).getContent());
                intent.putExtra("d_image",list.get(i).getImage());
                startActivityForResult(intent, i);
            }
        });

        /* 长按删除。*/
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //删除对应的item索引
                dao.delete(list.get(position));
                list.remove(position);
                //如果适配器发生变化，需要当前的listView也通知到。
                //对适配器重新进行初始化操作。

                //最简单的方法，不提倡。
//                adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.array_adapter_item,list);
//                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();//涉及到观察者模式
                return true;
            }
        });
    }

    /**
     * 初始化dao
     */
    public void DbUtil(){
        dao = ((MyApplication)this.getApplication()).getDao();
    }

    /**
     *跳转到写日记页面
     */
    public void write(View v){
        Intent intent = new Intent(MainActivity.this, diary_write.class);
        intent.putExtra("author",author);
        startActivity(intent);
    }

    /**
     *跳转到登录页面
     */
    public void close(View v){
        Intent intent = new Intent(MainActivity.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;   //清空login上面的所有activity并重新创建login
        startActivity(intent);
    }

    /**
     * 接受返回的数据
     * @param requestCode  记录哪一个页面返回的
     * @param resultCode 记录是否返回数据
     * @param data  数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==i){
                Bundle bundle = data.getExtras();
                diary d=list.get(i);
                d.setTitle(bundle.getString("title"));
                d.setContent(bundle.getString("content"));
                d.setImage(bundle.getString("image"));
                dao.update(d);
                getNotifyData();
            }
        }
    }

    /**
     * 当页面回到此活动时，调用此方法，刷新ListView
     * */
    @Override
    protected void onResume() {
        super.onResume();
        getNotifyData();
    }

    /**
     * 这个是用来动态刷新 * */
    public void getNotifyData() {
        //使用新的容器获得最新查询出来的数据
        newList = dao.findall(author);
        //清除原容器里的所有数据
        list.clear();
        //将新容器里的数据添加到原来容器里
        list.addAll(newList);
        //刷新适配器
        adapter.notifyDataSetChanged();
    }
}