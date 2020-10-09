package com.itlife.heavyheart.test;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author kex
 * @Create 2020/6/30 20:02
 * @Description
 */
public class Util {
    public static void main(String[] args) {
//        Random random = new Random();
//        int start = 800;
//        int end = 1600;
//        for (int i = 0; i < 30; i++) {
//            int num = random.nextInt(end - start + 1) + start;
//            System.out.println((num / 100D) + "s");
//        }

//        List<Test> testList = new ArrayList<>();
//        Test test1 = new Test(1, "test1", new BigDecimal("23.5"), 5);
//        Test test2 = new Test(2, "test2", new BigDecimal("32.5"), 10);
//        Test test3 = new Test(3, "test3", new BigDecimal("52.3"), 15);
//        Test test4 = new Test(4, "test4", new BigDecimal("53.2"), 20);
//        testList.add(test1);
//        testList.add(test2);
//        testList.add(test3);
//        testList.add(test4);
//        Map<Integer, Test> map = testList.stream().collect(Collectors.toMap(Test::getId, a -> a, (k1, k2) -> k1));
//        //Map<Integer, Test> map = (Map<Integer, Test>) Collectors.toMap(Test::getId, a -> a,(k1, k2)->k1);
//        map.forEach((k, v) -> System.out.println("k:v = " + k.toString() + ":" + v.getName()));
//        map.entrySet().iterator().forEachRemaining(entrySet -> System.out.println("k:v = " + entrySet.getKey().toString() + ":" + entrySet.getValue().getName()));
//        map.entrySet().forEach(entrySet -> System.out.println("k:v = " + entrySet.getKey().toString() + ":" + entrySet.getValue().getName()));
//        map.keySet().forEach(keySet -> System.out.println(keySet.toString() + ":" + map.get(keySet).getValue()));
//        map.values().forEach(value -> System.out.println(value.getNum()));

        List<String> names = Arrays.asList("A", "B", "C", "D", "E");
        List<String> namesWithA = names.stream()
                .filter(name -> name.startsWith("A"))
                .collect(Collectors.toList());
        namesWithA.forEach(n -> System.out.println(n));
    }
}
