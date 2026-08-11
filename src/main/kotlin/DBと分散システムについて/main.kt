package org.example.DBと分散システムについて

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val csv = File("products.csv")

    // (1) 100万行の「商品テーブル」をCSVで作る
    val genMs = measureTimeMillis {
        csv.bufferedWriter().use { w ->
            w.write("id,name,price,stock\n")
            for (i in 1..1_000_000) {
                w.write("$i,item$i,${(i % 9000) + 100},${i % 50}\n")
            }
        }
    }
    println("生成: ${genMs}ms / サイズ ${csv.length() / 1024 / 1024}MB")

    // (2) id = 999999 を1件だけ探す
    //     SQL なら SELECT * FROM products WHERE id = 999999
    repeat(2) {
        var found: String? = null
        val ms = measureTimeMillis {
            csv.bufferedReader().use { r ->
                while (true) {
                    val line = r.readLine() ?: break
                    if (line.startsWith("999999,")) {
                        found = line
                        break
                    }
                }
            }
        }
        println("1件検索: ${ms}ms -> $found")
    }

    // (3) 集計
    //     SQL なら SELECT COUNT(*) FROM products WHERE price > 5000
    var count = 0
    val aggMs = measureTimeMillis {
        csv.bufferedReader().use { r ->
            r.readLine() // ヘッダを捨てる
            while (true) {
                val line = r.readLine() ?: break
                if (line.split(",")[2].toInt() > 5000) count++
            }
        }
    }
    println("集計: ${aggMs}ms -> ${count}件")
}
