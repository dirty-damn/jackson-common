package top.senseiliu.jackson;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Assert;
import org.junit.Test;

public class TestApp {
    @Data
    @Accessors(chain = true)
    public static class User {
        private Long userId;
        private String name;
        private String gender;
        private Integer age;
    }

    @Data
    @Accessors(chain = true)
    public static class UserDTO {
        private String name;
        private String gender;
        private Integer age;
        private Integer high;
    }

    @Test
    public void JacksonTest() {
        User user = new User().setUserId(1001L).setName("lgl").setGender("1").setAge(24);

        // 对象 转 JacksonObject
        JacksonObject jacksonObject = Jackson.convertObject(user);
        Assert.assertNotNull(jacksonObject);

        // 列表 转 JacksonArray
        User user2 = new User().setUserId(1002L).setName("cyl").setGender("2").setAge(23);
        List<User> users = Arrays.asList(user, user2);
        JacksonArray jacksonArray = Jackson.convertArray(users);
        Assert.assertNotNull(jacksonArray);

        // 对象 转 对象
        UserDTO userDTO = Jackson.convert(user, UserDTO.class);
        Assert.assertEquals(userDTO.getName(), user.getName());
        Map userMap = Jackson.convert(user, Map.class);
        Assert.assertNotNull(userMap);
        Map<String, String> userMapWithType = Jackson.convert(user, new TypeReference<Map<String, String>>() {});
        Assert.assertNotNull(userMapWithType);

        // 对象 转 JsonString
        String userJsonStr = Jackson.objectToString(user);
        Assert.assertNotNull(userJsonStr);
        String userJsonStrPretty = Jackson.objectToStringPretty(user);
        Assert.assertNotNull(userJsonStrPretty);

        // JsonString 转 对象
        String jsonString = "{\"userId\":1001,\"name\":\"lgl\",\"gender\":\"1\",\"age\":24,\"birth\":1215}";
        User user1 = Jackson.parseJavaObject(jsonString, User.class);
        Assert.assertNotNull(user1);
        user1 = Jackson.parseJavaObject(jsonString, new TypeReference<User>() {});
        Assert.assertNotNull(user1);

        // JsonString 转 JacksonObject
        JacksonObject jacksonObject1 = Jackson.parseObject(jsonString);
        Assert.assertNotNull(jacksonObject1);

        // JsonString 转 JacksonArray
        String jsonArrayStr = "[{\"id\":1001,\"name\":\"lgl\",\"gender\":\"1\",\"age\":24,\"birth\":1215},{\"id\":1002,\"name\":\"cyl\",\"gender\":\"2\",\"age\":23,\"birth\":1014}]";
        JacksonArray jacksonArray1 = Jackson.parseArray(jsonArrayStr);
        Assert.assertNotNull(jacksonArray1);

        // 对象 转 byte[]
        byte[] bytes = Jackson.objectToBytes(user);
        Assert.assertNotNull(bytes);

        // 成员方法
        Assert.assertNotNull(jacksonObject.toJsonString());
        User user3 = jacksonObject.toJava(User.class);
        Assert.assertNotNull(user3);
        User user4 = jacksonObject.toJava(new TypeReference<User>() {});
        Assert.assertNotNull(user4);
        String s = jacksonObject.toString();
        Assert.assertNotNull(s);
    }

