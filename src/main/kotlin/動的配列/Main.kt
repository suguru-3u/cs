package org.example.動的配列

import java.util.ArrayDeque

/*
【問題】
整数のリストとウィンドウサイズ K が与えられます。
各連続する K 個の要素ごとに **最大値** を求めて、リストとして返してください。

【入力例】
val numbers = listOf(1, 3, -1, -3, 5, 3, 6, 7)
val k = 3

【出力例】
[3, 3, 5, 5, 6, 7]

【説明】
各ウィンドウにおける最大値を求めます：

- [1, 3, -1] → 3
- [3, -1, -3] → 3
- [-1, -3, 5] → 5
- [-3, 5, 3] → 5
- [5, 3, 6] → 6
- [3, 6, 7] → 7

【制約】
- K は 1 以上、numbers.size 以下
- 結果は List<Int> として返してください
*/

fun maxSlidingWindowV1(numbers: List<Int>, k: Int): List<Int> {
    // 実装する
    val recordArray = ArrayList<Int>()
    val resultArray = ArrayList<Int>()

    for (index in numbers.indices) {
        recordArray.add(numbers[index])
        if (index >= k - 1) {
            resultArray.add(recordArray.maxOrNull()!!)
            recordArray.removeAt(0)
        }
    }
    return resultArray
}

//【入力例】
// val numbers = listOf(1, 3, -1, -3, 5, 3, 6, 7)
// val k = 3
//
//【出力例】
// [3, 3, 5, 5, 6, 7]

fun maxSlidingWindowV2(numbers: List<Int>, k: Int): List<Int> {
    val result = mutableListOf<Int>()
    val deque = ArrayDeque<Int>() // インデックスを格納

    for (i in numbers.indices) {
        // 1. 範囲外のインデックスを削除
        println(i)
        if (deque.isNotEmpty() && deque.first() <= i - k) {
            println("--------------")
            println(deque.first())
            deque.removeFirst()
        }

        // 2. dequeの末尾から、今の値より小さいものを削除
        while (deque.isNotEmpty() && numbers[deque.last()] < numbers[i]) {
            println("***************")
            println(deque.last())
            deque.removeLast()
        }

        // 3. 今のインデックスをdequeに追加
        deque.addLast(i)

        // 4. ウィンドウのサイズに達したら最大値を記録
        if (i >= k - 1) {
            result.add(numbers[deque.first()])
        }
    }

    return result
}

fun main() {
    val numbers = listOf(1, 3, -1, -3, 5, 3, 6, 7)
    val k = 3
    maxSlidingWindowV2(numbers, k).forEach(::println)
}

/*
【問題】
整数のリストとウィンドウサイズKが与えられます。
連続するK個の要素ごとに平均を求め、それらをリストで返す関数を作成してください。

【入力例】
val numbers = listOf(1, 3, 2, 6, -1, 4, 1, 8, 2)
val k = 5

【出力例】
[2.0, 2.8, 3.6, 4.4, 2.8]

【解説】
以下のように、連続する5個の平均を順に計算します。
→ (1+3+2+6+(-1)) / 5 = 2.2
→ (3+2+6+(-1)+4) / 5 = 2.8
→ (2+6+(-1)+4+1) / 5 = 2.4
→ ...

【条件】
- Kは1以上、リストのサイズ以下。
- 小数点の平均を `Double` 型で返してください。
- 必ず新しいリストを返すこと。
*/

fun calculateAveragesV1(numbers: List<Int>, k: Int): List<Double> {
    // 実装を書く

    val recordArray = ArrayList<Double>()
    // numbersをループする
    for ((index, _) in numbers.withIndex()) {
        // ループした要素から4つ先の要素があるのか確認する
        if (numbers.size < index + k) continue
        // 4つ先の要素がある場合のみ、要素を足し算する
        // 足し算する要素が4つ先の要素分足し算したら、それ以降足し算しない
        var targetValue = 0
        for (targetIndex in index..((index - 1) + k)) {
            targetValue += numbers[targetIndex]
        }
        recordArray.add(targetValue / k.toDouble())
    }

    // 足し算した結果をレスポンスする
    return recordArray
}

fun calculateAveragesOptimizedV2(numbers: List<Int>, k: Int): List<Double> {
    val result = ArrayList<Double>()
    var windowSum = 0

    for (i in numbers.indices) {
        windowSum += numbers[i]
        if (i >= k - 1) {
            result.add(windowSum / k.toDouble())
            windowSum -= numbers[i - k + 1]
        }
    }

    return result
}


/*
【問題】
整数のリストが与えられます。
このリストから「連続して重複している要素」を1つにまとめた新しいリストを作成してください。

ただし、非連続な重複（たとえば間に別の値を挟んで再び同じ数が出てくる）については削除しないでください。

【入力例】
val numbers = listOf(1, 1, 2, 2, 2, 3, 1, 1, 4, 4, 5)

【出力例】
[1, 2, 3, 1, 4, 5]

【条件】
- 元の順序は保持してください。
- 重複を削除するのは連続している部分だけです。
- 新しいリストを返す関数を作成してください。
*/

fun removeConsecutiveDuplicatesV1(numbers: List<Int>): List<Int> {
    val recordArray = ArrayList<Int>()

    for (number in numbers) {
        if (recordArray.size == 0) {
            recordArray.add(number)
            continue
        }
        // 連続で同じ数字だった場合、次のループに移動する
        if (recordArray.last() == number) {
            continue
        }
        recordArray.add(number)
    }

    return recordArray
}

/*
【問題】
整数のリストを受け取り、そのリストから重複を取り除いた新しいリストを作成してください。
結果のリストは元の順序を保持しなければなりません。

【入力例】
val numbers = mutableListOf(3, 1, 2, 3, 4, 1, 5, 2)

【出力例】
[3, 1, 2, 4, 5]

【条件】
- 同じ数字が複数回出てくる場合は、最初の出現だけを残し、以降は削除してください。
- 新しいリストを返す関数を作成してください。
*/

fun removeDuplicates(numbers: MutableList<Int>): MutableList<Int> {
    // ここに実装を書く
    val recordArray = ArrayList<Int>()

    for (number in numbers) {
        if (recordArray.size == 0) {
            recordArray.add(number)
            continue
        }
        var recordFlg = false
        for (record in recordArray) {
            if (number == record) {
                recordFlg = false
                break
            } else {
                recordFlg = true
            }
        }
        if (recordFlg) {
            recordArray.add(number)
        }
    }

    return recordArray
}

fun removeDuplicatesV2(numbers: MutableList<Int>): MutableList<Int> {
    val recordArray = ArrayList<Int>()
    for (number in numbers) {
        if (recordArray.contains(number).not()) {
            recordArray.add(number)
        }
    }

    return recordArray
}

fun removeDuplicatesV3(numbers: MutableList<Int>): MutableList<Int> {
    return numbers.distinct().toMutableList()
}

fun removeDuplicatesV4(numbers: List<Int>): List<Int> {
    val seen = HashSet<Int>()
    val result = ArrayList<Int>()
    for (num in numbers) {
        if (seen.add(num)) {  // HashSet に新しく追加されたときだけ true
            result.add(num)
        }
    }
    return result
}