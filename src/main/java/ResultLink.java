import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by arek on 21.10.18.
 */
public class ResultLink implements Comparable {
    private URL currentLink;
    private Set<ResultLink> sameDomainLinks = new HashSet<>();
    private Set<ResultLink> externalLinks = new HashSet<>();
    private Set<ResultLink> staticContentLinks = new HashSet<>();

    ResultLink(URL url) {
        this.currentLink = url;
    }

    public URL getCurrentLink() {
        return currentLink;
    }

    public void setCurrentLink(URL currentLink) {
        this.currentLink = currentLink;
    }

    public Set<ResultLink> getSameDomainLinks() {
        return sameDomainLinks;
    }

    public void setSameDomainLinks(Set<ResultLink> sameDomainLinks) {
        this.sameDomainLinks = sameDomainLinks;
    }

    public Set<ResultLink> getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(Set<ResultLink> externalLinks) {
        this.externalLinks = externalLinks;
    }

    public Set<ResultLink> getStaticContentLinks() {
        return staticContentLinks;
    }

    public void setStaticContentLinks(Set<ResultLink> staticContentLinks) {
        this.staticContentLinks = staticContentLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultLink that = (ResultLink) o;

        return currentLink.equals(that.currentLink);

    }

    @Override
    public int hashCode() {
        return currentLink.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
