# wipro

Author: Arkadiusz Sarzynski

1. How to build and run your solution

project can be build with maven

after build jar file it can be used:
file.jar [url] [uniqueLinks] [maxDepth:optional]

[url] - url to site on base what sitemap should be created
[uniqueLinks] - true/false - if sitemap should contains only unique links
[maxDepth:optional] - haw many levels of links sitemap should contains

example: solution.jar http://www.google.pl true 1

2. Reasoning and describe any trade offs

First trade off to consider was how to find all urls on website. Solution(maybe not perfect, but not so time consuming)
was to use regular expression on content of website and find html attributes: href="..." src="..." url('...'). After that
links were normalized in method LinksExtractor.normalizeAndCreateURLs().

Second trade off was haw to find static content. I decided to create list of most popular file extensions and find them in links.

I also created simple cache mechanism(Map of Url->Content) in variable cacheUrlContent. It allows me to speed up application
to avoid getting contents again.

For create sitemap tree I used recursive method.

3. Explanation of what could be done with more time

Because of not enough time project is without tests. Junit dependency is added, but I focused on the working solution.
More time will allow me also to work on output(currently its simple String with sitemap tree). I should also make some refactoring.