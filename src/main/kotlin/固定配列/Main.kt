package org.example.固定配列

// 問題：次の要求に基づいて、配列を操作するプログラムを作成してください。

// 1. 数字の配列を受け取る関数`reverseArray`を作成してください。
//    この関数は、配列の要素を逆順に並べ替えて返します。
fun reverseArray(array: Array<Int>): IntArray {
    val revertArray = IntArray(array.size)
    for (index in (array.size - 1) downTo 0) {
        revertArray[((array.size - (index + 1)))] = array[index]
    }
    return revertArray
}

// 2. 配列に含まれる偶数の合計を求める関数`sumOfEvens`を作成してください。
//    この関数は、配列の中で偶数であるすべての数の合計を返します。
fun sumOfEvens(array: Array<Int>): Int {
    var sum = 0
    array.forEach {
        if (it % 2 == 0) sum += it
    }
    return sum
}


// 3. 配列の要素のうち、最小の要素を返す関数`minElement`を作成してください。
//    もし配列が空の場合は`null`を返してください。
fun minElement(array: Array<Int>): Int? {
    if (array.isEmpty()) return null
    var minNum = 1
    array.forEach {
        if (minNum > it) {
            minNum = it
        }
    }
    return minNum
}

// 4. 配列の要素のうち、最初の要素が3以上の最初のインデックスを返す関数`findFirstGreaterThanThree`を作成してください。
//    もしそのような要素がなければ`-1`を返してください。
fun findFirstGreaterThanThree(array: Array<Int>): Int {
    val targetNumber = 3
    for ((index, value) in array.withIndex()) {
        if (targetNumber < value) {
            return index
        }
    }
    return -1
}

fun main() {
    val numbers = arrayOf(1, 2, 3, 4, 5, 6)
    reverseArray(numbers).forEach(::print) // 返り値: [6, 5, 4, 3, 2, 1]
    println()
    println(sumOfEvens(numbers)) // 返り値: 12 (2 + 4 + 6)
    println(minElement(numbers)) // 返り値: 1
    println(findFirstGreaterThanThree(numbers)) // 返り値: 3 (最初に3が現れるインデックス)


    val myList = MySimpleList<Int>()
    myList.add(0, 10)
    myList.add(1, 20)
    myList.add(2, 30)

    println(myList) // [10, 20, 30]
}

class MySimpleList<E> : AbstractMutableList<E>() {
    private val innerArray = arrayListOf<E>()

    override val size: Int
        get() = innerArray.size

    override fun set(index: Int, element: E): E {
        val old = innerArray[index]
        innerArray[index] = element
        return old
    }

    override fun get(index: Int): E {
        return innerArray[index]
    }

    override fun add(index: Int, element: E) {
        innerArray.add(index, element)
    }

    override fun removeAt(index: Int): E {
        return innerArray.removeAt(index)
    }
}