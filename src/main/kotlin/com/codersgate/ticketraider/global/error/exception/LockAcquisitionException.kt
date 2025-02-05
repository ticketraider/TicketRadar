package com.codersgate.ticketraider.global.error.exception

class LockAcquisitionException(val msg : String) : RuntimeException(
    "락 획득에 실패하였습니다."
)
