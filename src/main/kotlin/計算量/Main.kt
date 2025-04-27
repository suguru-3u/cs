package org.example.計算量

// =====================================
// 問題文：パフォーマンスチューニング
//
// 以下のプログラムは、リストの中から「偶数だけ」を取り出して
// 新しいリストを作る処理です。
// しかし、このコードは非常に「非効率」です。
//
// このプログラムを、より「効率よく（時間・空間計算量を意識して）」
// 書き直してください！
//
// 【条件】
// - Kotlinらしい書き方でOK（標準ライブラリ活用歓迎！）
// - できるだけムダな計算やメモリ確保を減らすこと
//
// 【ヒント】
// - どこでリストを作り直してる？
// - 無駄なループやオブジェクト生成を減らせない？
//
// =====================================

// 改善前
fun inefficientEvenFilter1(numbers: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (n in numbers) {
        if (n % 2 == 0) {
            val tempList = result.toMutableList() // 毎回コピーしている
            tempList.add(n)
            result.clear()
            result.addAll(tempList)
        }
    }
    return result
}

// 改善後
fun inefficientEvenFilter2(numbers: List<Int>): List<Int> {
    return numbers.filter { it % 2 == 0 }
}

// =====================================
// 問題文：重複を除いて昇順に並べる（パフォーマンスチューニング）
//
// 以下のプログラムは、リストの中から「重複を除き」
// さらに「昇順に並べる」処理をしています。
// しかし、このコードはとても非効率です。
//
// このプログラムを、より「効率よく（時間・空間計算量を意識して）」
// 書き直してください！
//
// 【条件】
// - Kotlinらしい書き方でOK（標準ライブラリ活用歓迎！）
// - できるだけムダなループや計算を減らすこと
//
// 【ヒント】
// - 重複を除くには？
// - 昇順にするには？
// - それぞれ最適なデータ構造は？
//
// =====================================

// 改善前
fun inefficientDedupAndSort1(numbers: List<Int>): List<Int> {
    val temp = mutableListOf<Int>()
    for (n in numbers) {
        if (!temp.contains(n)) {  // 毎回リストを走査して重複チェック
            temp.add(n)
        }
    }
    temp.sort() // 最後にソート
    return temp
}

// 改善後
// メモリ効率はよくないが、時間効率がいい
fun inefficientDedupAndSort2(numbers: List<Int>): List<Int> {
    return numbers.toSet().sorted()
}

// メモリ効率、時間効率ともにまあまあ
fun efficientDedupAndSort3(numbers: List<Int>): List<Int> {
    val treeSet = sortedSetOf<Int>()
    treeSet.addAll(numbers)
    return treeSet.toList()
}

//  メモリ最強、でも時間はそこそこかかってしまう
fun ultimateDedupAndSort(numbers: List<Int>): List<Int> {
    val result = mutableListOf<Int>()

    for (n in numbers) {
        if (n !in result) {
            result.add(n)
        }
    }
    result.sort()
    return result
}

/*
【問題】
与えられたリスト numbers: List<Int> に対して、
- 重複を除去し
- 偶数だけを残し
- 小さい順に並び替えたリストを返す関数

を作成してください。

ただし、以下の制約を守ってください。
- メモリ使用量は可能な限り少なくする（できればリスト1個だけ使う）
- 処理スピードにも配慮する（無駄なループは避ける）

関数名: `efficientEvenDedupAndSort`
引数: `numbers: List<Int>`
戻り値: `List<Int>`

【ヒント】
- 重複排除と偶数フィルタリングをうまくまとめよう！
- 最後に1回だけソートしよう！
*/

fun efficientEvenDedupAndSort(numbers: List<Int>): List<Int> {
    // ここに書いてください
    val treeSet = sortedSetOf<Int>()
    for (num in numbers) {
        if (num % 2 == 0) {
            treeSet.add(num)
        }
    }
    return treeSet.toList()
}

/*
問題：以下の条件を満たす関数を作成してください。

- 偶数だけを取り出し、重複を排除してソートしたリストを返す関数を作成します。
- メモリ使用量を最小化し、パフォーマンスを最大化してください。
- ListやSetなど、余計なデータ構造をできるだけ使わず、より効率的な方法を探ってください。
*/
fun efficientEvenDedupAndSortOptimized(numbers: List<Int>): List<Int> {
    val seen = mutableSetOf<Int>()
    val result = mutableListOf<Int>()

    for (num in numbers) {
        if (num % 2 == 0 && seen.add(num)) {  // Setに追加することで重複を自動的に排除
            result.add(num)
        }
    }

    return result.sorted()
}

// ↓ main関数でテストできるよ！
fun main() {
    val numbers1 = (1..100000).toList()
    val evens = inefficientEvenFilter2(numbers1)
    println(evens.take(10)) // [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]

    val numbers2 = listOf(5, 1, 2, 2, 4, 3, 1, 5, 6, 7, 3)
    val result = inefficientDedupAndSort2(numbers2)
    println(result) // [1, 2, 3, 4, 5, 6, 7]
}