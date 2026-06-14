package org.example.グラフアルゴリズム

/**
 * ある町に N 個の家があって、 すべての家を電線で繋ぎたい。 どの2軒を繋ぐにもコスト(距離)が違う。 すべての家を繋ぐ最小コスト はどうやって求める?
 *
 * グラフ:
 *   A ─4─ B
 *   │     │
 *   3     2
 *   │     │
 *   C ─5─ D
 *    \  /
 *     1
 *     │
 *     E
 *
 * すべての頂点を最小コストで繋ぐには?
 *
 * 「全部の頂点を繋ぐ、 でもサイクルは作らない、 そして合計重みが最小」。 つまり 木で覆う 形になります(木は n 頂点で n-1 辺、 Phase 0 第8章で学びましたね)。
 *
 * これを「全域木」 と呼び、 そのうちコストが最小のものを「最小全域木」 と呼びます。
 *
 * 用途
 *
 *
 * ネットワーク設計(光ファイバ、 道路、 電力網)
 * クラスタリング(似たもの同士をまとめる)
 * 画像処理(セグメンテーション)
 * 巡回セールスマン問題の近似解法
 *
 *
 * クラスカル法のアイデア
 *
 * シンプルです:
 *
 *
 * すべての辺を重みの小さい順にソート
 * 小さい順に追加していく。 ただし、 サイクルを作る辺はスキップ
 * n-1 本の辺を追加したら終了
 *
 *
 * この戦略は「貪欲法(greedy)」 と呼ばれます。 第10章で詳しくやりますが、 「その時点で最良に見える選択をする」 という方針。 ここでは「常に最も軽い辺を選ぶ」 だけで MST が得られる、 という美しい事実があります。
 * Union-Find ・ 「サイクルかどうか」 の判定
 * クラスカル法のキモは「この辺を追加するとサイクルになるか?」 の判定です。 これは Union-Find(またはDisjoint Set Union、 DSU) というデータ構造で高速にできます。
 * Union-Find の発想: 「2つの頂点が既に繋がっているグループにいる」 なら、 その間に辺を追加するとサイクルになる。
 */

class UnionFind(n: Int) {
    private val parent = IntArray(n) { it }   // 自分自身が親
    private val rank = IntArray(n)

    fun find(x: Int): Int {
        if (parent[x] != x) {
            parent[x] = find(parent[x])   // 経路圧縮
        }
        return parent[x]
    }

    fun union(x: Int, y: Int): Boolean {
        val rx = find(x)
        val ry = find(y)
        if (rx == ry) return false   // 既に同じグループ → ユニオン不要
        // 小さい木を大きい木にぶら下げる
        if (rank[rx] < rank[ry]) {
            parent[rx] = ry
        } else if (rank[rx] > rank[ry]) {
            parent[ry] = rx
        } else {
            parent[ry] = rx
            rank[rx]++
        }
        return true
    }
}


data class WeightedEdge(val u: Int, val v: Int, val weight: Int)

fun kruskal(n: Int, edges: List<WeightedEdge>): List<WeightedEdge> {
    // 重みで昇順ソート
    val sorted = edges.sortedBy { it.weight }
    val uf = UnionFind(n)
    val mst = mutableListOf<WeightedEdge>()

    for (edge in sorted) {
        if (uf.union(edge.u, edge.v)) {
            // 異なるグループだった → サイクル発生なし → 採用
            mst.add(edge)
            if (mst.size == n - 1) break   // n-1 本集まれば完成
        }
    }
    return mst
}

fun main() {
    // 上の例: A=0, B=1, C=2, D=3, E=4
    val edges = listOf(
        WeightedEdge(0, 1, 4),  // A-B 4
        WeightedEdge(0, 2, 3),  // A-C 3
        WeightedEdge(1, 3, 2),  // B-D 2
        WeightedEdge(2, 3, 5),  // C-D 5
        WeightedEdge(2, 4, 1),  // C-E 1
        WeightedEdge(3, 4, 1),  // D-E 1
    )
    val mst = kruskal(5, edges)
    println(mst)
    println("Total weight: ${mst.sumOf { it.weight }}")
    // 採用される辺: (C-E, 1), (D-E, 1), (B-D, 2), (A-C, 3)
    // 合計: 7
}