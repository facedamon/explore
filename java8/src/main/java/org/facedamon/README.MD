|使用案例|lambda关键字|函数式接口|
|---|---|---|
|布尔表达式|(List\<String> list) -> list.isEmpty()|Predicate\<List\<String>>|
|创建对象|() -> new Apple(10)|Supplier\<Apple>|
|消费一个对象|(Apple a) -> sout(a.getWeight)|Consumer\<Apple>|
|从一个对象中选择,提取|(String s) -> s.length()|Function\<String, Integer>或ToIntFunction\<String>|
|合并两个值|(int a, int b) -> a * b|IntBinaryOperator|
|比较两个值|(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight))|Comparator\<Apple>|