package com.jcanseco.inventoryapi.dashboard.realtime;

public record DashboardUpdatedMessage(String type) {
    public static DashboardUpdatedMessage create() {
        return new DashboardUpdatedMessage("dashboard.updated");
    }
}
