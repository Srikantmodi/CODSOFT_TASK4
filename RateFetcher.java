import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RateFetcher {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public double getExchangeRate(String baseCurrency, String targetCurrency) throws IOException {
        String response = fetchRatesJson(baseCurrency);
        return parseRate(response, targetCurrency);
    }

    public double convertAmount(double amount, String baseCurrency, String targetCurrency) {
        try {
            double rate = getExchangeRate(baseCurrency, targetCurrency);
            return amount * rate;
        } catch (IOException exception) {
            return Double.NaN;
        }
    }

    private String fetchRatesJson(String baseCurrency) throws IOException {
        HttpURLConnection connection = null;
        try {
            // open the http connection and ask for the base currency snapshot
            URL url = new URL(API_URL + baseCurrency);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int statusCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    statusCode >= 200 && statusCode < 300
                            ? connection.getInputStream()
                            : connection.getErrorStream(),
                    StandardCharsets.UTF_8));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            if (statusCode < 200 || statusCode >= 300) {
                throw new IOException("rate service returned status " + statusCode);
            }

            return response.toString();
        } catch (IOException exception) {
            throw new IOException("unable to fetch exchange rates", exception);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private double parseRate(String json, String targetCurrency) throws IOException {
        // simple string parsing so we do not need a json library
        String token = "\"" + targetCurrency + "\":";
        int startIndex = json.indexOf(token);
        if (startIndex == -1) {
            throw new IOException("target currency not found");
        }

        startIndex += token.length();
        int endIndex = startIndex;
        while (endIndex < json.length()) {
            char current = json.charAt(endIndex);
            if ((current >= '0' && current <= '9') || current == '.' || current == 'E' || current == 'e' || current == '+' || current == '-') {
                endIndex++;
            } else {
                break;
            }
        }

        if (endIndex == startIndex) {
            throw new IOException("exchange rate value missing");
        }

        try {
            return Double.parseDouble(json.substring(startIndex, endIndex));
        } catch (NumberFormatException exception) {
            throw new IOException("invalid exchange rate format", exception);
        }
    }
}