    @Test
    public void JacksonObjectTest() {
        String jsonString = "{\"id\":1,\"good\":true,\"desc\":\"123\",\"currentDate\":\"2022-04-03\",\"user\":{\"id\":1001,\"name\":\"lgl\",\"gender\":\"1\",\"age\":24,\"birth\":1215},\"userList\":[{\"id\":1001,\"name\":\"lgl\",\"gender\":\"1\",\"age\":24,\"birth\":1215},{\"id\":1002,\"name\":\"cyl\",\"gender\":\"2\",\"age\":23,\"birth\":1014}]}";

        JacksonObject jacksonObject = Jackson.parseObject(jsonString);
        Assert.assertFalse(jacksonObject.isEmpty());
        Assert.assertTrue(jacksonObject.size() > 0);
        Assert.assertTrue(jacksonObject.contains("id"));

        // 取 JacksonObject
        JacksonObject userJacksonObject = jacksonObject.getJacksonObject("user");
        Assert.assertNotNull(userJacksonObject);

        // 取 JacksonArray
        JacksonArray userList = jacksonObject.getJacksonArray("userList");
        Assert.assertNotNull(userList);

        // 取 Object
        Object user = jacksonObject.getObject("user");
        Assert.assertNotNull(user);
        // 取 原生JsonNode
        JsonNode user1 = jacksonObject.getNode("user");
        Assert.assertNotNull(user1);

        // 取 对象
        User user2 = jacksonObject.getObject("user", User.class);
        Assert.assertNotNull(user2);
//        TestApp.User user3 = jacksonObject.getJavaObject("user");
//        Assert.assertNotNull(user3);
        List<User> userList1 = jacksonObject.getObject("userList", new TypeReference<List<User>>() {});
        Assert.assertNotNull(userList1);

        // 取 byte[]
        byte[] users = jacksonObject.getBytes("user");
        Assert.assertNull(users);

        // 取属性
        // Boolean
        Boolean good = jacksonObject.getBoolean("good");
        boolean good1 = jacksonObject.booleanValue("good");
        Assert.assertEquals(good, good1);
        // Short
        Short id = jacksonObject.getShort("id");
        short id1 = jacksonObject.shortValue("id");
        Assert.assertEquals((short) id, id1);
        // Integer
        Integer id2 = jacksonObject.getInteger("id");
        int id3 = jacksonObject.intValue("id");
        Assert.assertEquals((int) id2, id3);
        // Long
        Long id4 = jacksonObject.getLong("id");
        long id5 = jacksonObject.longValue("id");
        Assert.assertEquals((long) id4, id5);
        // Float
        Float id6 = jacksonObject.getFloat("id");
        Assert.assertTrue(id6 > 0);
        float id7 = jacksonObject.floatValue("id");
        Assert.assertTrue(id7 > 0);
        // Double
        Double id8 = jacksonObject.getDouble("id");
        Assert.assertTrue(id8 > 0);
        double id9 = jacksonObject.doubleValue("id");
        Assert.assertTrue(id9 > 0);
        // BigDecimal
        BigDecimal id10 = jacksonObject.getBigDecimal("id");
        Assert.assertNotNull(id10);
        // BigInteger
        BigInteger id11 = jacksonObject.getBigInteger("id");
        Assert.assertNotNull(id11);
        // String
        String desc = jacksonObject.getString("desc");
        Assert.assertNotNull(desc);
        // LocalDateTime
        Date currentDate = jacksonObject.getDateTime("currentDate");
        Assert.assertNotNull(currentDate);

        // 放入属性
        JacksonObject jacksonObject1 = new JacksonObject();
        jacksonObject1.put("currentDate", new Date());
        String s = jacksonObject1.toJsonString();
        Assert.assertNotEquals(s, "");

        // 映射值
        Serializable currentDate1 = jacksonObject1.map("currentDate", o -> {
            JsonNode jsonNode = o.orElse(null);
            if (null == jsonNode) {
                return -1;
            }

            return jsonNode.asText();
        });
        Assert.assertNotNull(currentDate1);

        // 存在操作
        jacksonObject1.ifPresent("currentDate", System.out::println);

        jacksonObject1.remove("currentDate");
        Assert.assertEquals(0, jacksonObject1.size());
    }

    @Test
    public void JacksonArrayTest() {
        JacksonArray jacksonArray1 = new JacksonArray();
        Assert.assertNotNull(jacksonArray1);
        jacksonArray1.add(new User().setUserId(1003L).setName("lll").setGender("1").setAge(24));
        Assert.assertTrue(jacksonArray1.size() > 0);
        Assert.assertFalse(jacksonArray1.isEmpty());
        Assert.assertTrue(jacksonArray1.contains(0));
        jacksonArray1.remove(0);
        jacksonArray1.addAll(Arrays.asList(new User().setUserId(1003L).setName("lll").setGender("1").setAge(24),
                new User().setUserId(1004L).setName("lgl").setGender("1").setAge(24),
                new User().setUserId(1005L).setName("eee").setGender("1").setAge(24)));
        User zzz = new User().setUserId(1006L).setName("zzz").setGender("1").setAge(100);
        jacksonArray1.set(0, zzz);
        Assert.assertEquals("1006", jacksonArray1.get(0).get("userId").asText());
        Assert.assertEquals(jacksonArray1.indexOf(zzz), 0);
        Assert.assertNotNull(jacksonArray1.subList(1, 2));
        Assert.assertNotNull(jacksonArray1.getJacksonObject(1));
        Assert.assertNotNull(jacksonArray1.getObject(1, User.class));
        Assert.assertNotNull(jacksonArray1.getObject(1, new TypeReference<User>() {}));


        jacksonArray1.removeAll();

        String jsonArrayString = "[{\"id\":1001,\"name\":\"lgl\",\"gender\":\"1\",\"age\":24,\"birth\":1215},{\"id\":1002,\"name\":\"cyl\",\"gender\":\"2\",\"age\":23,\"birth\":1014}]";
        JacksonArray jacksonArray = Jackson.parseArray(jsonArrayString);
        Assert.assertNotNull(jacksonArray);

        for (JsonNode next : jacksonArray) {
            Assert.assertNotNull(next.get("name"));
        }
    }

    @Test
    public void OtherTest() {
        @Data
        @Accessors(chain = true)
        // 为null的字段不输出
        @JsonInclude(JsonInclude.Include.NON_NULL)
        // 命名策略
        @JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
        class Goods {
            private Long id;
            // 序列化别名
            // @JsonProperty("goodsName")
            private String goodsName;
            private String desc;
            private Integer price;
        }

        Goods goods = new Goods().setId(1001L).setGoodsName("可乐").setPrice(3);
        String s = Jackson.objectToString(goods);
        Assert.assertNotNull(s);

        String s1 = Jackson.objectToString(goods);
        Assert.assertNotNull(s1);
        String s2 = Jackson.objectToString(goods, PropertyNamingStrategies.UPPER_SNAKE_CASE);
        Assert.assertNotNull(s2);
    }
}