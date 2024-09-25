package com.happy.friendogly.firebase.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class CrashlyticsHelper
    @Inject
    constructor() {
        private val crashlytics = FirebaseCrashlytics.getInstance()

        /**
         * crashlytics 설정은 완료했씀돠!
         * Coroutine Exception Handler에 심어두면 좋을 것 같음!!
         * 그 외에 예외가 발생해도 큰 문제는 없지만, 예외를 기록해두고 싶다면 심어두어도 좋을 것 같음!
         */
        fun logError(throwable: Throwable?) {
            throwable ?: return
            crashlytics.log(throwable.message ?: ERROR_MESSAGE)
            crashlytics.recordException(throwable)
        }

        companion object {
            private const val ERROR_MESSAGE = "알 수 없는 에러"
        }
    }
