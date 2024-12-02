package com.example.sulochanasalon;

public enum NavigationItem
{
        PROFILE(R.id.nav_profile),
        APPOINTMENTS(R.id.nav_appointments),
        SERVICES(R.id.nav_services),
        PAYMENT_HISTORY(R.id.nav_payment_history),
        NOTIFICATIONS(R.id.nav_notifications),
        LOGOUT(R.id.nav_logout);

        private final int menuId;

        NavigationItem(int menuId) {
        this.menuId = menuId;
    }

        public int getMenuId() {
        return menuId;
    }

        public static NavigationItem fromMenuId(int id) {
        for (NavigationItem item : values()) {
            if (item.getMenuId() == id) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown menu item id: " + id);
    }
}

