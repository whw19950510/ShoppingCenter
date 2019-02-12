
package enums;


import lombok.val;

public enum UserType {
    BUYER("buyer"),
    SELLER("seller");
    private final String value;

    UserType(final String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

    public static UserType valuesOf(String value){
        for(final UserType cur : UserType.values()) {
            if(cur.getValue().equalsIgnoreCase(value)) {
                return cur;
            }
        }
        throw new IllegalArgumentException();
    }
}
