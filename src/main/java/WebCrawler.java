import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by arek on 21.10.18.
 */
public class WebCrawler {
    private final static Logger LOGGER = Logger.getLogger(WebCrawler.class.getName());

    private final static int MAX_DEPTH = 1;

    private Map<String, String> cacheUrlContent = new HashMap<>();

    private Set<ResultLink> allResultlinks = new HashSet<>();

    private URL currentDomainUrl;

    private LinksAdapter linksAdapter;

    private int maxDepth = MAX_DEPTH;

    WebCrawler(String url, boolean uniqueLinks) {
        if (uniqueLinks) {
            linksAdapter = new UniqueLinksAdapter();
        } else {
            linksAdapter = new LinksAdapter() {};
        }
        try {
            currentDomainUrl = new URL(url);
        } catch (MalformedURLException e) {
            LOGGER.severe("Error during creating URL: " + e.toString());
        }
    }

    public String generateSitemap() {
        ResultLink resultLink = computeResultLink(currentDomainUrl, 0);
        return generateSitemapAsString(resultLink, 0);
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    private ResultLink computeResultLink(URL url, int depth) {
        ResultLink resultLink = prepareResultLink(url);
        prepareResultLinksForSameDomain(resultLink, depth);
        if (depth < maxDepth) {
            for (ResultLink resultLinkSameDomain: resultLink.getSameDomainLinks()) {
                resultLink.getSameDomainLinks().add(computeResultLink(resultLinkSameDomain.getCurrentLink(), depth + 1));
            }
        }
        return resultLink;
    }

    private ResultLink prepareResultLink(URL url) {
        ResultLink resultLink = new ResultLink(url);
        try {
            LinksExtractor linksExtractor = new LinksExtractor(url, currentDomainUrl, cacheUrlContent);
            resultLink.setSameDomainLinks(linksAdapter.prepare(linksExtractor.getSameDomainLinks()));
            resultLink.setExternalLinks(linksAdapter.prepare(linksExtractor.getExternalLinks()));
            resultLink.setStaticContentLinks(linksAdapter.prepare(linksExtractor.getStaticContentLinks()));
        } catch (IOException e) {
            LOGGER.severe(e.toString());
        }
        return resultLink;
    }

    private void prepareResultLinksForSameDomain(ResultLink resultLink, int depth) {
        Set<ResultLink> sameDomainLinks = new HashSet<>();
        for (ResultLink resultLinkSameDomain: resultLink.getSameDomainLinks()) {
            if (depth < maxDepth && !allResultlinks.contains(resultLinkSameDomain)) {
                allResultlinks.add(resultLinkSameDomain);
                sameDomainLinks.add(prepareResultLink(resultLinkSameDomain.getCurrentLink()));
            } else {
                sameDomainLinks.add(resultLinkSameDomain);
            }
        }
        resultLink.setSameDomainLinks(sameDomainLinks);
    }

    private String generateSitemapAsString(ResultLink resultLink, int depth) {
        StringBuilder output = new StringBuilder();
        String indent = StringUtils.repeat("\t", depth);
        output.append(indent).append("\t").append(resultLink.getCurrentLink().toString()).append("\n");
        if (resultLink.getSameDomainLinks().size() > 0) {
            output.append(indent).append("\t\tSame domain links:\n");
            for (ResultLink link : resultLink.getSameDomainLinks()) {
                output.append(indent).append(generateSitemapAsString(link, depth + 1));
            }
        }
        if (resultLink.getExternalLinks().size() > 0) {
            output.append(indent).append("\t\tExternal domain links:\n");
            for (ResultLink link : resultLink.getExternalLinks()) {
                output.append(indent).append("\t\t\t").append(link.getCurrentLink().toString()).append("\n");
            }
        }
        if (resultLink.getStaticContentLinks().size() > 0) {
            output.append(indent).append("\t\tStatic content links:\n");
            for (ResultLink link : resultLink.getStaticContentLinks()) {
                output.append(indent).append("\t\t\t").append(link.getCurrentLink().toString()).append("\n");
            }
        }
        return output.toString();
    }
}
