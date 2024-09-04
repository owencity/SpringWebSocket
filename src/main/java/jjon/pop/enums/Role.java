package jjon.pop.enums;

import java.util.Arrays;

public enum Role {
    USER("ROLE_USER") , CONSULTANT("ROLE_CONSULTANT");

    String code;

    Role(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    // Role enum은 상수들의 집합이므로, 일반적으로 객체를 생성하여 사용하지 않습니다.
    // static 으로 선언함으로써 객체를 생성하지않고 Role 클래스에대한 자체 연산을 합니다.
    public static Role fromCode(String code) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getCode().equals(code))
                .findFirst()
                .orElseThrow();

        // 람다식 안쓰는 경우 아래 코드로 변환
//        public static Role fromCode(String code) {
//            return Arrays.stream(Role.values())
//                    .filter(new Predicate<Role>() {
//                        @Override
//                        public boolean test(Role role) {
//                            return role.getCode().equals(code);
//                        }
//                    })
//                    .findFirst()
//                    .orElseThrow();
        }
    }
}
