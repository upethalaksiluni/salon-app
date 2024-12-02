package com.example.sulochanasalon;

public enum AdminMenuAction
{
    MANAGE_SERVICES(R.id.nav_manage_services),
    VIEW_USERS(R.id.nav_view_users),
    VIEW_PAYMENTS(R.id.nav_view_payments),
    ADD_SERVICE(R.id.nav_add_service),
    NAV_APPOINTMENTS(R.id.nav_appointments),
    NAV_NOTIFICATIONS(R.id.nav_notifications),
    LOGOUT(R.id.nav_logout);

    private final int menuId;

    AdminMenuAction(int menuId) {
        this.menuId = menuId;
    }

    public static AdminMenuAction fromId(int id) {
        for (AdminMenuAction action : values()) {
            if (action.menuId == id) {
                return action;
            }
        }
        return null;
    }
}