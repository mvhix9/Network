package com.network.countriesinformation.Service;

import android.content.ContentValues;

import com.network.countriesinformation.Entity.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class ServiceCaller {
    public final static String GET = "GET";
    public final static String SET = "SET";

    public String call(String url, String method, ContentValues params)
    {
        InputStream in = null;
        BufferedReader br = null;
        StringBuilder stringBuilder = new StringBuilder();
        String linkService = url; //?(1)

        try {
            if(params.size() > 0)
            {
                stringBuilder.append(url);
                stringBuilder.append("?");
                Set<Map.Entry<String, Object>> valueSet = params.valueSet();
                for (Map.Entry<String , Object> entry : valueSet)
                {
                    String columnName = entry.getKey();
                    stringBuilder.append(entry.getKey().toString());
                    stringBuilder.append("=");
                    stringBuilder.append(entry.getValue().toString());
                    stringBuilder.append("&");
                }
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&")); //?
                linkService = stringBuilder.toString(); //?(2)
            }

            URL urlConn = new URL(linkService);
            HttpURLConnection httpConn = (HttpURLConnection)urlConn.openConnection();
            httpConn.setRequestMethod(method);
            httpConn.connect();
            int resCode = httpConn.getResponseCode();

            if(resCode == HttpURLConnection.HTTP_OK)
            {
                in = httpConn.getInputStream();
                br = new BufferedReader(new InputStreamReader(in,"UTF-8"));

                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = br.readLine())!= null)
                {
                    sb.append(s);
                    sb.append("\n");
                }
                return sb.toString();
            }
            else {
                Country country = new Country(httpConn.getResponseMessage(), 0,resCode,"20000.0"); //deo hieu lam
                return country.toJSON();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(in);
        }
        return null;
    }
    public static class IOUtils {
        public static void closeQuietly(InputStream in)  {
            try {
                in.close();
            }catch (Exception e) {

            }
        }
        public static void closeQuietly(Reader reader)  {
            try {
                reader.close();
            }catch (Exception e) {

            }
        }
    }
}
