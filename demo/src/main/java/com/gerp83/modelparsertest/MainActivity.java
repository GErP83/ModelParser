package com.gerp83.modelparsertest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gerp83.modelparser.ModelJsonUtil;
import com.gerp83.modelparsertest.model.Color;
import com.gerp83.modelparsertest.model.Color2;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by GErP83
 *
 */

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ArrayList<Color> colors = readFirstSample(this);
        readSecondSample(this);


        ArrayList<String> notNeededFields = new ArrayList<>();
        notNeededFields.add("notNeed");
        for(Color c : colors) {
            System.out.println("------------");
            System.out.println(ModelJsonUtil.toJson(c, notNeededFields).toString());
        }
    }

    public ArrayList<Color> readFirstSample(Context context) {
        ArrayList<Color> colors = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("sample1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONArray array = new JSONArray(json);

            for(int i = 0; i < array.length(); i++) {
                Color color = new Color(array.getJSONObject(i));
                colors.add(color);
                System.out.println("------------");
                System.out.println(color);

            }

        } catch (Throwable ex) {
            ex.printStackTrace();

        }
        return colors;
    }

    public void readSecondSample(Context context) {
        try {
            InputStream is = context.getAssets().open("sample2.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONArray array = new JSONArray(json);

            for(int i = 0; i < array.length(); i++) {
                Color2 color = new Color2(array.getJSONObject(i));
                System.out.println("------------");
                System.out.println(color);

            }

        } catch (Throwable ex) {
            ex.printStackTrace();

        }
    }

}
