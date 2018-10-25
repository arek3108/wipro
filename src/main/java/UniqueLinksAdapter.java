import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by arek on 25.10.18.
 */
public class UniqueLinksAdapter implements LinksAdapter {
    private Set<String> savedLinks = new HashSet<>();

    @Override
    public Set<ResultLink> prepare(Set<ResultLink> resultLinks) {
        return resultLinks.stream()
                .filter(resultLink -> !savedLinks.contains(resultLink.getCurrentLink().toString()))
                .map(resultLink1 -> { savedLinks.add(resultLink1.getCurrentLink().toString()); return resultLink1; })
                .collect(Collectors.toSet());
    }
}
