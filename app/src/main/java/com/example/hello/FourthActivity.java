package com.example.hello;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FourthActivity extends AppCompatActivity implements Runnable{

    //定义法二中的对象名
    private final String TAG = "Rate";
    private float dollarRate = 0.0f;
    private float euroRate = 0.0f;
    private float wonRate = 0.0f;
    private String updateDate = "";

    EditText rmb;
    TextView show;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView) findViewById(R.id.showOut);

        //获取SharedPreferences里保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("mynate",Activity.MODE_PRIVATE);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        updateDate = sharedPreferences.getString("update_Date","");

        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr = sdf.format(today);

        Log.i(TAG,"onCreate: sp dollarRate=" + dollarRate);
        Log.i(TAG,"onCreate: sp euroRate=" + euroRate);
        Log.i(TAG,"onCreate: sp wonRate=" + wonRate);
        Log.i(TAG,"onCreate: sp updateDate=" + updateDate);

        //判断时间
        if(!todayStr.equals(updateDate)) {
            Log.i(TAG,"onCreate:需要更新");
            //开启子线程
            Thread t = new Thread(this);
            t.start();
        }else {
            Log.i(TAG,"onCreate:不需要更新");
        }


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                    //Object str = (String) msg.obj;
                    Bundle bd1 = (Bundle) msg.obj;
                    dollarRate = bd1.getFloat("dallar_rate");
                    euroRate = bd1.getFloat("euro_rate");
                    wonRate = bd1.getFloat("won_rate");

                    Log.i(TAG,"onActivityResult:dollarRate=" + dollarRate);
                    Log.i(TAG,"onActivityResult:euroRate=" + euroRate);
                    Log.i(TAG,"onActivityResult:wonRate=" + wonRate);

                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("mynate",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.putString("update_Date",todayStr);
                    editor.apply();

                    Toast.makeText(FourthActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

    }

    public void onClick(View btn) {
        //获取用户当前输入
        String str = rmb.getText().toString();
        float r = 0;
        if(str.length()>0) {
            r = Float.parseFloat(str);
        }else {
            //提示用户输入内容
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }

        //计算（法一）
//        float val = 0;
//         if(btn.getId()==R.id.btnDollar) {
//            val = r * (1/6.7f);
//        }else if(btn.getId()==R.id.btnEuro) {
//            val = r * (1/11f);
//        }else {
//            val = r * 500;
//        }
        //显示两位小数
//        DecimalFormat df = new DecimalFormat("0.00");
//        String ss = df.format(val);
//        show.setText(String.valueOf(ss));

        //计算（法二）
        if(btn.getId()==R.id.btnDollar) {
            show.setText(String.format("%.2f",r*dollarRate));
        }else if(btn.getId()==R.id.btnEuro) {
            show.setText(String.format("%.2f",r*euroRate));
        }else {
            show.setText(String.format("%.2f",r*wonRate));
        }

    }

    public void openOne(View btn) {
        Log.i("open","openOne:");
        //打开一个页面Activity
        openConfig();

    }

    private void openConfig() {
        Intent config = new Intent(this,FifthActivity.class);

        //把数据通过附加值的方式传递到下个页面
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        //startActivity(config);

        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set) {
            //打开配置对话框
            openConfig();

        }else if(item.getItemId()==R.id.open_list) {
            //打开列表窗口
            //Intent list = new Intent(this,RateListActivity.class);
            Intent list = new Intent(this,MyList2Activity.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode==2) {
            //获得intent对象的Bundle
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);

            //将新设置的汇率写到SharedPreferences中
            SharedPreferences sharedPreferences = getSharedPreferences("mynate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();

        }

    }

    @Override
    public void run() {
        for (int i = 1; i < 6; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //用于保存从网页中获取的汇率
        Bundle bundle;

//        //获取Msg对象，用于返回主线程
//        Message msg = handler.obtainMessage(5);
//        //msg.what(5);
//        msg.obj = "";
//        handler.sendMessage(msg);

        //获取网络数据
        //方法一：
        /*try {
            URL url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Document doc = Jsoup.parse(html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //方法二
        bundle = getFromBOC();

        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.what(5);
        msg.obj = "";
        msg.obj = bundle;
        handler.sendMessage(msg);

    }

    //从“中国银行”获取数据
    private Bundle getFromBOC() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj").get();
            Log.i(TAG, "run:" + doc.title());
            Elements tables = doc.getElementsByTag("table");
            int i = 1;
            Element table2 = tables.get(1);
            Log.i(TAG,"run:table2=" + table2);
            //获取TD中的数据
            Elements tds = table2.getElementsByTag("td");
            for(int j=0;j< tds.size();j+=8) {
                Element td1= tds.get(j);
                Element td2= tds.get(j+5);
                Log.i(TAG,"run:" + td1.text() + "==>" + td2.text());
                String str1 = td1.text();
                String val = td2.text();

                if("美元".equals(str1)) {
                    bundle.putFloat("dollar_rate",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)) {
                    bundle.putFloat("euro_rate",100f/Float.parseFloat(val));
                }else if("韩国元".equals(str1)) {
                    bundle.putFloat("won_rate",100f/Float.parseFloat(val));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }


    //从“美元人民币汇率网”获取数据
    private Bundle getFromUsdCny() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run:" + doc.title());
            Elements tables = doc.getElementsByTag("table");
            int i = 1;
            /*for (Element table : tables) {
                Log.i(TAG, "run:table[" + i + "]=" + table);
                i++;
            }*/
            Element table1 = tables.get(0);
            Log.i(TAG,"run:table1=" + table1);
            //获取TD中的数据
            Elements tds = table1.getElementsByTag("td");
            for(int j=0;j< tds.size();j+=6) {
                Element td1= tds.get(j);
                Element td2= tds.get(j+5);
                Log.i(TAG,"run:" + td1.text() + "==>" + td2.text());
                String str1 = td1.text();
                String val = td2.text();

                if("美元".equals(str1)) {
                    bundle.putFloat("dollar_rate",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)) {
                    bundle.putFloat("euro_rate",100f/Float.parseFloat(val));
                }else if("韩元".equals(str1)) {
                    bundle.putFloat("won_rate",100f/Float.parseFloat(val));
                }

            }
//            for(Element td : tds) {
//                Log.i(TAG,"run:td=" + td);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }


    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}
