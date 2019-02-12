
package enums;


public enum Status {
    SOLD("sold"),
    ONSELL("onsell");
    private final String value;

    Status(final String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public static Status valuesOf(String value){
        for(final Status cur : Status.values()) {
            if(cur.getValue().equalsIgnoreCase(value)) {
                return cur;
            }
        }
        throw new IllegalArgumentException();
    }
}
