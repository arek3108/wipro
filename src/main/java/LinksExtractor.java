import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by arek on 21.10.18.
 */
public class LinksExtractor {
    private final static Logger LOGGER = Logger.getLogger(LinksExtractor.class.getName());

    private static final String[] LINKS_ATTRIBUTE_PATTERNS = {
            "href=\"([^\"]*)\"",
            "href='([^\']*)'",
            "src=\"([^\"]*)\"",
            "src='([^\']*)'",
            "url(\"([^\"]*)\")",
            "url('([^\']*)')"
    };

    private static final Set<String> STATIC_CONTENT_CONTAINS = new HashSet<>(Arrays.asList(".js", ".png", ".jpg", ".jpeg", ".bmp", ".css", ".ico"));

    private Set<URL> links;
    private URL currentDomain;

    LinksExtractor(URL url, URL currentDomain, Map<String, String> cacheUrlContent) throws IOException {
        this.currentDomain = currentDomain;
        try {
            String content;
            if (cacheUrlContent != null && cacheUrlContent.containsKey(url.toString())) {
                content = cacheUrlContent.get(url.toString());
            } else {
                content = Utils.readStringFromUrl(url).replace(" ", "");
                if (cacheUrlContent != null) {
                    cacheUrlContent.put(url.toString(), content);
                }
            }
            links = extractLinks(content);
        } catch (IOException e) {
            throw new IOException("Error during reading content from URL: " + e.toString());
        }
    }

    private Set<URL> extractLinks(String content) {
        Set<String> links = new HashSet<>();
        for (String pattern: LINKS_ATTRIBUTE_PATTERNS) {
            Matcher matcherTag = Pattern.compile(pattern).matcher(content);
            while (matcherTag.find()) {
                links.add(matcherTag.group(1));
            }
        }
        return links.stream()
                .filter(s -> s.startsWith("//") || s.contains("://"))
                .map(s1 -> s1.startsWith("//") ? "http:" + s1 : s1)
                .map(s2 -> s2.contains("#") ? s2.split("#")[0] : s2)
                .map(s2 -> s2.endsWith("&") ? s2.substring(0, s2.length()-1) : s2)
                .map(s -> {
                    try {
                        return new URL(s);
                    } catch (MalformedURLException e) {
                        LOGGER.severe("Error during creating URL: " + e.toString());
                    }
                    return null;
                })
                .collect(Collectors.toSet());
    }

    public Set<ResultLink> getSameDomainLinks() {
        return links.stream()
                .filter(s -> s.getHost().replace("www.", "").equals(currentDomain.getHost().replace("www.", "")) && !isStaticContent(s))
                .map(ResultLink::new)
                .collect(Collectors.toSet());
    }

    public Set<ResultLink> getExternalLinks() {
        return links.stream()
                .filter(s -> !s.getHost().replace("www.", "").equals(currentDomain.getHost().replace("www.", "")) && !isStaticContent(s))
                .map(ResultLink::new)
                .collect(Collectors.toSet());
    }

    public Set<ResultLink> getStaticContentLinks() {
        return links.stream()
                .filter(this::isStaticContent)
                .map(ResultLink::new)
                .collect(Collectors.toSet());
    }

    private boolean isStaticContent(URL url) {
        return STATIC_CONTENT_CONTAINS.stream()
                .anyMatch(ext -> url.getFile().contains(ext));
    }
}
