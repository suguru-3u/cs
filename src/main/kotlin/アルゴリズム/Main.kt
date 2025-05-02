package org.example.アルゴリズム

/**
 * スライディングウィンドウ（Sliding Window）技法
 * 連続する区間（部分列）を効率よく扱うためのアルゴリズム手法です。
 * 特に、リスト・配列・文字列などの 部分区間を繰り返し処理する問題でよく使われます。
 *
 * 次のようにウィンドウ（部分列）を "スライド（ずらす）" して使い回します。
 *
 * 例：numbers = [1, 2, 3, 4, 5], k = 3
 *
 * 最初のウィンドウ：1, 2, 3 → 合計: 6
 * 次のウィンドウ：   2, 3, 4 → 前回の合計から「1 を引いて 4 を足す」→ 合計: 9
 * 次のウィンドウ：      3, 4, 5 → 合計: 12
 * このように 先頭の要素を引いて、末尾の新しい要素を加えることで、合計値などを再計算せずに処理できます。
 */
fun slidingWindow(numbers: List<Int>, k: Int): List<Double> {
    val result = mutableListOf<Double>()
    var windowSum = 0

    for (i in numbers.indices) {
        windowSum += numbers[i]  // 新しい要素を加える

        if (i >= k - 1) {
            result.add(windowSum / k.toDouble())  // 平均を計算
            windowSum -= numbers[i - k + 1]       // ウィンドウから古い要素を除く
        }
    }

    return result
}

