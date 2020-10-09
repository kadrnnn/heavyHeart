package com.itlife.heavyheart.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @Author kex
 * @Create 2020/7/14 14:27
 * @Description
 */
@Component
public class MessageUtils {
    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource){
        MessageUtils.messageSource = messageSource;
    }

    public static String get(String msgKey){
        try{
           return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
        }catch (Exception e){
            return msgKey;
        }
    }
}
