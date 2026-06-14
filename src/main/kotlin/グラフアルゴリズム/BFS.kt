package org.example.グラフアルゴリズム

/**
 * BFSの特徴
 *
 * FIFOキューを使用して実装している。
 *
 * BFS のアイデア
 *
 * 「近い順から探索する」。
 * 具体的には:
 * 開始頂点 s をキューに入れる
 * キューから頂点を1つ取り出す
 * その隣接頂点のうち、 まだ訪問していないものをキューに入れる
 * キューが空になるまで繰り返す
 *
 * 「近いところから順に」 が「キューに入れた順に処理」 という FIFO の性質と完全に一致するので、
 * キューが BFS の自然な道具 になります。 第4章でやったキューの真の応用例です。
 *
 * 計算量
 *
 * 各頂点は最大1回キューに入る → 全頂点で O(V) の処理
 * 各辺は最大1回見られる(両端から1回ずつなので、 無向で2回)→ O(E)
 * 全体 O(V + E)
 *
 * これは「グラフのサイズに線形」 という、 大変効率的な計算量です。
 *
 * BFS の用途
 * 無重みグラフの最短経路(辺の重みが全部1のとき)
 * 連結成分の発見(BFS で訪問できる頂点群が1つの連結成分)
 * 木の階層的な走査(レベルごとに処理)
 * 6次の隔たり(知人関係を BFS で辿る)
 *
 * 動きを追ってみる
 *
 * グラフ:
 * 0 ─ 1 ─ 2
 * │       │
 * 3 ─ 4 ─ 5
 *
 * 開始: 頂点 0
 *
 * ここではスタートからの距離を出力している。
 */

fun bfs(adjList: List<List<Int>>, start: Int): IntArray {
    val n = adjList.size
    val distance = IntArray(n) { -1 }   // -1 は未訪問の意味
    distance[start] = 0
    val queue = ArrayDeque<Int>()
    queue.addLast(start)

    while (queue.isNotEmpty()) {
        val u = queue.removeFirst()
        for (v in adjList[u]) {
            if (distance[v] == -1) {   // 未訪問なら
                distance[v] = distance[u] + 1
                queue.addLast(v)
            }
        }
    }
    return distance
}

fun main() {
    // 上の例のグラフ
    val n = 6
    val adj = List(n) { mutableListOf<Int>() }
    fun addEdge(u: Int, v: Int) {
        adj[u].add(v); adj[v].add(u)
    }
    addEdge(0, 1);
    addEdge(0, 3)
    addEdge(1, 2)
    addEdge(2, 5)
    addEdge(3, 4);
    addEdge(4, 5)

    val dist = bfs(adj, 0)
    println(dist.toList())  // [0, 1, 2, 1, 2, 3]
}

