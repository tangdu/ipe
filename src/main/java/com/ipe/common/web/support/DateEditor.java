package com.ipe.common.web.support;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Spring 对日期转换
 *
 * 默认使用有时分秒的，当不为时分秒的时。使用格式2
 */
public class DateEditor extends PropertyEditorSupport {

    private static SimpleDateFormat FORMATER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat FORMATERSIMPLE = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && text.trim().length() > 0) {
            try {
                setValue(FORMATER.parse(text));
            }
            catch (ParseException ex) {
                try {
                    setValue(FORMATERSIMPLE.parse(text));
                }catch (ParseException ex2) {
                }
            }
        }
    }

    @Override
    public String getAsText() {
        return (getValue() == null) ? "" : FORMATER.format(getValue());
    }
}
