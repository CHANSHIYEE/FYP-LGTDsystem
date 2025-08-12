package shiyee_FYP.fullstack_backend.model;

public enum GlobalTransportMode {
    SHIPPING("海运"),
    AIR("空运"),
    ROAD("公路"),
    RAIL("铁路"),
    PIPELINE("管道");

    private final String chineseName;

    GlobalTransportMode(String chineseName) {
        this.chineseName = chineseName;
    }


    public static GlobalTransportMode fromString(String text) {
        for (GlobalTransportMode mode : GlobalTransportMode.values()) {
            if (mode.name().equalsIgnoreCase(text)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("未知的运输方式: " + text);
    }
}