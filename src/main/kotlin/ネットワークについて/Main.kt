package org.example.ネットワークについて

import java.net.InetAddress

fun main() {
    val host = InetAddress.getByName("github.com")

    // タイムアウト 2000ms で「到達可能か」を確認(ping に近い疎通確認)
    val reachable = host.isReachable(20000)
    println("${host.hostAddress} reachable = $reachable")
}