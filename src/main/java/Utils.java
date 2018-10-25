import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Created by arek on 21.10.18.
 */
public class Utils {
    private static String DEFAULT_PROTOCOL = "http";

    static String addProtocolIfMissing(String url) {
        url = url.replace(":\\\\", "://");
        if (!url.contains("://")) {
            return DEFAULT_PROTOCOL + "://" + url;
        }
        return url;
    }

    static String readStringFromUrl(URL url) throws IOException {
        String outputString;
        URLConnection conn = url.openConnection();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            outputString = reader.lines().collect(Collectors.joining("\n"));
        }
        return outputString;
    }
}
