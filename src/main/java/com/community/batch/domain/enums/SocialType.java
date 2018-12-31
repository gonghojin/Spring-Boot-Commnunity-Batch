package com.community.batch.domain.enums;

public enum SocialType {
    FACEBOOK("facebook"),
    GOOGLE("google"),
    KAKAO("kakao");

    private final String ROLE_PRIFIX = "ROLE_";
    private String name;

    SocialType(String name) {
        this.name = name;
    }

    public String getRoleType() {
        return ROLE_PRIFIX + name.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public boolean isEquals(String authority) {
        return this.getRoleType().equals(authority);
    }
}
