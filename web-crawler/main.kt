import java.net.URL
import java.util.regex.Matcher
import java.util.regex.Pattern

fun main() {
    BasicWebCrawler().crawl(url = "https://google.com")
}

data class Node(val url: String, val depth: Int)

class BasicWebCrawler {
    private val queue = ArrayDeque<Node>()
    private val visited = HashSet<String>()
    private val regexp = "(http|https)://(\\w+\\.)+(com|org)"
    private val pattern: Pattern = Pattern.compile(regexp)
    private val maxDepth = 2

    fun crawl(url: String) {
        queue.addLast(Node(url = url, depth = 0))
        visited.add(url)
        var cnt = 0
        while(!queue.isEmpty()) {
            val (link, depth) = queue.removeFirst()
            if(depth < maxDepth) {
                val stream = runCatching {
                    val urlConnection = URL(link).openConnection()
                    urlConnection.connectTimeout = 500
                    urlConnection.readTimeout = 1000
                    urlConnection.getInputStream()
                }

                stream.onSuccess { inputStream ->
                    inputStream.use {
                        val rawHtml = it.bufferedReader().readText()

                        val matcher: Matcher = pattern.matcher(rawHtml)
                        while (matcher.find()) {
                            val href: String = matcher.group()
                            if (!visited.contains(href)) {
                                visited.add(link)
                                queue.addLast(Node(url = href, depth = depth + 1))
                            }
                        }
                    }
                }.onFailure {
                    println("Failed to scrape the WEB resource[url='$url', error='$it']")
                }
            }
        }
    }
}
