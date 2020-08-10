package com.example.daero;

import androidx.appcompat.app.AppCompatActivity;
import android.os.StrictMode;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView status1 = (TextView)findViewById(R.id.result);

        boolean initem = false, inAddr = false;

        String addr = null, chargeTp = null;
        try{
            URL url = new URL("http://api.korea.go.kr/openapi/svcAst/code?serviceKey=qJQdGNCT%2F3GLGiFxm%2F4dl38xVUMfclbVD7wUMriztrFn%2BmE9QyhdhHdwd7uitI%2BzNCrMDIzrrBYfh96LMhpkcQ%3D%3D&level=medium&upAstCd=010000&"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("svcAstCode")){ //title 만나면 내용을 받을수 있게 하자
                            inAddr = true;
                        }
                        if(parser.getName().equals("svcAstName")){ //address 만나면 내용을 받을수 있게 하자
                            initem = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            status1.setText(status1.getText()+"에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inAddr){ //isTitle이 true일 때 태그의 내용을 저장.
                            addr = parser.getText();
                            inAddr = false;
                        }
                        if(initem){ //isAddress이 true일 때 태그의 내용을 저장.
                            chargeTp = parser.getText();
                            initem = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            status1.setText(status1.getText()+"주소 : "+ addr +"\n 충전기 타입: "+ chargeTp);
                            initem = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            status1.setText("에러가..났습니다...");
        }




    }
}