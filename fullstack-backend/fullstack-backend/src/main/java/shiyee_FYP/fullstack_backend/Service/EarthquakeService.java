package shiyee_FYP.fullstack_backend.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EarthquakeService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, Object>> fetchEarthquakes(String period, double minMagnitude) {
        String url = buildUsgsApiUrl(period);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject root = new JSONObject(response.getBody());
        JSONArray features = root.getJSONArray("features");

        List<Map<String, Object>> results = new ArrayList<>();
        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            JSONObject properties = feature.getJSONObject("properties");
            JSONObject geometry = feature.getJSONObject("geometry");

            double mag = properties.optDouble("mag", 0);
            if (mag >= minMagnitude) {
                results.add(Map.of(
                        "title", properties.optString("title"),
                        "magnitude", mag,
                        "place", properties.optString("place"),
                        "time", properties.optLong("time"),
                        "longitude", geometry.getJSONArray("coordinates").optDouble(0),
                        "latitude", geometry.getJSONArray("coordinates").optDouble(1)
                ));
            }
        }
        return results;
    }

    private String buildUsgsApiUrl(String period) {
        switch (period) {
            case "hour":
                return "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";
            case "day":
                return "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";
            case "week":
                return "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
            default:
                return "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";
        }
    }
}
