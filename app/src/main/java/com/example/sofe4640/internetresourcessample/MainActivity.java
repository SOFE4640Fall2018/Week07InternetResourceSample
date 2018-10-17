package com.example.sofe4640.internetresourcessample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    private String baseURL ="http://services.aonaware.com/DictService/DictService.asmx/Define?word=";
    private String definition= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void getOrigin(View v){
       EditText name = (EditText)findViewById(R.id.txtName);
       String url = baseURL + name.getText().toString().toLowerCase();

      DownloadDefinition task = new DownloadDefinition();
      task.execute(url);
    }


    private class DownloadDefinition extends AsyncTask <String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            try {
                url = new URL(params[0]);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document document = dBuilder.parse(url.openStream());
                document.getDocumentElement().normalize();
                NodeList main = document.getElementsByTagName("Definitions");
                if(main.getLength()>0 && main.item(0).getNodeType()==Node.ELEMENT_NODE){
                     Element definitions = (Element)main.item(0);
                    NodeList defTag= definitions.getElementsByTagName("WordDefinition");
                    for(int i =0;i<defTag.getLength();i++){
                        Node def= defTag.item(i);
                        definition += def.getTextContent().toString()+ "\n";
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            Log.d("TestDef.","definition" + definition);
            return definition;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //txtDefinition

            TextView textView = (TextView) findViewById(R.id.txtDefinition);
            textView.setText(s.trim());
        }
    }

}
