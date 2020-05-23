package cz.cvut.fel.tlappka.events.tracking

import android.content.Context
import android.os.SystemClock
import android.widget.Chronometer


class MyChronometer(context: Context?) : Chronometer(context) {
    private var msElapsed : Long = 0
    var isRunning = false

    fun getMsElapsed(): Long {
        return msElapsed
    }

    fun setMsElapsed(ms: Long) {
        base -= ms
        msElapsed = ms
    }

    override fun start() {
        super.start()
        base = SystemClock.elapsedRealtime() - msElapsed
        isRunning = true
    }

    override fun stop() {
        super.stop()
        if (isRunning) {
            msElapsed = (SystemClock.elapsedRealtime() - this.base).toLong()
        }
        isRunning = false
    }
}