package pl.ife.tcs.commonlib.util

import java.util.*
import java.util.concurrent.ThreadLocalRandom

object RandUtil{

    @JvmStatic fun getRandomString(): String {
        return UUID.randomUUID().toString()
    }

    @JvmStatic fun getRandomLong(): Long {
        return getRandomLong(Int.MIN_VALUE.toLong(), Int.MAX_VALUE.toLong())
    }

    @JvmStatic fun getRandomLong(from: Long, to: Long): Long {
        return ThreadLocalRandom.current().nextLong(from, to)
    }

    @JvmStatic fun <T> pickRandomElements(list: List<T>, n: Int): List<T> {
        val rand = ThreadLocalRandom.current()
        val length = list.size

        if (length < n) return list

        for (i in length - 1 downTo length - n) {
            Collections.swap(list, i, rand.nextInt(i + 1))
        }

        return list.subList(length - n, length)
    }
}