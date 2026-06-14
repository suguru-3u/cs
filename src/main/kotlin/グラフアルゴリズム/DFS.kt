package org.example.グラフアルゴリズム

/**
 * DFSの特徴
 *
 * BFS と対になるのが DFS。 「行けるところまで突き進んで、 行き止まりになったら戻る」 という探索戦略です。
 *
 * DFS のアイデア
 * 迷路で「右手の壁伝いに進む」 戦略を想像してください。
 *
 * 開始頂点から、 隣接する未訪問の頂点に進む
 * そこから、 さらに未訪問の隣接頂点に進む
 * 進める頂点がなくなったら、 戻って別の道を試す
 * 「最後に進んだところから戻る」 という性質が スタック と一致します。 BFS がキュー(FIFO)なら、 DFS はスタック(LIFO)。 これも第4章の応用です。
 *
 * 動きを追ってみる(再帰版)
 * グラフ:
 * 0 ─ 1 ─ 2
 * │       │
 * 3 ─ 4 ─ 5
 *
 * 開始: 頂点 0
 * DFS の訪問順(隣接リストの順による): 0 → 1 → 2 → 5 → 4 → 3
 *
 * 頂点 0 から始める:
 * 0 に行く
 * 1 に行く
 * 2 に行く
 * 5 に行く
 * 4 に行く
 * 3 に行く (3 の隣接 0 は訪問済み、 行き止まり)
 * 戻る(4 の他の選択肢なし)
 * 戻る
 * 戻る
 * 戻る
 * 終了
 *
 * 訪問順: 0 → 1 → 2 → 5 → 4 → 3
 */

// スタック版
fun dfsIterative_stack(adjList: List<List<Int>>, start: Int): List<Int> {
    val n = adjList.size
    val visited = BooleanArray(n)
    val order = mutableListOf<Int>()
    val stack = ArrayDeque<Int>()
    stack.addLast(start)

    while (stack.isNotEmpty()) {
        val u = stack.removeLast()
        if (visited[u]) continue
        visited[u] = true
        order.add(u)
        // 隣接を逆順にスタックに入れると、 再帰版と同じ訪問順になる
        for (v in adjList[u].reversed()) {
            if (!visited[v]) stack.addLast(v)
        }
    }
    return order
}

// 再帰版
fun dfs(adjList: List<List<Int>>, start: Int): List<Int> {
    val n = adjList.size
    val visited = BooleanArray(n)
    val order = mutableListOf<Int>()

    fun visit(u: Int) {
        visited[u] = true
        order.add(u)
        for (v in adjList[u]) {
            if (!visited[v]) {
                visit(v)
            }
        }
    }

    visit(start)
    return order
}