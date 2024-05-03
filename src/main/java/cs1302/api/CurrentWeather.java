package cs1302.api;

import com.google.gson.annotations.SerializedName;
/**Represents the information of current weather inside WeatherResponse.*/

public class CurrentWeather {

    @SerializedName("temp_f") double tempF;
    @SerializedName("wind_mph") double windMPH;
    @SerializedName("precip_in") double precipIN;
    int humidity;
    Condition condition;
}
