package assignment;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.DataProvider;

public class datasupply {
    @DataProvider(name="getflights", parallel = true)
    public String[] get_flights() throws Exception {

        JSONParser jparser = new JSONParser();
        FileReader reader = new FileReader("/Users/raghavendrakulkarni/git/repository/Test2/data.json");
        JSONObject jsonobj = (JSONObject)jparser.parse(reader);
        JSONArray jarray = (JSONArray)jsonobj.get("flights");
        String myflight[]=new String[jarray.size()];
        for(int i =0 ; i<jarray.size();i++) {
            JSONObject flight=(JSONObject)jarray.get(i);
            String from =(String)flight.get("from");
            String to =(String)flight.get("to");
            String dpDt =(String)flight.get("dpDt");
            //String rtDt =(String)flight.get("rtDt");
            //myflight[i] = from +"/"+to+"/"+dpDt+"/" +rtDt;

            myflight[i] = from +"/"+to+"/"+dpDt;
            //System.out.println();
        }
        return myflight;
    }
}
