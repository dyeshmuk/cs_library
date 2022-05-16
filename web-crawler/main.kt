import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.regex.Matcher
import java.util.regex.Pattern

fun main() {
    BasicWebCrawler().crawl(
        urls = listOf(
            "https://google.com",
            "https://facebook.com",
            "https://linkedin.com"
        ),
    )
}

data class Node(val url: String, val depth: Int)

class BasicWebCrawler {

    fun crawl(urls: List<String>) {
        val threadPool = Executors.newFixedThreadPool(8)
        urls.forEach {
            threadPool.execute(WebCrawlerWorker(baseUrl = it))
        }
        threadPool.shutdown()
    }
}

class WebCrawlerWorker(private val baseUrl: String) : Runnable {
    private val regexp = "(http|https)://(\\w+\\.)+(com|org)"
    private val pattern: Pattern = Pattern.compile(regexp)
    private val maxPages = 100 // max number of unique pages to be visited

    override fun run() {
        crawl(url = baseUrl)
    }

    private fun crawl(url: String) {
        val queue = ArrayDeque<Node>()
        val visited = HashSet<String>()
        queue.addLast(Node(url = url, depth = 0))
        visited.add(url)
        var cnt = 1
        while (!queue.isEmpty()) {
            val (link, depth) = queue.removeFirst()
            println("link->$link")
            val stream = runCatching {
                val urlConnection = URL(link).openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 500
                urlConnection.readTimeout = 1000
                urlConnection.inputStream
            }
            stream.onSuccess { inputStream ->
                inputStream.use {
                    val rawHtml = it.bufferedReader().readText()

                    val matcher: Matcher = pattern.matcher(rawHtml)
                    while (matcher.find()) {
                        val href: String = matcher.group()
                        if (!visited.contains(href) && cnt < maxPages) {
                            visited.add(href)
                            queue.addLast(Node(url = href, depth = depth + 1))
                            cnt++
                        }
                    }
                }
            }.onFailure {
                println("Failed to scrape the WEB resource[url='$url', error='$it']")
            }
        }
    }
}
