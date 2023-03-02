package hs.dcb.roacguys.configuration

import hs.dcb.roacguys.common.exception.MessageException
import hs.dcb.roacguys.listener.BasicMessageListener
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Aspect
class ExceptionAopConfig {

    val log = LoggerFactory.getLogger(BasicMessageListener::class.java)

    @Pointcut("execution(* hs.dcb.roacguys.listener..*(..))")
    fun defaultPointcut() {}

    @Around("defaultPointcut()")
    fun handleException(joinPoint: ProceedingJoinPoint): Any? {
        return try {
            joinPoint.proceed()
        } catch (e: MessageException) {
            log.error("MessageException Error Occurred : {}", e.message)
            null
        }
    }
}