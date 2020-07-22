package com.myandroid.myfoodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private TextView textView;

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = (EditText)findViewById(R.id.searchEditText);
        textView = (TextView)findViewById(R.id.textView);

        findViewById(R.id.searchBtn).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.searchBtn :
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            data = getXmlData();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText(data);
                                }
                            });
                        }
                    }).start();

                    break;
            }
        }

        String getXmlData() {
            StringBuffer buffer = new StringBuffer();

            String str = searchEditText.getText().toString();
            String food_name = URLEncoder.encode(str);

            String queryUrl = "API주소 + 발급받은 KEY"
                    + food_name +"&pageNo=1&numOfRows=100";

//            System.out.println("쿼리 : " + queryUrl);

            try {
                URL url = new URL(queryUrl);
                InputStream is = url.openStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;

                xpp.next();
                int eventType = xpp.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            buffer.append("파싱 시작...\n\n");
                            break;

                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();

                            if(tag.equals("item"));
                            else if(tag.equals("DESC_KOR")) {
                                buffer.append("식품명 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if(tag.equals("SERVING_WT")) {
                                buffer.append("1회 제공량 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if(tag.equals("NUTR_CONT1")) {
                                buffer.append("열량(kcal) : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if(tag.equals("NUTR_CONT2")) {
                                buffer.append("탄수화물(g) : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if(tag.equals("NUTR_CONT3")) {
                                buffer.append("단백질(g) : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if(tag.equals("NUTR_CONT4")) {
                                buffer.append("지방(g) : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if(tag.equals("NUTR_CONT5")) {
                                buffer.append("당류(g) : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if(tag.equals("NUTR_CONT6")) {
                                buffer.append("나트륨(mg) : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if(tag.equals("NUTR_CONT7")) {
                                buffer.append("콜레스테롤(mg) : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if(tag.equals("NUTR_CONT8")) {
                                buffer.append("포화지방산(g) : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if(tag.equals("NUTR_CONT9")) {
                                buffer.append("트랜스지방산(g) : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                            break;

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            tag = xpp.getName();

                            if(tag.equals("item")) {
                                buffer.append("\n");
                            }

                            break;
                    }

                    eventType = xpp.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            buffer.append("파싱 끝\n");

            return buffer.toString();
        }
    };
}