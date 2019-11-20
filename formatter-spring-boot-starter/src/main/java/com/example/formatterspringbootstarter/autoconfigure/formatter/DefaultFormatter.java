package com.example.formatterspringbootstarter.autoconfigure.formatter;

/**
 * 默认实现
 * @author Ryze
 * @date 2019-11-20 16:46
 */
public class DefaultFormatter implements Formatter {
    @Override
    public String formatter(Object object) {
        return String.valueOf(object);
    }
}
