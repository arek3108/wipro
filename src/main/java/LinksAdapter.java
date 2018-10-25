import java.util.Set;

/**
 * Created by arek on 25.10.18.
 */
public interface LinksAdapter {
    default Set<ResultLink> prepare(Set<ResultLink> resultLinks) {
        return resultLinks;
    }
}
