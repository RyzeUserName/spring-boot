package com.example.boothello.profile;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * java 8实现
 * @author Ryze
 * @date 2019-11-04 15:55
 */
@Service
@Profile("java8")
public class Java8CalculateImpl implements Calculate {
    /**
     * 累加 计算
     * @param integer 计算的参数
     * @return Integer sum
     * @author Ryze
     * @date 2019-11-04 15:54:27
     */
    @Override
    public Integer sum(Integer... integer) {
        Integer reduce = Stream.of(integer).reduce(0, Integer::sum);
        System.out.printf("java8计算 %s 结果: %d\n", Arrays.asList(integer), reduce);
        return reduce;
    }
}
