package org.example.グラフアルゴリズム

/**
 * 何が問題?
 *
 * 地図アプリで「現在地から目的地まで最短時間で行きたい」 を考えてみてください。
 * 単純な BFS では、 各辺の重み(=距離や時間)を考慮できないので使えません。
 *
 * A ─5─ B ─5─ C
 * │              │
 * └──── 11 ────┘
 *
 * A から C への最短距離は?
 * ルート1: A → B → C = 5 + 5 = 10
 * ルート2: A → C = 11
 *
 * → ルート1 の方が短い。 これを発見したい。
 *
 * BFS で愚直に行くと「A → C を直接行く」 のが最短だと答えてしまう(辺数が少ないので)。 重みを考慮する仕組みが必要。

 * ダイクストラ法のアイデア*
 * 「現時点で確定している頂点の中で、 開始点から最短距離の頂点を選び、 そこから隣接頂点の距離を更新する」
 * そして、 「現時点で最短距離の頂点を素早く取り出す」 のに 優先度キュー(=ヒープ、 第7章) を使います。 ここでヒープが大活躍します!
 *
 * アルゴリズム
 * すべての頂点の距離を ∞ で初期化、 開始頂点の距離は 0
 * 優先度キュー(最小ヒープ)に開始頂点を (距離 0, 頂点) で入れる
 * キューから最小距離の頂点 u を取り出す
 * u の隣接頂点 v について、 (uまでの距離) + (u→v の重み) が現在の v の距離より小さければ更新し、 キューに入れる
 * キューが空になるまで繰り返す
 */

import java.util.PriorityQueue

data class Edge(val to: Int, val weight: Int)

fun dijkstra(adjList: List<List<Edge>>, start: Int): IntArray {
    val n = adjList.size
    val dist = IntArray(n) { Int.MAX_VALUE }
    dist[start] = 0

    // (distance, vertex) のペアを distance でソート
    val pq = PriorityQueue<IntArray>(compareBy { it[0] })
    pq.offer(intArrayOf(0, start))

    while (pq.isNotEmpty()) {
        val (d, u) = pq.poll()
        if (d > dist[u]) continue   // 古い情報はスキップ

        for (edge in adjList[u]) {
            val newDist = d + edge.weight
            if (newDist < dist[edge.to]) {
                dist[edge.to] = newDist
                pq.offer(intArrayOf(newDist, edge.to))
            }
        }
    }
    return dist
}

fun main() {
    // 上の例のグラフ
    val n = 6   // A=0, B=1, C=2, D=3, E=4, F=5
    val adj = List(n) { mutableListOf<Edge>() }
    fun addEdge(u: Int, v: Int, w: Int) {
        adj[u].add(Edge(v, w))
        adj[v].add(Edge(u, w))
    }
    addEdge(0, 1, 5)  // A-B 5
    addEdge(1, 2, 2)  // B-C 2
    addEdge(0, 3, 3)  // A-D 3
    addEdge(1, 4, 4)  // B-E 4
    addEdge(2, 5, 1)  // C-F 1
    addEdge(3, 4, 1)  // D-E 1
    addEdge(4, 5, 7)  // E-F 7

    val dist = dijkstra(adj, 0)
    println(dist.toList())   // [0, 5, 7, 3, 4, 8]
}