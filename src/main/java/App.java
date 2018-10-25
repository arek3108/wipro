/**
 * Created by arek on 21.10.18.
 */
public class App {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.print("You must give min 2 arguments: url[String] and uniqueLinks[true/false]. 3rd argument: maxDepth is optional and default set as 1");
        } else {
            WebCrawler webCrawler = new WebCrawler(Utils.addProtocolIfMissing(args[0]), Boolean.valueOf(args[1]));
            if (args.length > 2) {
                webCrawler.setMaxDepth(Integer.valueOf(args[2]));
            }
            System.out.println(webCrawler.generateSitemap());
        }
    }
}
